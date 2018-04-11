package com.travelbank.knit.viewevents;

import android.os.Bundle;
import android.view.View;

import com.travelbank.knit.Poolable;

import java.lang.ref.WeakReference;

/**
 *
 * View Event Environment. Contains all data for a view event such as the tag of the event that's fired.
 *
 * @author Omer Ozer
 */



public abstract class ViewEventEnv implements Poolable {

    /**
     * Unique string tag for the event being fired.
     */
    private String tag;

    /**
     * Data bundle that contains all information.
     */
    private Bundle dataBundle;

    /**
     * Reference to the Android view that's associated with the event.
     */
    private WeakReference<View> viewWeakReference;

    protected ViewEventEnv(String tag,View view){
        this();
        this.tag = tag;
        this.viewWeakReference = new WeakReference<View>(view);
    }

    protected ViewEventEnv(){
        this.dataBundle = new Bundle();
    }

    /**
     * @see Poolable
     */
    @Override
    public void recycle(){
        this.tag = null;
        this.dataBundle.clear();
        this.viewWeakReference = null;
    }

    /**
     * Setter for {@link this#tag}
     * @param tag unique tag given to the {@link ViewEventEnv}.
     */
    public void setTag(String tag) {
        this.tag = tag;
    }

    /**
     * Setter for reference to the view.
     * @param view View instance to be set.
     */
    public void setViewWeakReference(View view) {
        this.viewWeakReference = new WeakReference<View>(view);
    }

    /**
     * Returns the instance of the Android view that fired this event.
     * @return
     */
    public View getView(){
        return viewWeakReference.get();
    }

    /**
     * Getter for {@link this#tag}
     * @return Unique tag for this event.
     */
    public String getTag() {
        return tag;
    }

    /**
     * Getter for {@link this#dataBundle}
     * @return Data container for this event.
     */
    public Bundle getDataBundle() {
        return dataBundle;
    }
}
