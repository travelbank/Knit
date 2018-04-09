package com.omerozer.knit.schedulers;

import android.os.Handler;
import android.os.HandlerThread;

import java.util.concurrent.atomic.AtomicReference;

/**
 * This class contains a {@link HandlerThread} that receives results from all tasks that get ran
 * inside {@link KnitIOThreadPool}.
 *
 * @author Omer Ozer
 */

public class KnitIOReceiverThread {

    /**
     * Thread name for the {@link HandlerThread}
     */
    private static final String KNIT_LISTENER_THREAD_NAME = "knit_io_receiver";

    /**
     * {@link AtomicReference} for the {@link HandlerThread} that will be receiving results.
     */
    private AtomicReference<HandlerThread> receiverThread;

    /**
     * {@link Boolean} flag for whether the {@link HandlerThread} is running or not.
     */
    private volatile boolean isRunning;

    /**
     * {@link AtomicReference} for the {@link Handler} for the {@link HandlerThread}. All results are
     * posted through this.
     */
    private AtomicReference<Handler> receiverHandler;


    public KnitIOReceiverThread(){
        this.isRunning = false;
        this.receiverThread  = new AtomicReference<>();
        this.receiverHandler = new AtomicReference<>();
    }

    /**
     * Method that starts the {@link HandlerThread} that listens to results.
     */
    public void start(){
        if(!isRunning){
            this.receiverThread.set(new HandlerThread(KNIT_LISTENER_THREAD_NAME));
            this.receiverThread.get().start();
            this.isRunning = true;
        }
        this.receiverHandler.set(new Handler(receiverThread.get().getLooper()));
    }

    /**
     * Method that shuts down the {@link HandlerThread} that listens to results.
     */
    public void shutdown(){
        this.receiverThread.get().quit();
        this.isRunning = false;
    }


    /**
     * All results are passed through this method.
     * @param runnable {@link Runnable} that contains the task that handles that result.
     */
    public void post(Runnable runnable){
        this.receiverHandler.get().post(runnable);
    }

}
