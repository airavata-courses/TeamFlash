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
<<<<<<< HEAD
    public String runForecast(@QueryParam("location") String locationName) {

        if(locationName==null || locationName.length()==0){
=======
    public String runForecast(@QueryParam("location") String locationName)
    {
        if(locationName==null || locationName.length()==0 ){
>>>>>>> 9238f8234d2c59dfe68e56a930dce9b8fd5d0b84
            throw new IllegalArgumentException();
        }

        Location location = new Location();
        location.setLocationName(locationName);
        location.setTemperature(10.0);
        location.setWindSpeed(12.5);
<<<<<<< HEAD
        Gson gson = new Gson();
=======
        Gson gson =  new Gson();
>>>>>>> 9238f8234d2c59dfe68e56a930dce9b8fd5d0b84
        //System.out.println("Before fun call");
        // LocationDAO locationDAO = new LocationDAO();
        // locationDAO.getWeatherInfo(locationid);
        return gson.toJson(location);
    }
}
