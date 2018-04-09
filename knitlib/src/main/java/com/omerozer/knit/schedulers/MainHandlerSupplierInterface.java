package com.omerozer.knit.schedulers;

import android.os.Handler;

/**
 * Interface that is used by {@link MainHandlerSupplier} and it's testing counterpart.
 *
 * @author Omer Ozer
 */

public interface MainHandlerSupplierInterface {

    /**
     * Accepts tasks to be posted to {@link Handler}.
     * @param runnable {@link Runnable} tasks.
     */
    void post(Runnable runnable);

    /**
     * Access main thread {@link Handler}.
     * @return Main thread {@link Handler}.
     */
    Handler getMainHandler();

}
