package com.omerozer.knit.schedulers;

import android.os.Handler;
import android.os.Looper;

/**
 * Created by omerozer on 3/27/18.
 */

public class MainHandlerSupplier implements MainHandlerSupplierInterface {

    private Handler handler = new Handler(Looper.getMainLooper());

    private final Object lock = new Object();

    @Override
    public void post(Runnable runnable){
        synchronized (lock){
            handler.post(runnable);
        }
    }

    @Override
    public Handler getMainHandler(){
        synchronized (lock){
            return handler;
        }
    }

}
