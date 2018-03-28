package com.omerozer.knit;

import android.os.Handler;

import com.omerozer.knit.schedulers.MainHandlerSupplierInterface;

import org.mockito.Mockito;

/**
 * Created by omerozer on 3/27/18.
 */

public class TestMainHandlerSupplier implements MainHandlerSupplierInterface {

    @Override
    public void post(Runnable runnable) {
        runnable.run();
    }

    @Override
    public Handler getMainHandler() {
        return Mockito.mock(Handler.class);
    }
}
