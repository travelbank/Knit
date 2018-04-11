package com.travelbank.knit;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

/**
 * Created by omerozer on 3/19/18.
 */

public class MessageTrainTests {


    MessageTrain messageTrain;

    @Mock
    KnitMessage knitMessage;

    @Before
    public void setup(){
        MockitoAnnotations.initMocks(this);
        messageTrain = new MessageTrain();
    }

    @Test
    public void containsMessageTest(){
        messageTrain.putMessageForView(View1.class,knitMessage);
        assertTrue(messageTrain.hasMessage(View1.class));
    }

    @Test
    public void messageRemoveAfterGettingTest(){
        messageTrain.putMessageForView(View1.class,knitMessage);
        messageTrain.getMessageForView(View1.class);
        assertFalse(messageTrain.hasMessage(View1.class));
    }

}
