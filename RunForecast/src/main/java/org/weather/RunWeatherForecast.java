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
    public String runForecast(@QueryParam("location") String locationName)
    {
        if(locationName==null || locationName.length()==0 ){
            throw new IllegalArgumentException();
        }

        Location location = new Location();
        //location.setLocationID(locationid);
        location.setLocationName(locationName);
        location.setTemperature(10.0);
        location.setWindSpeed(12.5);
        Gson gson =  new Gson();
        //System.out.println("Before fun call");
        // LocationDAO locationDAO = new LocationDAO();
        // locationDAO.getWeatherInfo(locationid);
        return gson.toJson(location);
    }
}
