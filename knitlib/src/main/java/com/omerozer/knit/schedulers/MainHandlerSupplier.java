package com.omerozer.knit.schedulers;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Class that holds an instance to Android Main Thread Handler. Mainly required for better testability.
 *
 * @author Omer Ozer
 */

class MainHandlerSupplier implements MainHandlerSupplierInterface {

    /**
     * {@link Handler} instance to the Main Thread.
     */
    private Handler handler = new Handler(Looper.getMainLooper());

    /**
     * Accepts tasks to be posted to {@link Handler}.
     * @param runnable {@link Runnable} tasks.
     */
    @Override
    public void post(Runnable runnable){
        handler.post(runnable);
    }

    /**
     * Access main thread {@link Handler}.
     * @return Main thread {@link Handler}.
     */
    @Override
    public Handler getMainHandler(){
            return handler;
    }

}
