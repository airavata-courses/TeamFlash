package com.teamFlash.microservices.registry.forecast;

import com.teamFlash.microservices.registry.aurora.AuroraFlashClient;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.Gson;
/**
 * Created by girish on 12/11/16.
 */


public class JobDetails
{
 
    public String getJobsTasks(String userName, String id)
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
