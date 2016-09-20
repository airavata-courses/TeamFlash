package org.weather;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import com.google.gson.Gson;

/**
 * Created by girish on 9/19/16.
 */
@Path("/run")
public class RunWeatherForecast {
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{locationid}")
    public String runForecast(@PathParam("locationid") int locationid)
    {
        Location location = new Location();
        location.setLocationID(locationid);
        location.setLocationName("Atlantic");
        location.setTemperature(10.0);
        location.setWindSpeed(12.5);
        Gson gson =  new Gson();

        return gson.toJson(location);
    }
}
