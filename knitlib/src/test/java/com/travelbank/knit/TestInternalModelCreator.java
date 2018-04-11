package com.travelbank.knit;

import org.mockito.Mockito;

/**
 * Created by Omer Ozer on 3/12/2018.
 */

public class TestInternalModelCreator {

    public static InternalModel create(){
        return Mockito.mock(InternalModel.class);
    }

}
