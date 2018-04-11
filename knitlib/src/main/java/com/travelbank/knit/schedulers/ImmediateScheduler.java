package com.travelbank.knit.schedulers;

import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

/**
 * This scheduler runs the tasks directly on the thread that it was fired from. Provides no async
 * operations. The main use case is for Unit tests.
 *
 * @author Omer Ozer
 */

public class ImmediateScheduler implements SchedulerInterface {

    /**
     *  {@link AtomicReference}  for {@link SchedulerInterface} that will be handling the consume task.
     */
    private AtomicReference<SchedulerInterface> target;

    /**
     * {@link AtomicReference} for {@link Consumer} that will be handling result of the task.
     */
    private AtomicReference<Consumer> resultConsumer;

    /**
     * {@link AtomicReference} for whether this scheduler task is done
     */
    private AtomicBoolean isDone;


    public ImmediateScheduler(){
        this.target = new AtomicReference<>();
        this.resultConsumer = new AtomicReference<>();
        this.isDone = new AtomicBoolean(false);
    }

    /**
     * Handles the callable immediately and passes the result to {@link Consumer}
     * @param callable {@link Callable} task that's being passed.
     * @param <T> Type of that the callable returns.
     */
    @Override
    public <T> void submit(Callable<T> callable) {
        try {
            final T data = callable.call();
            if(target.get()!=null){
                target.get().start();
                target.get().submit(new Runnable() {
                    @Override
                    public void run() {
                        resultConsumer.get().consume(data);
                    }
                });

            }
            isDone.set(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Handles {@link Runnable} immediately.
     * @param runnable {@link Runnable} task that's being passed.
     */
    @Override
    public void submit(Runnable runnable) {
        runnable.run();
        this.isDone.set(true);
    }

    /**
     * Starts the scheduler. Also registers in to the {@link com.travelbank.knit.schedulers.EvictorThread}
     */
    @Override
    public void start() {

    }


    /**
     * This does nothing as this Scheduler doesn't hold any other dependencies.
     */
    @Override
    public void shutDown() {

    }


    /**
     * Sets the target {@link SchedulerInterface} which will run the consume task.
     * @param schedulerInterface Target scheduler to run the consume task.
     * @param consumer {@link Consumer} object that contains the way consume will occur.
     * @param <T> Type that the consumer will receive.
     */
    @Override
    public <T> void setTargetAndConsumer(SchedulerInterface schedulerInterface, Consumer consumer) {
        this.target.set(schedulerInterface);
        this.resultConsumer.set(consumer);
    }

    /**
     * If the task currently being executed is done ,it returns {@code true}. Otherwise {@code false}.
     * @return whether or not the task is done.
     */
    @Override
    public boolean isDone() {
        return isDone.get();
    }
}
