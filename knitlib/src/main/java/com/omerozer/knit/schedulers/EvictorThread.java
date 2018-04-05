package com.omerozer.knit.schedulers;


import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 *
 * This is the thread responsible for eviction of the schedulers. It will iterate over all registered
 * {@link Schedulers} 10x / second and shut down the ones that are done.
 *
 * This thread starts running the moment a {@link Schedulers} is fired and will be killed 20seconds after.
 * Each {@link Schedulers} started within that 20 seconds will push the ending further without the need for
 * restarting of the thread. If there are no {@link Schedulers} activity for 20 seconds straight, this thread will die.
 *
 * @author Omer Ozer
 */

public class EvictorThread implements Runnable {

    private static final long SLEEP_DELAY = 100L;
    private static final long STAY_ALIVE = 20L * 1000L;

    private volatile boolean isRunning;
    private AtomicLong evictBase;
    private Thread thread;
    private ReadWriteLock entryLock;
    private Set<SchedulerInterface> schedulers;

    EvictorThread(){
        this.isRunning = false;
        this.entryLock = new ReentrantReadWriteLock();
        this.schedulers = new LinkedHashSet<>();
        this.evictBase = new AtomicLong();
    }

    /**
     * Starts the thread. Will keep running for 20 seconds unless another {@link Schedulers} is registered.
     */
    void start(){
        this.isRunning = true;
        this.thread = new Thread(this);
        this.evictBase.set(System.currentTimeMillis());
        this.thread.start();
    }

    /**
     * Stops the thread.
     */
    void stop(){
        this.isRunning = false;
    }

    /**
     * Registers another {@link Schedulers}. This will either start this thread or rebase the start time.
     * This thread will always get killed 20seconds after the last time this method was called.
     *
     * @param scheduler Scheduler that is being registered.
     */
    public void registerScheduler(SchedulerInterface scheduler){
        if(!isRunning){start();}
        this.evictBase.set(System.currentTimeMillis());
        this.entryLock.writeLock().lock();
        this.schedulers.add(scheduler);
        this.entryLock.writeLock().unlock();
    }

    /**
     * Iterates over all registered {@link Schedulers} to kill the ones that are 'done'.
     */
    @Override
    public void run() {
        while (isRunning){
            entryLock.readLock().lock();
            Iterator<SchedulerInterface> iterator = schedulers.iterator();
            SchedulerInterface scheduler;
            while (iterator.hasNext()){
                scheduler = iterator.next();
                if(scheduler.isDone()){
                    scheduler.shutDown();
                }
                iterator.remove();
            }
            entryLock.readLock().unlock();

            if(System.currentTimeMillis() - evictBase.get() >= STAY_ALIVE){
                stop();
            }

            sleep();
        }
    }

    private void sleep(){
        try {
            Thread.sleep(SLEEP_DELAY);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
