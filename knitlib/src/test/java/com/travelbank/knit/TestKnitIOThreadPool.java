package com.travelbank.knit;

import com.travelbank.knit.schedulers.KnitIOThreadPool;

import java.util.concurrent.Callable;

/**
 * Created by omerozer on 3/13/18.
 */

public class TestKnitIOThreadPool extends KnitIOThreadPool {

    @Override
    public <T> void submit(Callable<T> callable) {
        try {
            callable.call();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void submit(Runnable runnable) {
        runnable.run();
    }
}
