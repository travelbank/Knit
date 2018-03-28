package com.omerozer.knit.schedulertests;

import static junit.framework.Assert.assertEquals;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.omerozer.knit.TestMainHandlerSupplier;
import com.omerozer.knit.TestSchedulers;
import com.omerozer.knit.schedulers.AndroidMainThreadScheduler;
import com.omerozer.knit.schedulers.Consumer;
import com.omerozer.knit.schedulers.MainHandlerSupplierInterface;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.concurrent.Callable;

/**
 * Created by omerozer on 3/27/18.
 */

public class AndroidMainThreadSchedulerTests {

    AndroidMainThreadScheduler androidMainThreadScheduler;

    MainHandlerSupplierInterface mainHandlerSupplier;

    @Mock
    Consumer<String> consumer;

    @Mock
    Callable<String> callable;

    @Before
    public void setup(){
        MockitoAnnotations.initMocks(this);
        mainHandlerSupplier = new TestMainHandlerSupplier();
        androidMainThreadScheduler = new AndroidMainThreadScheduler(mainHandlerSupplier);
    }

    @Test
    public void submitCallableTest() throws Exception{
        when(callable.call()).thenReturn("test");
        androidMainThreadScheduler.start();
        androidMainThreadScheduler.setTargetAndConsumer(new TestSchedulers().immediate(),consumer);
        androidMainThreadScheduler.submit(callable);
        ArgumentCaptor<String> consumedDataCaptor = ArgumentCaptor.forClass(String.class);
        verify(consumer).consume(consumedDataCaptor.capture());
        assertEquals("test",consumedDataCaptor.getValue());
    }

    @Test
    public void submitRunnableTest(){
        Runnable runnable = Mockito.mock(Runnable.class);
        androidMainThreadScheduler.start();
        androidMainThreadScheduler.submit(runnable);
        verify(runnable,times(1)).run();
    }


}
