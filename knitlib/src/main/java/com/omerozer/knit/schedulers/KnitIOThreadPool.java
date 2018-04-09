package com.omerozer.knit.schedulers;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Holds a {@link ExecutorService} with 4 Threads. Accepts both {@link Callable} and {@link Runnable} tasks.
 */

public class KnitIOThreadPool {

    /**
     * Default max size of the thread pool.
     */
    private static final int DEFAULT_THREAD_POOL_SIZE = 4;

    /**
     * {@link AtomicReference} for the contained {@link ExecutorService}.
     */
    private static AtomicReference<ExecutorService> THREAD_POOL;

    /**
     * Static constructor method to initialize the thread pool.
     */
    static {
        THREAD_POOL = new AtomicReference<>();
        init();
    }

    /**
     * Initializes the {@link this#THREAD_POOL} if it's shut down or null.
     */
    static void init(){
        if(THREAD_POOL.get()!=null && THREAD_POOL.get().isShutdown()){
            THREAD_POOL.set(Executors.newFixedThreadPool(DEFAULT_THREAD_POOL_SIZE));
        }else if (THREAD_POOL.get()==null){
            THREAD_POOL.set(Executors.newFixedThreadPool(DEFAULT_THREAD_POOL_SIZE));
        }
    }

    /**
     * Used when submitting {@link Callable}s to the pool.
     * @param callable task to be run.
     * @param <T> type of the result returned from when {@link Callable} is complete.
     */
    public<T> void submit(Callable<T> callable){
        THREAD_POOL.get().submit(callable);
    }

    /**
     * Used when submitting {@link Runnable}s to the pool.
     * @param runnable task to be run.
     */
    public void submit(Runnable runnable){
        THREAD_POOL.get().submit(runnable);
    }

}
