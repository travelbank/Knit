package com.travelbank.knit.schedulertests;

import static junit.framework.Assert.assertEquals;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.travelbank.knit.TestKnitIOReceiverThread;
import com.travelbank.knit.TestKnitIOThreadPool;
import com.travelbank.knit.TestSchedulers;
import com.travelbank.knit.schedulers.Consumer;
import com.travelbank.knit.schedulers.IOScheduler;
import com.travelbank.knit.schedulers.KnitIOReceiverThread;
import com.travelbank.knit.schedulers.KnitIOThreadPool;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.concurrent.Callable;

/**
 * Created by omerozer on 3/13/18.
 */

public class IOSchedulerTests {

    IOScheduler ioScheduler;

    @Mock
    KnitIOThreadPool knitIOThreadPool;

    @Mock
    Consumer<String> consumer;

    @Mock
    Runnable runnable;

    @Mock
    Callable<String> callable;

    Throwable testException = new RuntimeException("runtime test exception");

    @Before
    public void setup() throws Exception{
        MockitoAnnotations.initMocks(this);
        this.ioScheduler = new IOScheduler(new TestKnitIOThreadPool(),new TestKnitIOReceiverThread());
        this.ioScheduler.setTargetAndConsumer(new TestSchedulers().immediate(),consumer);
        when(callable.call()).thenReturn("data");
    }

    private void setMock(){
        this.ioScheduler = new IOScheduler(knitIOThreadPool,new KnitIOReceiverThread());
    }

    @Test
    public void submitRunnableTest(){
        setMock();
        ArgumentCaptor<Runnable> runnableArgumentCaptor = ArgumentCaptor.forClass(Runnable.class);
        ioScheduler.submit(runnable);
        verify(knitIOThreadPool).submit(runnableArgumentCaptor.capture());
        assertEquals(runnable,runnableArgumentCaptor.getValue());
    }

    @Test
    public void submitCallableTest() throws Exception{
        ArgumentCaptor<String> dataArgumentCaptor = ArgumentCaptor.forClass(String.class);
        ioScheduler.submit(callable);
        verify(callable).call();
        verify(consumer).consume(dataArgumentCaptor.capture());
        assertEquals("data",dataArgumentCaptor.getValue());
        Mockito.reset(callable);
        when(callable.call()).thenThrow(testException);
        ArgumentCaptor<Throwable> exceptionCaptor = ArgumentCaptor.forClass(Throwable.class);
        ioScheduler.submit(callable);
        verify(callable).call();
        verify(consumer).error(exceptionCaptor.capture());
        assertEquals(testException,exceptionCaptor.getValue());
    }


}
