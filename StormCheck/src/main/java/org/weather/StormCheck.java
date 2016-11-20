package org.weather;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.*;
/**
 * Created by girish on 9/18/16.
 */
@Path("/verify")
public class StormCheck {

    public String isStormPresent = "{\"message\": \"Yes\"}";

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String checkStormExists(@QueryParam("value") String exists )
    {

        System.out.println(exists);
        long start = System.currentTimeMillis();
        //doCapactiyTesting();

        if(exists.equalsIgnoreCase("no")){
            isStormPresent = "{\"message\": \"No\"}";
        }
        long end = System.currentTimeMillis();

        System.out.println("Time: "+(end-start));

        return isStormPresent;
    }

    private void doCapactiyTesting()
    {
        ArrayList<Integer> list = new ArrayList<Integer>();
        int result=0;
        for(int i=0;i<200000;i++)
        {
            list.add(i);
            result+=Math.pow(i,10000);
            if(result%5==0)
            {
                System.out.println("Divisible by 5");
            }
        }

        for(int i=0;i<200000;i++)
        {
            list.remove(0);
        }
    }
}
