package com.omerozer.knit.schedulers;

import java.util.concurrent.Callable;

/**
 * Created by omerozer on 2/21/18.
 */

public interface SchedulerInterface {

    <T>void  submit(Callable<T> callable);
    void submit(Runnable runnable);
    void start();
    void shutDown();
    <T>void setTargetAndConsumer(SchedulerInterface schedulerInterface,Consumer consumer);
    boolean isDone();
}
