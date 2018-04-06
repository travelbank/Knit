package com.omerozer.knit;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * This is used internally by Knit to listen for activity lifecycle events and handle them
 * accordingly.
 *
 * @author Vincent Williams
 */

public class KnitAppListener implements Application.ActivityLifecycleCallbacks {

    /**
     * Required to notify Knit about activity lifecycle events
     */
    private Knit knit;

    /**
     * Listener for fragment({@link Fragment} life cycle events
     */
    private FragmentManager.FragmentLifecycleCallbacks supportFragmentCallbacks;

    /**
     * Listener for fragment({@link android.app.Fragment} life cycle events
     */
    private android.app.FragmentManager.FragmentLifecycleCallbacks fragmentCallbacks;

    KnitAppListener(Knit knit) {
        this.knit = knit;
    }


    /**
     * Called when an {@link Activity} is created. This method attaches Fragment life cycle listeners
     * to the activity and initializes dependencies for it.
     *
     * @param activity Activity that has been created
     * @param bundle   Saved instance state of the Activity being created
     */
    @Override
    public void onActivityCreated(Activity activity, Bundle bundle) {
        attachFragmentListeners(activity);
        knit.initViewDependencies(activity);
    }


    private boolean returnedToView(Activity activity) {
        return false;
    }


    /**
     * Called when an {@link Activity} is started. It finds the presenter({@link InternalPresenter})
     * for the given activity and calls the presenters {@link InternalPresenter#onViewStart()
     * onViewStart} method.
     *
     * @param activity Activity that has started
     */
    @Override
    public void onActivityStarted(Activity activity) {
        if (knit.findPresenterForView(activity) != null) {
            if (returnedToView(activity)) {
                knit.findPresenterForView(activity).get().onReturnToView();
            }
            knit.findPresenterForView(activity).get().onViewStart();
        }
    }

    /**
     * Called when an {@link Activity} is resumed. It finds the presenter({@link InternalPresenter})
     * for the given activity and calls the presenters {@link InternalPresenter#onViewResume()
     * onViewResume} method.
     *
     * @param activity Activity that has resumed
     */
    @Override
    public void onActivityResumed(Activity activity) {
        if (knit.findPresenterForView(activity) != null) {
            knit.findPresenterForView(activity).get().onViewResume();
        }
    }

    /**
     * Called when an {@link Activity} is paused. It finds the presenter({@link InternalPresenter})
     * for the given activity and calls the presenters {@link InternalPresenter#onViewPause()
     * onViewPause} method.
     *
     * @param activity Activity that has paused
     */
    @Override
    public void onActivityPaused(Activity activity) {
        if (knit.findPresenterForView(activity) != null) {
            knit.findPresenterForView(activity).get().onViewPause();
        }
    }

    /**
     * Called when an {@link Activity} is stopped. It finds the presenter({@link InternalPresenter})
     * for the given activity and calls the presenters {@link InternalPresenter#onViewStop()
     * onViewStop} method.
     *
     * @param activity Activity that has been stopped
     */
    @Override
    public void onActivityStopped(Activity activity) {
        if (knit.findPresenterForView(activity) != null) {
            knit.findPresenterForView(activity).get().onViewStop();
        }
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {}

    /**
     * Called when an {@link Activity} has been destroyed. It tells knit to destroy the components
     * used by the activity.
     *
     * @param activity Activity that has been destroyed
     */
    @Override
    public void onActivityDestroyed(Activity activity) {
        if (activity.isFinishing()) {
            knit.destroyComponent(activity);
            return;
        }
        knit.releaseViewFromComponent(activity);
    }

    /**
     * Attaches fragment life cycle listeners to the specified activity.
     *
     * @param activity Activity to listen for fragment events
     */
    private void attachFragmentListeners(Activity activity) {
        if (activity instanceof AppCompatActivity) {
            ((AppCompatActivity) activity).getSupportFragmentManager()
                    .registerFragmentLifecycleCallbacks(
                            getSupportFragmentCallbacks(), true);
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                activity.getFragmentManager().registerFragmentLifecycleCallbacks(
                        getFragmentCallbacks(), true);
            }
        }
    }

    /**
     * Get a fragment life cycle listener for a {@link Fragment}
     *
     * @return Fragment life cycle listener
     */
    private FragmentManager.FragmentLifecycleCallbacks getSupportFragmentCallbacks() {
        if (supportFragmentCallbacks == null) {
            supportFragmentCallbacks = new FragmentManager.FragmentLifecycleCallbacks() {

                /**
                 * Called when the {@link Fragment} is created. Tells {@link Knit} to
                 * initialize the dependencies for the fragment.
                 *
                 * @param f {@link Fragment} being created
                 */
                @Override
                public void onFragmentViewCreated(FragmentManager fm, Fragment f, View v,
                        Bundle savedInstanceState) {
                    super.onFragmentViewCreated(fm, f, v, savedInstanceState);
                    knit.initViewDependencies(f);
                }

                /**
                 * Called when a {@link Fragment} is destroyed. Tells {@link Knit} to
                 * destroy the components used by the fragment.
                 *
                 * @param f {@link Fragment} being destroyed
                 */
                @Override
                public void onFragmentDestroyed(FragmentManager fm, Fragment f) {
                    super.onFragmentDestroyed(fm, f);
                    knit.destroyComponent(f);
                }
            };
        }
        return supportFragmentCallbacks;
    }

    /**
     * Get a fragment life cycle listener for a {@link android.app.Fragment}
     *
     * @return Fragment life cycle listener
     */
    @SuppressLint("NewApi")
    private android.app.FragmentManager.FragmentLifecycleCallbacks getFragmentCallbacks() {
        if (fragmentCallbacks == null) {
            fragmentCallbacks = new android.app.FragmentManager.FragmentLifecycleCallbacks() {

                /**
                 * Called when the {@link android.app.Fragment} is created. Tells {@link Knit} to
                 * initialize the dependencies for the fragment.
                 *
                 * @param f {@link android.app.Fragment} being created
                 */
                @Override
                public void onFragmentViewCreated(android.app.FragmentManager fm,
                        android.app.Fragment f, View v,
                        Bundle savedInstanceState) {
                    super.onFragmentViewCreated(fm, f, v, savedInstanceState);
                    knit.initViewDependencies(f);
                }

                /**
                 * Called when a {@link android.app.Fragment} is destroyed. Tells {@link Knit} to
                 * destroy the components used by the fragment.
                 *
                 * @param f {@link android.app.Fragment} being destroyed
                 */
                @Override
                public void onFragmentDestroyed(android.app.FragmentManager fm,
                        android.app.Fragment f) {
                    super.onFragmentDestroyed(fm, f);
                    knit.destroyComponent(f);
                }
            };
        }
        return fragmentCallbacks;
    }


}
