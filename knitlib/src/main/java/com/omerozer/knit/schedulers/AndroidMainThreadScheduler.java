package com.omerozer.knit.schedulers;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Created by omerozer on 2/26/18.
 */

public class AndroidMainThreadScheduler implements SchedulerInterface {


    private AtomicReference<SchedulerInterface> target;
    private AtomicReference<Consumer> resultConsumer;
    private AtomicBoolean isDone;
    private MainHandlerSupplierInterface mainHandlerSupplier;

    public AndroidMainThreadScheduler(MainHandlerSupplierInterface mainHandlerSupplier){
        this.target = new AtomicReference<>();
        this.resultConsumer = new AtomicReference<>();
        this.isDone = new AtomicBoolean(false);
        this.mainHandlerSupplier = mainHandlerSupplier;
    }


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

    @Override
    public void submit(Runnable runnable) {
        mainHandlerSupplier.post(runnable);
        isDone.set(true);
    }

    @Override
    public void start() {
        EVICTOR_THREAD.registerScheduler(this);
    }

    @Override
    public void shutDown() {

    }

    @Override
    public <T> void setTargetAndConsumer(SchedulerInterface schedulerInterface, Consumer consumer) {
        this.target.set(schedulerInterface);
        this.resultConsumer.set(consumer);
    }

    @Override
    public boolean isDone() {
        return isDone.get();
    }
}
