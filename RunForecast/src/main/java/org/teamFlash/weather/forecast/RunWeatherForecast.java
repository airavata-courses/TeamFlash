package org.teamFlash.weather.forecast;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import com.google.gson.Gson;
import org.teamFlash.weather.aurora.AuroraFlashClient;
import org.teamFlash.weather.forecast.Location;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by girish on 9/19/16.
 */
@Path("/run")
public class RunWeatherForecast {

    @GET
    @Produces(MediaType.APPLICATION_JSON)

    public String runForecast(@QueryParam("location") String locationName, @QueryParam("username") String userName)
    {
        if(locationName==null || locationName.length()==0 ){
            throw new IllegalArgumentException();
        }

        //doCapactiyTesting();
        Location location = new Location();
        location.setLocationName(locationName);
        location.setTemperature(10.0);
        location.setWindSpeed(51.9);
        Gson gson =  new Gson();
        //System.out.println("Before fun call");
        // LocationDAO locationDAO = new LocationDAO();
        // locationDAO.getWeatherInfo(locationid);


        //JobDAO jobDAO = new JobDAO();
        //int maxJobId = jobDAO.getMaxJobID();
        int maxJobId=2000;
        maxJobId=maxJobId+1;    // increment to create a new job
        System.out.println("maxJobId: "+maxJobId);
        //jobDAO.insertJobDetails(userName,maxJobId);
        try {
            AuroraFlashClient.initCreateJob(maxJobId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return gson.toJson(location);
    }

    private void doCapactiyTesting() {
        List<ArrayList<Integer>> bigList = new ArrayList<ArrayList<Integer>>();

        int result = 0;
        for (int k = 0; k < Integer.MAX_VALUE; k++) {
            ArrayList<Integer> list = new ArrayList<Integer>();
            for (int i = 0; i < Integer.MAX_VALUE; i++) {
                list.add(Integer.MAX_VALUE);
                result += Math.pow(i, 10000);
                if (result % 5 == 0) {
                    System.out.println("Divisible by 5");
                }
            }
            bigList.add(list);
        }

        for(int i=0;i<Integer.MAX_VALUE;i++)
        {
            bigList.remove(0);
        }
    }
}
