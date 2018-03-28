package com.omerozer.knit.classloadertests;

import com.omerozer.knit.InternalModel;
import com.omerozer.knit.TestModel;
import com.omerozer.knit.TestModel_Model;
import com.omerozer.knit.TestSchedulers;
import com.omerozer.knit.classloaders.KnitModelLoader;
import com.omerozer.knit.schedulers.SchedulerProvider;
import com.omerozer.knit.schedulers.Schedulers;

import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

/**
 * Created by omerozer on 3/8/18.
 */

public class KnitModelLoaderTests {

    KnitModelLoader knitModelLoader;

    SchedulerProvider schedulerProvider;

    @Before
    public void setup(){
        this.schedulerProvider = new TestSchedulers();
        this.knitModelLoader = new KnitModelLoader(schedulerProvider);
    }

    @Test
    public void getModelForModelClassTest(){
        Class<?> internalModelClass = knitModelLoader.getModelForModel(TestModel.class);
        assertEquals(TestModel_Model.class,internalModelClass);
    }

    @Test
    public void getModelTest(){
        InternalModel model = knitModelLoader.loadModel(TestModel_Model.class);
        assertEquals(TestModel_Model.class,model.getClass());
        SchedulerProvider passedScheduler = ((TestModel_Model)model).getSchedulerProvider();
        assertEquals(schedulerProvider,passedScheduler);
    }


}
