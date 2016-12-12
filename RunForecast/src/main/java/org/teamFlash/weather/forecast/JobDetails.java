package org.teamFlash.weather.forecast;

import org.teamFlash.weather.aurora.AuroraFlashClient;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;
import com.google.gson.Gson;
/**
 * Created by girish on 12/11/16.
 */

@Path("/getJobs")
public class JobDetails
{
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getJobsTasks(@QueryParam("username") String userName,@QueryParam("id") String id)
    {
        JobDAO jobDAO = new JobDAO();
        Gson gson = new Gson();
        List<Integer> jobs = jobDAO.getJobs(userName);
        List<List<JobBean>> result = new ArrayList<List<JobBean>>();
        System.out.println(jobs.size());
        for(int i=0;i<jobs.size();i++)
        {
            int jobid = jobs.get(i);
            List<JobBean> taskList = AuroraFlashClient.getTasks(userName,jobid);
            System.out.println("taskList.size(): "+taskList.size());
            if(taskList.size()>0) {
                result.add(taskList);
            }
        }

        String response =  gson.toJson(result);
        return response;
    }

}
