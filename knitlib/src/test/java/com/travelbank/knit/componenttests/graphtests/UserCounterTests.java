package com.travelbank.knit.componenttests.graphtests;

import com.travelbank.knit.components.graph.UserCounter;

import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.*;

/**
 * Created by bo_om on 3/12/2018.
 */

public class UserCounterTests {

    UserCounter userCounter;

    @Before
    public void setup(){
        this.userCounter = new UserCounter();
    }

    @Test
    public void countCycleTest(){
        this.userCounter.use();
        assertEquals(true,userCounter.isUsed());
        this.userCounter.release();
        assertEquals(false,userCounter.isUsed());
        assertEquals(0,userCounter.getCount());
    }

}
