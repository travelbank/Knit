package com.omerozer.knit;

import android.app.Activity;
import android.content.Intent;

/**
 *
 * Callback interface that provides {@link KnitPresenter} with callback methods from a native Android {@link android.app.Activity}.
 *
 * @author Omer Ozer
 */

public interface NativeViewCallbacks {

    /**
     * Exposes {@link Activity#onStart()}.
     */
    void onViewStart();

    /**
     * Exposes {@link Activity#onResume()}.
     */
    void onViewResume();

    /**
     * Exposes {@link Activity#onPause()}.
     */
    void onViewPause();

    /**
     * Exposes {@link Activity#onStop()} .
     */
    void onViewStop();

    /**
     * Exposes {@link Activity#onActivityResult(int, int, Intent)}
     */
    void onViewResult(int requestCode, int resultCode, Intent data);

    /**
     * Called when the navigation returns to the current view after initializing another.
     */
    void onReturnToView();
}
