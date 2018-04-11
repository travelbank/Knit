package com.travelbank.knit.schedulers;

import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

/**
 * This scheduler is meant to handle all simple IO operations such as Rest calls and Database look ups.
 * It uses a {@link java.util.concurrent.ThreadPoolExecutor} provided by {@link KnitIOThreadPool} to
 * distribute tasks to other threads.
 */

public class IOScheduler implements SchedulerInterface {


    /**
     * Object that contains the thread pool that runs the assigned tasks.
     */
    private KnitIOThreadPool knitIOThreadPool;

    /**
     * {@link android.os.HandlerThread} that receives results from {@link KnitIOThreadPool}.
     */
    private KnitIOReceiverThread knitIOReceiverThread;

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

    public IOScheduler(KnitIOThreadPool ioThreadPool,KnitIOReceiverThread receiverThread){
        this.knitIOThreadPool = ioThreadPool;
        this.knitIOReceiverThread = receiverThread;
        this.target = new AtomicReference<>();
        this.resultConsumer = new AtomicReference<>();
        this.isDone = new AtomicBoolean(false);
    }



    /**
     * Passes {@link Callable} to the {@link KnitIOThreadPool}
     * @param callable {@link Callable} task that's being passed.
     * @param <T> Type of that the callable returns.
     */
    @Override
    public<T> void submit(Callable<T> callable) {
        knitIOThreadPool.submit(createTask(callable));
    }

    /**
     * Passes {@link Runnable} to the {@link KnitIOThreadPool}
     * @param runnable {@link Runnable} task that's being passed.
     */
    @Override
    public void submit(Runnable runnable) {
        knitIOThreadPool.submit(runnable);
    }

    /**
     * Starts the scheduler. Also registers in to the {@link com.travelbank.knit.schedulers.EvictorThread}.
     * {@link KnitIOReceiverThread} also starts running here.
     */
    @Override
    public void start() {
        this.knitIOReceiverThread.start();
    }

    /**
     * Shuts down the scheduler by killing the {@link KnitIOReceiverThread}
     */
    @Override
    public void shutDown() {

    }

    /**
     * If the task currently being executed is done ,it returns {@code true}. Otherwise {@code false}.
     * @return whether or not the task is done.
     */
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


    /**
     * Sets the target {@link SchedulerInterface} which will run the consume task.
     * @param schedulerInterface Target scheduler to run the consume task.
     * @param consumer {@link Consumer} object that contains the way consume will occur.
     * @param <T> Type that the consumer will receive.
     */
    @Override
    public <T>void setTargetAndConsumer(SchedulerInterface schedulerInterface,
            Consumer consumer) {
        this.target.set(schedulerInterface);
        this.resultConsumer.set(consumer);
    }
}
