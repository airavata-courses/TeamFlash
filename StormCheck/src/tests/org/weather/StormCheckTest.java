package org.weather;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by girish on 10/19/16.
 */
public class StormCheckTest {
    @Test
    public void checkStormExists() throws Exception {

        StormCheck stormcheck = new StormCheck();
        String res = stormcheck.checkStormExists("no");
        assertTrue(res!=null);
    }
    
}