package com.omerozer.knit.schedulers;

import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Created by omerozer on 2/23/18.
 */

public class IOScheduler implements SchedulerInterface {



    private KnitIOThreadPool knitIOThreadPool;
    private KnitIOReceiverThread knitIOReceiverThread;
    private AtomicReference<SchedulerInterface> target;
    private AtomicReference<Consumer> resultConsumer;
    private AtomicBoolean isDone;

    public IOScheduler(KnitIOThreadPool ioThreadPool,KnitIOReceiverThread receiverThread){
        this.knitIOThreadPool = ioThreadPool;
        this.knitIOReceiverThread = receiverThread;
        this.target = new AtomicReference<>();
        this.resultConsumer = new AtomicReference<>();
        this.isDone = new AtomicBoolean(false);
    }



    @Override
    public<T> void submit(Callable<T> callable) {
        knitIOThreadPool.submit(createTask(callable));
    }

    @Override
    public void submit(Runnable runnable) {
        knitIOThreadPool.submit(runnable);
    }


    @Override
    public void start() {
        this.knitIOReceiverThread.start();
    }

    @Override
    public void shutDown() {
        this.knitIOReceiverThread.shutdown();
    }

    @Override
    public boolean isDone() {
        return isDone.get();
    }

    private<T> Runnable createTask(final Callable<T> callable){
        return new Runnable() {
            @Override
            public void run() {
                try {
                    final Object data = callable.call();
                    knitIOReceiverThread.post(new Runnable() {
                        @Override
                        public void run() {
                            target.get().start();
                            target.get().submit(new Runnable() {
                                @Override
                                public void run() {
                                    resultConsumer.get().consume(data);
                                    isDone.set(true);
                                }
                            });
                        }
                    });
                } catch (Exception e) {
                    resultConsumer.get().error(e);

                }
            }
        };
    }

    @Override
    public void setTargetAndConsumer(SchedulerInterface schedulerInterface,
            Consumer consumer) {
        this.target.set(schedulerInterface);
        this.resultConsumer.set(consumer);
    }
}
