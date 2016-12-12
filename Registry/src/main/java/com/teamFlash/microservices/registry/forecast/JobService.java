package com.teamFlash.microservices.registry.forecast;

import com.google.gson.Gson;
import com.teamFlash.microservices.registry.aurora.AuroraFlashClient;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by girish on 9/19/16.
 */

public class JobService  {

    

    public String createJOB(String locationName, String userName)
    {
        if(locationName==null || locationName.length()==0 ){
            throw new IllegalArgumentException();
        }

        //Gson gson =  new Gson();
        JobDAO jobDAO = new JobDAO();
        int maxJobId = jobDAO.getMaxJobID();
        //int maxJobId=2000;
        maxJobId=maxJobId+1;    // increment to create a new job
        System.out.println("maxJobId: "+maxJobId);
        jobDAO.insertJobDetails(userName,maxJobId);
        try {
            AuroraFlashClient.initCreateJob(maxJobId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "Success";
    }

    
}
