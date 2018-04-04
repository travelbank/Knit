package com.omerozer.knit;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

/**
 * Created by omerozer on 4/4/18.
 */

public class EntityInstanceTests {

    EntityInstance<InternalModel> instance;

    @Mock
    InternalModel model;

    @Before
    public void setup(){
        MockitoAnnotations.initMocks(this);
        this.instance = new EntityInstance<>();
    }

    @Test
    public void assignValueTest(){
        this.instance.set(model);
        assertEquals(model,instance.get());
    }

    @Test
    public void isAvailableTest(){
        assertFalse(instance.isAvailable());
        instance.set(model);
        assertTrue(instance.isAvailable());
    }



}
