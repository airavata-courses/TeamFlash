package org.weather;

import org.junit.Assert;
import org.junit.Test;
import org.weather.StormCheck;
import static org.junit.Assert.*;

/**
 * Created by girish on 10/19/16.
 */
public class TestStormCheck {
    @Test
    public void checkStormExists() throws Exception {

        StormCheck stormcheck = new StormCheck();
        String res = stormcheck.checkStormExists(false);
        Assert.assertTrue(res!=null);

        res = stormcheck.checkStormExists(true);
        Assert.assertTrue(res!=null);

    }
    
}