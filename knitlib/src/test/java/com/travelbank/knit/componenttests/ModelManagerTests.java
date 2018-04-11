package com.travelbank.knit.componenttests;

import com.travelbank.knit.EntityInstance;
import com.travelbank.knit.InternalModel;
import com.travelbank.knit.InternalPresenter;
import com.travelbank.knit.KnitModel;
import com.travelbank.knit.NullValues;
import com.travelbank.knit.components.ComponentTag;
import com.travelbank.knit.components.ModelManager;
import com.travelbank.knit.components.graph.UsageGraph;
import com.travelbank.knit.schedulers.KnitSchedulers;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import static junit.framework.Assert.*;

/**
 * Created by bo_om on 3/12/2018.
 */

public class ModelManagerTests {

    ModelManager modelManager;

    ComponentTag tag1;

    ComponentTag tag2;

    @Mock
    KnitModel m1;

    @Mock
    KnitModel m2;

    @Mock
    EntityInstance<InternalModel> model1;

    @Mock
    EntityInstance<InternalModel> model2;

    @Mock
    InternalModel internalModel1;

    @Mock
    InternalModel internalModel2;

    @Mock
    UsageGraph usageGraph;

    String[] handledVals1 = new String[]{"v1","v2"};
    String[] handledVals2 = new String[]{"v3"};

    @Before
    public void setup(){
        MockitoAnnotations.initMocks(this);

        this.tag1 = ComponentTag.getNewTag();
        this.tag2 = ComponentTag.getNewTag();

        when(internalModel1.getHandledValues()).thenReturn(handledVals1);
        when(model1.get()).thenReturn(internalModel1);


        when(internalModel2.getHandledValues()).thenReturn(handledVals2);
        when(model2.get()).thenReturn(internalModel2);


        when(usageGraph.getModelWithTag(tag1)).thenReturn(model1);
        when(usageGraph.getModelWithTag(tag2)).thenReturn(model2);

        when(model1.get().getParent()).thenReturn(m1);
        when(model2.get().getParent()).thenReturn(m2);

        this.modelManager =  new ModelManager();

        this.modelManager.setUsageGraph(usageGraph);
    }

    @Test
    public void registerComponentTagTest(){
        modelManager.registerModelComponentTag(tag1);
        ArgumentCaptor<ModelManager> modelManagerArgumentCaptor = ArgumentCaptor.forClass(ModelManager.class);
        verify(m1).setModelManager(modelManagerArgumentCaptor.capture());
        verify(internalModel1).getHandledValues();
        assertEquals(modelManager,modelManagerArgumentCaptor.getValue());
    }

    @Test
    public void unregisterComponentTagTest(){
        modelManager.registerModelComponentTag(tag1);
        modelManager.unregisterComponentTag(tag1);
        verify(internalModel1   ,times(handledVals1.length)).getHandledValues();
    }

    @Test
    public void requestTests(){
        modelManager.registerModelComponentTag(tag1);
        ArgumentCaptor<String> dataArgumentCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<KnitSchedulers> runOnCaptor = ArgumentCaptor.forClass(KnitSchedulers.class);
        ArgumentCaptor<KnitSchedulers> consumeOnCaptor = ArgumentCaptor.forClass(KnitSchedulers.class);
        ArgumentCaptor<EntityInstance<InternalPresenter>> presenterArgumentCaptor = ArgumentCaptor.forClass(EntityInstance.class);
        ArgumentCaptor<String> paramCaptor = ArgumentCaptor.forClass(String.class);
        modelManager.request("v1", KnitSchedulers.IO,KnitSchedulers.MAIN, NullValues.NULL_PRESENTER,"param1");
        verify(internalModel1).request(dataArgumentCaptor.capture(),runOnCaptor.capture(),consumeOnCaptor.capture(),presenterArgumentCaptor.capture(),paramCaptor.capture());
        assertEquals("v1",dataArgumentCaptor.getValue());
        assertEquals(KnitSchedulers.IO,runOnCaptor.getValue());
        assertEquals(KnitSchedulers.MAIN,consumeOnCaptor.getValue());
        assertEquals(NullValues.NULL_PRESENTER,presenterArgumentCaptor.getValue());
        assertEquals("param1",paramCaptor.getValue());
    }

    @Test
    public void requestImmediatelyTest(){
        modelManager.registerModelComponentTag(tag1);
        ArgumentCaptor<String> dataArgumentCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Integer> paramCaptor = ArgumentCaptor.forClass(Integer.class);
        modelManager.requestImmediately("v2",4);
        verify(internalModel1).requestImmediately(dataArgumentCaptor.capture(),paramCaptor.capture());
        assertEquals("v2",dataArgumentCaptor.getValue());
        assertEquals(Integer.valueOf(4),paramCaptor.getValue());
    }

    @Test
    public void inputTest(){
        modelManager.registerModelComponentTag(tag1);
        ArgumentCaptor<String> dataArgumentCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Integer> paramCaptor = ArgumentCaptor.forClass(Integer.class);
        modelManager.input("v2",4);
        verify(internalModel1).input(dataArgumentCaptor.capture(),paramCaptor.capture());
        assertEquals("v2",dataArgumentCaptor.getValue());
        assertEquals(Integer.valueOf(4),paramCaptor.getValue());
    }

}
