package com.omerozer.knit;

import org.mockito.Mockito;

/**
 * Created by omerozer on 3/26/18.
 */

public class MockKnitMessage {
    static KnitMessage message;

    public static KnitMessage get(){
        if(message == null){
            message = Mockito.mock(KnitMessage.class);
        }
        Mockito.reset(message);
        return message;
    }
}
