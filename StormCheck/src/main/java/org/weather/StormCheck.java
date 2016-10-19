package org.weather;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

/**
 * Created by girish on 9/18/16.
 */
@Path("/verify")
public class StormCheck {

    public String isStormPresent = "{\"message\": \"Yes\"}";

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String checkStormExists(@QueryParam("value") boolean exists )
    {

        System.out.println(exists);
        if(!exists){
            isStormPresent = "{\"message\": \"No\"}";
        }

        return isStormPresent;
    }
}
