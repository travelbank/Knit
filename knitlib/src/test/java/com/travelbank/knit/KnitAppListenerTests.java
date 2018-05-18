package com.travelbank.knit;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import android.app.FragmentManager;
import android.content.res.Configuration;
import android.content.res.Resources;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

/**
 * Created by omerozer on 3/13/18.
 */

public class KnitAppListenerTests {

    KnitAppListener knitAppListener;

    @Mock
    Knit knit;

    @Mock
    InternalPresenter internalPresenter;

    @Captor
    ArgumentCaptor<Object> viewCaptor;

    @Mock
    TestActivity testActivity;

    @Mock
    Resources resources;

    Configuration configuration;

    @Mock
    FragmentManager fragmentManager;

    @Before
    public void setup(){
        MockitoAnnotations.initMocks(this);
        EntityInstance<InternalPresenter> presenterEntityInstance = new EntityInstance<>();
        presenterEntityInstance.set(internalPresenter);
        when(knit.findPresenterForView(any(Object.class))).thenReturn(presenterEntityInstance);
//        this.configuration = new Configuration();
//        this.configuration.orientation = 1;
        when(resources.getConfiguration()).thenReturn(configuration);
        when(testActivity.getResources()).thenReturn(resources);
        when(testActivity.getFragmentManager()).thenReturn(fragmentManager);
        when(testActivity.isFinishing()).thenReturn(true);
        this.knitAppListener = new KnitAppListener(knit);

    }

    @Test
    public void activityCreatedTest(){
        ArgumentCaptor<Boolean> booleanCaptor = ArgumentCaptor.forClass(Boolean.class);
        ArgumentCaptor<FragmentManager.FragmentLifecycleCallbacks> fragmentLifecycleCallbacksArgumentCaptor = ArgumentCaptor.forClass(FragmentManager.FragmentLifecycleCallbacks.class);
        knitAppListener.onActivityCreated(testActivity,null);
        verify(knit).initViewDependencies(viewCaptor.capture());
        assertEquals(testActivity.getClass(),viewCaptor.getValue().getClass());
//        verify(fragmentManager).registerFragmentLifecycleCallbacks(fragmentLifecycleCallbacksArgumentCaptor.capture(),booleanCaptor.capture());
//        assertEquals(knitAppListener.getFragmentCallbacks(),fragmentLifecycleCallbacksArgumentCaptor.getValue());
//        assertTrue(booleanCaptor.getValue());
    }

    @Test
    public void activityLifeCycleTests(){
        knitAppListener.onActivityStarted(testActivity);
        verify(internalPresenter).onViewStart();
        knitAppListener.onActivityResumed(testActivity);
        verify(internalPresenter).onViewResume();
        knitAppListener.onActivityPaused(testActivity);
        verify(internalPresenter).onViewPause();
        knitAppListener.onActivityStopped(testActivity);
        verify(internalPresenter).onViewStop();
    }

    @Test
    public void destroyComponentTests(){
        knitAppListener.onActivityDestroyed(testActivity);
        verify(knit).destroyComponent(viewCaptor.capture());
        assertEquals(testActivity,viewCaptor.getValue());
        Mockito.reset(testActivity);
        Mockito.reset(knit);
        EntityInstance<InternalPresenter> presenterEntityInstance = new EntityInstance<>();
        presenterEntityInstance.set(internalPresenter);
        when(knit.findPresenterForView(any(Object.class))).thenReturn(presenterEntityInstance);
        when(testActivity.isFinishing()).thenReturn(false);
        knitAppListener.onActivityDestroyed(testActivity);
        verify(knit,never()).destroyComponent(viewCaptor.capture());
    }

}

