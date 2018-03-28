package com.omerozer.knit.schedulers;

import android.os.Handler;

/**
 * Created by omerozer on 3/27/18.
 */

public interface MainHandlerSupplierInterface {

    void post(Runnable runnable);

    Handler getMainHandler();

}
