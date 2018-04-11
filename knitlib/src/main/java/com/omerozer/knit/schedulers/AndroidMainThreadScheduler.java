package com.omerozer.knit.schedulers;

import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

/**
 *
 * This scheduler runs all tasks on the Android main thread through a {@link android.os.Handler}
 * provided by the {@link MainHandlerSupplier}
 *
 * @author Omer Ozer
 */

public class AndroidMainThreadScheduler implements SchedulerInterface {


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

    /**
     * {@link MainHandlerSupplier} that supplies the handler to run tasks on the main/UI thread.
     */
    private MainHandlerSupplierInterface mainHandlerSupplier;

    public AndroidMainThreadScheduler(MainHandlerSupplierInterface mainHandlerSupplier){
        this.target = new AtomicReference<>();
        this.resultConsumer = new AtomicReference<>();
        this.isDone = new AtomicBoolean(false);
        this.mainHandlerSupplier = mainHandlerSupplier;
    }

    /**
     * Passes {@link Callable} to the {@link MainHandlerSupplier}
     * @param callable {@link Callable} task that's being passed.
     * @param <T> Type of that the callable returns.
     */
    @Override
    public <T> void submit(final Callable<T> callable) {
        mainHandlerSupplier.post(new Runnable() {
            @Override
            public void run() {
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
        });
    }

    /**
     * Passes {@link Runnable} to the {@link MainHandlerSupplier}
     * @param runnable {@link Runnable} task that's being passed.
     */
    @Override
    public void submit(Runnable runnable) {
        mainHandlerSupplier.post(runnable);
        isDone.set(true);
    }

    /**
     * Starts the scheduler. Might need to do stuff here if creating a custom scheduler.
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
