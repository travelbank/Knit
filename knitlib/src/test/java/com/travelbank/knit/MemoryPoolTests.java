package com.travelbank.knit;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

/**
 * Created by omerozer on 3/20/18.
 */

public class MemoryPoolTests {

    MessagePool messagePool;

    @Mock
    KnitMessage message;

    @Before
    public void setup(){
        MockitoAnnotations.initMocks(this);
        messagePool = new MessagePool();
    }

    @Test
    public void returnNewIfEmptyTest(){
        assertEquals(0,messagePool.availableObjects());
        assertNotNull(messagePool.getObject());
    }

    @Test
    public void stopPoolingAtMaxObjectsTest(){
        int max = messagePool.getMaxPoolSize();
        for(int i = 0; i < max; i++){
            messagePool.pool(new KnitMessage());
        }
       //This one shouldn't get added to the pool
       messagePool.pool(new KnitMessage());
       assertEquals(max,messagePool.availableObjects());
    }

    @Test
    public void poolClearsTheObjectTest(){
        messagePool.pool(message);
        verify(message).recycle();
    }
}
