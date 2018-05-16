package com.travelbank.knitprocessor;

import javax.annotation.processing.ProcessingEnvironment;

/**
 * Created by omerozer on 5/16/18.
 */

public abstract class KnitBaseProcessor {

    private ProcessingEnvironment env;

    protected KnitBaseProcessor(ProcessingEnvironment env){
        this.env = env;
    }

    protected ProcessingEnvironment getEnv(){
        return env;
    }

}
