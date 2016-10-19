package org.weather;

import static org.junit.Assert.*;
<<<<<<< HEAD
import org.junit.*;
=======
import org.junit.Assert;
import org.junit.Test;

>>>>>>> 9238f8234d2c59dfe68e56a930dce9b8fd5d0b84
/**
 * Created by girish on 10/19/16.
 */
public class TestRunWeatherForecast {
    @Test(expected = IllegalArgumentException.class)
<<<<<<< HEAD
    public void testArgumentNull() throws Exception {

=======
    public void testNull() throws Exception {
>>>>>>> 9238f8234d2c59dfe68e56a930dce9b8fd5d0b84
        RunWeatherForecast runWeatherForecast = new RunWeatherForecast();
        runWeatherForecast.runForecast(null);
    }

    @Test
    public void testOutput()
    {
        RunWeatherForecast runWeatherForecast = new RunWeatherForecast();
<<<<<<< HEAD
        String res = runWeatherForecast.runForecast("Spain");
        Assert.assertTrue(res!=null);
    }
=======
        String str = runWeatherForecast.runForecast("Spain");
        Assert.assertTrue(str!=null);
    }

>>>>>>> 9238f8234d2c59dfe68e56a930dce9b8fd5d0b84
}