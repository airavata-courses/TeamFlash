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
        doCapactiyTesting();

        if(exists.equalsIgnoreCase("no")){
            isStormPresent = "{\"message\": \"No\"}";
        }
        long end = System.currentTimeMillis();

        System.out.println("Time: "+(end-start));

        return isStormPresent;
    }

    private void doCapactiyTesting()
    {
        List<ArrayList<Integer>> bigList = new ArrayList<ArrayList<Integer>>();

        int result = 0;
        for (int k = 0; k < 100; k++) {
            ArrayList<Integer> list = new ArrayList<Integer>();
            for (int i = 0; i < 100; i++) {
                list.add(Integer.MAX_VALUE);
                result += Math.pow(i, 10000);
                if (result % 5 == 0) {
                    System.out.println("Divisible by 5");
                }
            }
            bigList.add(list);
        }

        /*for(int i=0;i<10;i++)
        {
            bigList.remove(0);
	}*/
    }
}
