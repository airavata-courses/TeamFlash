package org.weather;

import static org.junit.Assert.*;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by girish on 10/19/16.
 */
public class TestRunWeatherForecast {
    @Test(expected = IllegalArgumentException.class)
    public void testNull() throws Exception {
        RunWeatherForecast runWeatherForecast = new RunWeatherForecast();
        runWeatherForecast.runForecast(null);
    }

    @Test
    public void testOutput()
    {
        RunWeatherForecast runWeatherForecast = new RunWeatherForecast();
        String str = runWeatherForecast.runForecast("Spain");
        Assert.assertTrue(str!=null);
    }

}