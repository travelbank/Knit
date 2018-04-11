package com.travelbank.knit.componenttests.graphtests;

import com.travelbank.knit.InternalModel;
import com.travelbank.knit.InternalPresenter;
import com.travelbank.knit.KnitInterface;
import com.travelbank.knit.KnitMessage;
import com.travelbank.knit.KnitMock;
import com.travelbank.knit.MockKnitMessage;
import com.travelbank.knit.ModelMapInterface;
import com.travelbank.knit.TestEnv;
import com.travelbank.knit.TestModel2_Model;
import com.travelbank.knit.TestModel_Model;
import com.travelbank.knit.TestPresenter_Presenter;
import com.travelbank.knit.TestSingleton_Model;
import com.travelbank.knit.UmbrellaModel_Model;
import com.travelbank.knit.View1;
import com.travelbank.knit.ViewToPresenterMapInterface;
import com.travelbank.knit.classloaders.KnitModelLoader;
import com.travelbank.knit.classloaders.KnitPresenterLoader;
import com.travelbank.knit.components.ComponentTag;
import com.travelbank.knit.components.ModelManager;
import com.travelbank.knit.components.graph.UsageGraph;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Created by Omer Ozer on 3/12/2018.
 */

public class UsageGraphTests {

    KnitInterface knit;

    UsageGraph usageGraph;

    ModelMapInterface modelMap;

    ViewToPresenterMapInterface viewToPresenterMap;

    ModelManager modelManager;

    KnitModelLoader modelLoader;

    KnitPresenterLoader presenterLoader;

    @Captor
    ArgumentCaptor<Class<? extends InternalModel>> internalModelCaptor;

    @Captor
    ArgumentCaptor<Class<? extends InternalPresenter>> internalPresenterCaptor;

    @Captor
    ArgumentCaptor<KnitMessage> messageCaptor;

    InternalModel testModel;

    InternalModel testModel2;

    InternalModel testUmbrellaModel;

    InternalModel testSingletonModel;

    InternalPresenter testPresenter;

    @Before
    public void setup(){
        MockitoAnnotations.initMocks(this);
        ComponentTag.reset();
        this.knit = KnitMock.get();
        this.modelManager = knit.getModelManager();
        this.modelLoader = knit.getModelLoader();
        this.presenterLoader = knit.getPresenterLoader();
        this.modelMap = knit.getModelMap();
        this.viewToPresenterMap = knit.getViewToPresenterMap();
        this.usageGraph = new UsageGraph(knit);
        this.testModel = modelLoader.loadModel(TestModel_Model.class);
        this.testModel2 = modelLoader.loadModel(TestModel2_Model.class);
        this.testUmbrellaModel = modelLoader.loadModel(UmbrellaModel_Model.class);
        this.testSingletonModel = modelLoader.loadModel(TestSingleton_Model.class);
        this.testPresenter = presenterLoader.loadPresenter(TestPresenter_Presenter.class);
    }

    @Test
    public void graphInitializationTest(){
        Short cur = ComponentTag.getNewTag().getTag();
        assertEquals(Short.valueOf("-32762"),cur);
        verify(modelMap).getAll();
        verify(viewToPresenterMap).getAllViews();
        verify(modelMap,times(TestEnv.totalModels())).getGeneratedValues(any(Class.class));
        verify(modelMap,times(TestEnv.totalModels())).getRequiredValues(any(Class.class));
        verify(viewToPresenterMap,times(TestEnv.totalPresentersWithDependencies())).getUpdatingValues(any(Class.class));
    }

    @Test
    public void startViewComponentTest(){
        usageGraph.startViewAndItsComponents(new View1());
        verify(modelManager,times(TestEnv.totalModels())).registerModelComponentTag(any(ComponentTag.class));
        verify(modelLoader,times(TestEnv.totalModels()+4)).loadModel(internalModelCaptor.capture());
        assertEquals(TestEnv.totalModels()+TestEnv.totalPresentersWithDependencies(),usageGraph.activeEntities().size());
        assertTrue(internalModelCaptor.getAllValues().contains(TestModel_Model.class));
        assertTrue(internalModelCaptor.getAllValues().contains(TestModel2_Model.class));
        assertTrue(internalModelCaptor.getAllValues().contains(UmbrellaModel_Model.class));
        assertTrue(internalModelCaptor.getAllValues().contains(TestSingleton_Model.class));
        verify(presenterLoader,times(TestEnv.totalPresentersWithDependencies()+1)).loadPresenter(internalPresenterCaptor.capture());
        assertEquals(TestPresenter_Presenter.class,internalPresenterCaptor.getValue());
        verify(testModel).onCreate();
        verify(testModel2).onCreate();
        verify(testUmbrellaModel).onCreate();
        verify(testSingletonModel).onCreate();
        verify(testPresenter).onCreate();
        verify(testPresenter).receiveMessage(messageCaptor.capture());
        assertEquals(MockKnitMessage.get(),messageCaptor.getValue());

    }

    @Test
    public void destroyViewComponentsTest(){
        View1 view1 = new View1();
        usageGraph.startViewAndItsComponents(view1);
        usageGraph.stopViewAndItsComponents(view1);
        verify(modelManager,times(TestEnv.nonSingleTonModels())).unregisterComponentTag(any(ComponentTag.class));
        verify(testModel).onDestroy();
        verify(testModel2).onDestroy();
        verify(testUmbrellaModel).onDestroy();
        verify(testSingletonModel,never()).onDestroy();
        verify(testPresenter).onDestroy();
    }

    @Test
    public void attachViewToComponentTest(){
        View1 view1 = new View1();
        ArgumentCaptor<View1> view1ArgumentCaptor = ArgumentCaptor.forClass(View1.class);
        usageGraph.startViewAndItsComponents(view1);
        Mockito.reset(testPresenter);
        usageGraph.attachViewToComponent(view1);
        verify(testPresenter).onViewApplied(view1ArgumentCaptor.capture());
        assertEquals(view1,view1ArgumentCaptor.getValue());
    }

    @Test
    public void releaseViewFromComponentTest(){
        View1 view1 = new View1();
        usageGraph.startViewAndItsComponents(view1);
        usageGraph.releaseViewFromComponent(view1);
        verify(testPresenter).onCurrentViewReleased();
    }

}
