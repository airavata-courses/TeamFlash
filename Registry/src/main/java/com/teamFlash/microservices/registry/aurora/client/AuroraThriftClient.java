/**
 * Created by girish on 12/10/16.
 */
package com.teamFlash.microservices.registry.aurora.client;

import java.io.FileInputStream;
import java.text.MessageFormat;
import java.util.Properties;

//import bean.*;
import com.teamFlash.microservices.registry.aurora.bean.*;
import com.teamFlash.microservices.registry.aurora.utils.*;
import com.teamFlash.microservices.registry.aurora.client.sdk.*;
import com.teamFlash.microservices.registry.aurora.AuroraFlashClient;


import java.util.Set;
import java.util.HashSet;

public class AuroraThriftClient   {

    private static AuroraThriftClient thriftClient = null;
    /** The read only scheduler client. */
    private ReadOnlyScheduler.Client readOnlySchedulerClient = null;

    /** The aurora scheduler manager client. */
    private AuroraSchedulerManager.Client auroraSchedulerManagerClient = null;

    private AuroraThriftClient(){

    }

    private static Properties properties = new Properties();

    public static AuroraThriftClient getAuroraThriftClient(String auroraSchedulerPropFile) throws Exception
    {
        try {
            if (thriftClient == null) {
                thriftClient = new AuroraThriftClient();
                //properties.load(new FileInputStream("aurora-scheduler.properties"));
                properties.load(AuroraFlashClient.class.getClassLoader().getResourceAsStream(auroraSchedulerPropFile));
                String auroraHost = properties.getProperty(Constants.AURORA_SCHEDULER_HOST);
                String auroraPort = properties.getProperty(Constants.AURORA_SCHEDULER_PORT);
                String connectionUrl = MessageFormat.format(Constants.AURORA_SCHEDULER_CONNECTION_URL, auroraHost, auroraPort);

                thriftClient.readOnlySchedulerClient = AuroraSchedulerClientFactory.createReadOnlySchedulerClient(connectionUrl);
                thriftClient.auroraSchedulerManagerClient = AuroraSchedulerClientFactory.createSchedulerManagerClient(connectionUrl);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return thriftClient;
    }

    public ResponseBean createJob(JobConfigBean jobConfigBean) throws Exception {
        ResponseBean response = null;
        try {
            if(jobConfigBean != null) {
                JobConfiguration jobConfig = AuroraThriftClientUtil.getAuroraJobConfig(jobConfigBean);
                Response createJobResponse = this.auroraSchedulerManagerClient.createJob(jobConfig);
                response = AuroraThriftClientUtil.getResponseBean(createJobResponse, ResponseResultType.CREATE_JOB);
            }
        } catch(Exception ex) {

            ex.printStackTrace();
        }
        return response;
    }

    /**
     * Kill tasks.
     *
     * @param jobKeyBean the job key bean
     * @param instances the instances
     * @return the response bean
     * @throws Exception the exception
     */
    public ResponseBean killTasks(JobKeyBean jobKeyBean, Set<Integer> instances) throws Exception {
        ResponseBean response = null;
        try {
            if(jobKeyBean != null) {
                JobKey jobKey = AuroraThriftClientUtil.getAuroraJobKey(jobKeyBean);
                Response killTaskResponse = this.auroraSchedulerManagerClient.killTasks(jobKey, instances);
                response = AuroraThriftClientUtil.getResponseBean(killTaskResponse, ResponseResultType.KILL_TASKS);
            }
        } catch(Exception ex) {
            /*logger.error(ex.getMessage(), ex);
            throw ex;*/
            ex.printStackTrace();
        }
        return response;
    }

    /**
     * Gets the job list.
     *
     * @param ownerRole the owner role
     * @return the job list
     * @throws Exception the exception
     */
    public GetJobsResponseBean getJobList(String ownerRole) throws Exception {
        GetJobsResponseBean response = null;
        try {
            Response jobListResponse = this.readOnlySchedulerClient.getJobs(ownerRole);
            response = (GetJobsResponseBean) AuroraThriftClientUtil.getResponseBean(jobListResponse, ResponseResultType.GET_JOBS);
        } catch(Exception ex) {
            /*logger.error(ex.getMessage(), ex);
            throw ex;*/
            ex.printStackTrace();
        }
        return response;
    }

    /**
     * Gets the pending reason for job.
     *
     * @param jobKeyBean the job key bean
     * @return the pending reason for job
     * @throws Exception the exception
     */
    public PendingJobReasonBean getPendingReasonForJob(JobKeyBean jobKeyBean) throws Exception {
        PendingJobReasonBean response = null;
        try {
            JobKey jobKey = AuroraThriftClientUtil.getAuroraJobKey(jobKeyBean);
            Set<JobKey> jobKeySet = new HashSet<>();
            jobKeySet.add(jobKey);

            TaskQuery query = new TaskQuery();
            query.setJobKeys(jobKeySet);

            Response pendingReasonResponse = this.readOnlySchedulerClient.getPendingReason(query);
            response = (PendingJobReasonBean) AuroraThriftClientUtil.getResponseBean(pendingReasonResponse, ResponseResultType.GET_PENDING_JOB_REASON);
        } catch(Exception ex) {
            /*logger.error(ex.getMessage(), ex);
            throw ex;*/
            ex.printStackTrace();
        }
        return response;
    }

    /**
    /**
     * Gets the job details.
     *
     * @param jobKeyBean the job key bean
     * @return the job details
     * @throws Exception the exception
     */
    public ResponseBean getJobDetails(JobKeyBean jobKeyBean) throws Exception {
        JobDetailsResponseBean response = null;
        try {
            if(jobKeyBean != null) {
                JobKey jobKey = AuroraThriftClientUtil.getAuroraJobKey(jobKeyBean);
                Set<JobKey> jobKeySet = new HashSet<>();
                jobKeySet.add(jobKey);

                System.out.println(jobKeySet.size());

                TaskQuery query = new TaskQuery();
                query.setJobKeys(jobKeySet);

                System.out.println(query);

                Response jobDetailsResponse = this.readOnlySchedulerClient.getTasksStatus(query);
                response = (JobDetailsResponseBean) AuroraThriftClientUtil.getResponseBean(jobDetailsResponse, ResponseResultType.GET_JOB_DETAILS);
            }
        } catch(Exception ex) {
            /*logger.error(ex.getMessage(), ex);
            throw ex;*/
            ex.printStackTrace();
        }
        return response;
    }

}
