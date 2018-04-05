package com.omerozer.knit.classloadertests;

import com.omerozer.knit.InternalModel;
import com.omerozer.knit.Knit;
import com.omerozer.knit.KnitInterface;
import com.omerozer.knit.KnitMock;
import com.omerozer.knit.TestModel;
import com.omerozer.knit.TestModel_Model;
import com.omerozer.knit.TestSchedulers;
import com.omerozer.knit.classloaders.KnitModelLoader;
import com.omerozer.knit.classloaders.KnitUtilsLoader;
import com.omerozer.knit.schedulers.SchedulerProvider;
import com.omerozer.knit.schedulers.Schedulers;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.when;

/**
 * Created by omerozer on 3/8/18.
 */

public class KnitModelLoaderTests {

    KnitModelLoader knitModelLoader;

    SchedulerProvider schedulerProvider;

    @Mock
    KnitInterface knit;

    @Before
    public void setup(){
        MockitoAnnotations.initMocks(this);
        this.schedulerProvider = new TestSchedulers();
        when(knit.getSchedulerProvider()).thenReturn(schedulerProvider);
        when(knit.getModelMap()).thenReturn(new KnitUtilsLoader().getModelMap(Knit.class));
        this.knitModelLoader = new KnitModelLoader(knit);
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
