package com.travelbank.knit.classloadertests;

import static junit.framework.Assert.assertEquals;

import com.travelbank.knit.InternalModel;
import com.travelbank.knit.InternalPresenter;
import com.travelbank.knit.KnitInterface;
import com.travelbank.knit.KnitMock;
import com.travelbank.knit.KnitNavigator;
import com.travelbank.knit.TestPresenter_Presenter;
import com.travelbank.knit.classloaders.KnitPresenterLoader;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

/**
 * Created by omerozer on 3/8/18.
 */

public class KnitPresenterLoaderTests {

    KnitInterface knit;

    KnitNavigator knitNavigator;

    InternalModel modelManager;

    KnitPresenterLoader knitPresenterLoader;

    @Mock
    Object accessor;

    @Before
    public void setup(){
        MockitoAnnotations.initMocks(this);
        this.knit = KnitMock.get();
        this.knitNavigator = knit.getNavigator();
        this.modelManager = knit.getModelManager();
        this.knitPresenterLoader = new KnitPresenterLoader(knit);
    }

    @Test
    public void loadPresenterTest(){
        InternalPresenter internalPresenter = knitPresenterLoader.loadPresenter(TestPresenter_Presenter.class);
        assertEquals(TestPresenter_Presenter.class,internalPresenter.getClass());
        TestPresenter_Presenter castPresenter = (TestPresenter_Presenter)internalPresenter;
        assertEquals(knit,castPresenter.getKnit());
        assertEquals(knitNavigator,castPresenter.getNavigator());
        assertEquals(modelManager,castPresenter.getModelManager());
    }

    @Test
    public void loadPresenterWithAccessorTest(){
        InternalPresenter internalPresenter = knitPresenterLoader.loadPresenter(TestPresenter_Presenter.class,accessor);
        assertEquals(TestPresenter_Presenter.class,internalPresenter.getClass());
        TestPresenter_Presenter castPresenter = (TestPresenter_Presenter)internalPresenter;
        assertEquals(knit,castPresenter.getKnit());
        assertEquals(knitNavigator,castPresenter.getNavigator());
        assertEquals(modelManager,castPresenter.getModelManager());
        assertEquals(accessor,castPresenter.getInteractor());
    }



}
