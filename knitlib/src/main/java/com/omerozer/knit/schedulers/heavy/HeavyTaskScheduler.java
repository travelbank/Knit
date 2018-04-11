package com.omerozer.knit.schedulers.heavy;

import static com.omerozer.knit.schedulers.heavy.HeavyThread.getPriority;
import static com.omerozer.knit.schedulers.heavy.HeavyThread.handleTask;

import android.content.Context;
import android.content.Intent;

import com.omerozer.knit.Knit;
import com.omerozer.knit.schedulers.Consumer;
import com.omerozer.knit.schedulers.SchedulerInterface;

import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.concurrent.Callable;

/**
 *
 * This is a {@link SchedulerInterface} that is meant to handle heavy weight tasks such as
 * downloading large files/images. Tasks will be run on an Android {@link android.app.IntentService}.
 * Knit creates a pool ({@link PriorityQueue}) of 4 {@link HeavyThread} s which are {@link android.app.IntentService}s.
 * The pool prioritizes these bases on the number of current running tasks. The next task will always
 * run on the {@link HeavyThread} with the least number of tasks running.
 *
 * @see HeavyThread
 * @see HThread1
 * @see HThread2
 * @see HThread3
 * @see HThread4
 *
 * @author Omer Ozer
 */

public class HeavyTaskScheduler implements SchedulerInterface {

    /**
     * Thread Id - 1
     */
    static final String HEAVY_THREAD_NAME1 = "knit_heavy_thread1";

    /**
     * Thread Id - 2
     */
    static final String HEAVY_THREAD_NAME2 = "knit_heavy_thread2";

    /**
     * Thread Id - 3
     */
    static final String HEAVY_THREAD_NAME3 = "knit_heavy_thread3";

    /**
     * Thread Id - 4
     */
    static final String HEAVY_THREAD_NAME4 = "knit_heavy_thread4";

    /**
     * Synchronization lock for {@link AvailableThread} {@link PriorityQueue}.
     */
    private static final Object queueLock;

    /**
     * {@link PriorityQueue} for all {@link AvailableThread}s. Priority is determined by the number of active running tasks. {@link PriorityQueue#poll()} will always return the thread with the lowers
     * number of active running tasks.
     */
    private static final PriorityQueue<AvailableThread> avaiableThreads;

    /**
     * Initialize all threads in the pool
     */
    static {
        queueLock = new Object();
        avaiableThreads = new PriorityQueue<>(4,getThreadPriorityComparator());
        avaiableThreads.offer(new AvailableThread(HThread1.class, HEAVY_THREAD_NAME1));
        avaiableThreads.offer(new AvailableThread(HThread2.class, HEAVY_THREAD_NAME2));
        avaiableThreads.offer(new AvailableThread(HThread3.class, HEAVY_THREAD_NAME3));
        avaiableThreads.offer(new AvailableThread(HThread4.class, HEAVY_THREAD_NAME4));
    }

    /**
     * Wrapper class that holds a thread id and the type of the thread.
     */
    private static class AvailableThread{
        public Class<? extends HeavyThread> clazz;
        public String threadId;

        AvailableThread(Class<? extends HeavyThread> clazz, String threadId) {
            this.clazz = clazz;
            this.threadId = threadId;
        }

    }

    /**
     * Getter for {@link AvailableThread} {@link Comparator}
     * @return Comparator that compares how busy {@link AvailableThread}s are..
     */
    private static Comparator<AvailableThread> getThreadPriorityComparator(){
        return new Comparator<AvailableThread>() {
            @Override
            public int compare(AvailableThread o1,
                    AvailableThread o2) {
                return (getPriority(o1.threadId) < getPriority(o2.threadId)) ? -1 : ((getPriority(o1.threadId)
                        == getPriority(o2.threadId)) ? 0 : 1);
            }
        };
    }

    /**
     * {@link SchedulerInterface} that will be running the consume task when the task here is complete.
     */
    private SchedulerInterface target;

    /**
     * {@link Consumer} instance that will handle how consume is done on the target {@link SchedulerInterface}
     */
    private Consumer consumer;

    /**
     * {@link Context} app level context.
     */
    private Context context;

    public HeavyTaskScheduler() {
        this.context = Knit.getInstance().getApp();
    }

    /**
     * Creates a {@link TaskPackage} to be sent to the {@link HeavyThread}.
     * @param callable {@link Callable} task that's being passed.
     * @param <T> Type of that the callable returns.
     */
    @Override
    public <T> void submit(Callable<T> callable) {
        TaskPackage taskPackage = new TaskPackage();
        taskPackage.setCallable(callable);
        taskPackage.setCurrent(this);
        taskPackage.setTarget(target);
        taskPackage.setConsumer(consumer);
        AvailableThread availableThread = getLeastBusyThread();
        handleTask(availableThread.threadId, taskPackage, context, availableThread.clazz);
    }

    /**
     * Creates a {@link TaskPackage} to be sent to the {@link HeavyThread}.
     * @param runnable {@link Runnable} task that's being passed.
     */
    @Override
    public void submit(Runnable runnable) {
        TaskPackage taskPackage = new TaskPackage();
        taskPackage.setRunnable(runnable);
        taskPackage.setTarget(target);
        taskPackage.setConsumer(consumer);
        AvailableThread availableThread =  getLeastBusyThread();
        handleTask(availableThread.threadId, taskPackage, context, availableThread.clazz);
    }

    /**
     * Returns the least busy thread. It is determined by the {@link PriorityQueue} that compares their total number of active running tasks.
     * @return the least busy thread that is wrapper in a {@link AvailableThread}.
     */
    private AvailableThread getLeastBusyThread(){
        synchronized (queueLock) {
            AvailableThread availableThread = avaiableThreads.poll();
            avaiableThreads.offer(availableThread);
            return availableThread;
        }
    }

    /**
     * Starts the scheduler. Might need to do stuff here if creating a custom scheduler.
     */
    @Override
    public void start() {
    }

    /**
     * Shuts down the Scheduler.
     */
    @Override
    public void shutDown() {
        if(getPriority(HEAVY_THREAD_NAME1)==0){context.stopService(new Intent(context, HThread1.class));}
        if(getPriority(HEAVY_THREAD_NAME2)==0){context.stopService(new Intent(context, HThread2.class));}
        if(getPriority(HEAVY_THREAD_NAME3)==0){context.stopService(new Intent(context, HThread3.class));}
        if(getPriority(HEAVY_THREAD_NAME4)==0){context.stopService(new Intent(context, HThread4.class));}
    }

    /**
     * Sets the target {@link SchedulerInterface} which will run the consume task.
     * @param schedulerInterface Target scheduler to run the consume task.
     * @param consumer {@link Consumer} object that contains the way consume will occur.
     * @param <T> Type that the consumer will receive.
     */
    @Override
    public <T> void setTargetAndConsumer(SchedulerInterface schedulerInterface, Consumer consumer) {
        this.target = schedulerInterface;
        this.consumer = consumer;
    }

    /**
     * If there are no {@link HeavyThread} running actively. This will return {@code true}.
     * @return Whether this scheduler has any tasks running or not.
     */
    @Override
    public boolean isDone() {
        return  getPriority(HEAVY_THREAD_NAME1) == 0 &&
                getPriority(HEAVY_THREAD_NAME2) == 0 &&
                getPriority(HEAVY_THREAD_NAME3) == 0 &&
                getPriority(HEAVY_THREAD_NAME4) == 0;
    }
}
