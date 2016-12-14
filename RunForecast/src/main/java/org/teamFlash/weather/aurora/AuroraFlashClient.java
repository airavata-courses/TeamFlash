/**
 * Created by girish on 12/10/16.
 */
package org.teamFlash.weather.aurora;
import java.io.FileInputStream;
import java.text.MessageFormat;
import java.util.*;


import org.apache.thrift.TException;
import org.teamFlash.weather.aurora.client.sdk.*;
import org.teamFlash.weather.aurora.bean.*;


import org.teamFlash.weather.aurora.utils.AuroraThriftClientUtil;
import  org.teamFlash.weather.aurora.utils.Constants;
import org.teamFlash.weather.aurora.client.AuroraThriftClient;
import  org.teamFlash.weather.aurora.client.AuroraSchedulerClientFactory;
import org.teamFlash.weather.forecast.JobBean;


public class AuroraFlashClient {

    /** The aurora scheduler client. */
    private static ReadOnlyScheduler.Client auroraSchedulerClient;

    /** The properties. */
    private static Properties properties = new Properties();

    /** The aurora scheduler manager client. */
    private AuroraSchedulerManager.Client auroraSchedulerManagerClient = null;

    public static void getJobSummary(ReadOnlyScheduler.Client client) {
        try {
            Response response = client.getJobs("team-flash");
            System.out.println("Response status: " + response.getResponseCode().name());
            if(response.getResult().isSetGetJobsResult()) {
                GetJobsResult result = response.getResult().getGetJobsResult();
                System.out.println(result);
                Set<JobConfiguration> jobConfigs = result.getConfigs();
                for(JobConfiguration jobConfig : jobConfigs) {
                    System.out.println(jobConfig);
                    JobKey jobKey = jobConfig.getKey();
                    Identity owner = jobConfig.getOwner();
                    TaskConfig taskConfig = jobConfig.getTaskConfig();
                    ExecutorConfig exeConfig = taskConfig.getExecutorConfig();

                    System.out.println("\n**** JOB CONFIG ****");
                    System.out.println("\t # instanceCount: " + jobConfig.getInstanceCount());

                    System.out.println("\t >> Job Key <<");
                    System.out.println("\t\t # name: " + jobKey.getName());
                    System.out.println("\t\t # role: " + jobKey.getRole());
                    System.out.println("\t\t # environment: " + jobKey.getEnvironment());

                    System.out.println("\t >> Identity <<");
                    System.out.println("\t\t # owner: " + owner.getUser());

                    System.out.println("\t >> Task Config <<");
                    System.out.println("\t\t # numCPUs: " + taskConfig.getNumCpus());
                    System.out.println("\t\t # diskMb: " + taskConfig.getDiskMb());
                    System.out.println("\t\t # ramMb: " + taskConfig.getRamMb());
                    System.out.println("\t\t # priority: " + taskConfig.getPriority());


                    System.out.println("\t >> Executor Config <<");
                    System.out.println("\t\t # name: " + exeConfig.getName());
                    System.out.println("\t\t # data: " + exeConfig.getData());
                }

            }
        } catch (TException e) {
            e.printStackTrace();
        }
    }

    public static void createJob(int jobID) throws Exception {
        JobKeyBean jobKey = new JobKeyBean("devel", "team-flash", "job-"+jobID);
        IdentityBean owner = new IdentityBean("team-flash");

        JobKeyBean jobKeyBean = new JobKeyBean("devel","team-flash","job-"+jobID);
        AuroraThriftClient client = AuroraThriftClient.getAuroraThriftClient(Constants.AURORA_SCHEDULER_PROP_FILE);
        JobDetailsResponseBean response = (JobDetailsResponseBean)client.getJobDetails(jobKeyBean);

        int alreadySubmittedJobs = response.getTasks().size();

        String containerID = jobID+"--"+String.valueOf(alreadySubmittedJobs+1);
        System.out.println(containerID);
        /*ProcessBean proc1 = new ProcessBean(
                "process_1", "docker run -it --volumes-from wpsgeog --volumes-from wrfinputsandy -v ~/wrfoutput:/wrfoutput --name flash-ncarwrfsandy bigwxwrf/ncar-wrf /wrf/run-wrf" , false);

        ProcessBean proc2 = new ProcessBean(
                "process_2", "docker run -it --rm=true -v ~/wrfoutput:/wrfoutput --name flash-postproc bigwxwrf/ncar-ncl'", false);*/



        ProcessBean proc1 = new ProcessBean(
                "process_1","docker run -i --volumes-from wpsgeog --volumes-from wrfinputsandy -v ~/wrfoutput:/wrfoutput --name flash-ncarwrfsandy-"+containerID+" bigwxwrf/ncar-wrf /wrf/run-wrf", false);

        ProcessBean proc2 = new ProcessBean(
                "process_2", "docker run -i --rm=true -v ~/wrfoutput:/wrfoutput --name flash-postproc bigwxwrf/ncar-ncl", false);

        Set<ProcessBean> processes = new HashSet<>();
        processes.add(proc1);
        processes.add(proc2);

        ResourceBean resources = new ResourceBean(0.5, 250, 250);

        TaskConfigBean taskConfig = new TaskConfigBean("team-flash-"+jobID, processes, resources);
        JobConfigBean jobConfig = new JobConfigBean(jobKey, owner, taskConfig, "example");

        String executorConfigJson = AuroraThriftClientUtil.getExecutorConfigJson(jobConfig);
        System.out.println(executorConfigJson);

        //AuroraThriftClient client = AuroraThriftClient.getAuroraThriftClient(Constants.AURORA_SCHEDULER_PROP_FILE);
        ResponseBean response1 = client.createJob(jobConfig);
        System.out.println(response1);
    }


    public static void initCreateJob(int jobID) {
        try {
            //properties.load(new FileInputStream("aurora-scheduler.properties"));


            properties.load(AuroraFlashClient.class.getClassLoader().getResourceAsStream(Constants.AURORA_SCHEDULER_PROP_FILE));
            String auroraHost = properties.getProperty(Constants.AURORA_SCHEDULER_HOST);
            String auroraPort = properties.getProperty(Constants.AURORA_SCHEDULER_PORT);
            //System.out.println(auroraHost+"/"+auroraPort);
            auroraSchedulerClient = AuroraSchedulerClientFactory.
                    createReadOnlySchedulerClient(MessageFormat.format(Constants.AURORA_SCHEDULER_CONNECTION_URL, auroraHost, auroraPort));

            // get jobs summary
//			AuroraClientSample.getJobSummary(auroraSchedulerClient);

            // create sample job
            AuroraFlashClient.createJob(jobID);
            AuroraThriftClient client = AuroraThriftClient.getAuroraThriftClient(Constants.AURORA_SCHEDULER_PROP_FILE);
            ResponseBean response = client.getPendingReasonForJob(new JobKeyBean("devel", "team-flash", "job-"+jobID));
            //JobKeyBean jobKeyBean = new JobKeyBean("devel","team-flash","job-"+jobID);
            //JobDetailsResponseBean response = (JobDetailsResponseBean)client.getJobDetails(jobKeyBean);

            //System.out.println(response);
            //System.out.println(response.getServerInfo());

            /*for(ScheduledTask task : response.getTasks())
            {
                System.out.println(task.getAssignedTask().getTaskId()+"/"+task.getStatus().toString());
                System.out.println(task.getAssignedTask().getSlaveHost());
            }*/

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

        public static List<JobBean> getTasks(String user,int jobid)
        {
            List<JobBean> listJobTask = new ArrayList<>();
            try
            {
                properties.load(AuroraFlashClient.class.getClassLoader().getResourceAsStream(Constants.AURORA_SCHEDULER_PROP_FILE));
                AuroraThriftClient client = AuroraThriftClient.getAuroraThriftClient(Constants.AURORA_SCHEDULER_PROP_FILE);

                String auroraHost = properties.getProperty(Constants.AURORA_SCHEDULER_HOST);
                String auroraPort = properties.getProperty(Constants.AURORA_SCHEDULER_PORT);
                auroraSchedulerClient = AuroraSchedulerClientFactory.
                        createReadOnlySchedulerClient(MessageFormat.format(Constants.AURORA_SCHEDULER_CONNECTION_URL, auroraHost, auroraPort));

                JobKeyBean jobKeyBean = new JobKeyBean("devel","team-flash","job-"+jobid);
                JobDetailsResponseBean response = (JobDetailsResponseBean)client.getJobDetails(jobKeyBean);

                //System.out.println(response);
                //System.out.println(response.getServerInfo());

                for(ScheduledTask task : response.getTasks())
                {
                    System.out.println(task.getAssignedTask().getTaskId()+"/"+task.getStatus().toString());
                    System.out.println(task.getAssignedTask().getSlaveHost());
                    JobBean job = new JobBean();
                    job.setUser(user);
                    job.setJobid(jobid);
                    job.setTaskid(task.getAssignedTask().getTaskId());
                    job.setTaskStatus(task.getStatus().toString());
                    job.setTaskServer(task.getAssignedTask().getSlaveHost());
                    listJobTask.add(job);

                }
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }

            return listJobTask;
        }
    }

