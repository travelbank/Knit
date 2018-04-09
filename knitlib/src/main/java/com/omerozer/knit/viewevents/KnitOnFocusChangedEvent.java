package com.omerozer.knit.viewevents;

import android.view.View;

import java.util.HashMap;

/**
 * {@link ViewEventEnv} for simple {@link View.OnFocusChangeListener#onFocusChange(View, boolean)} events.
 *
 * @see ViewEventEnv
 * @author Omer Ozer
 */

public class KnitOnFocusChangedEvent extends ViewEventEnv {

    /**
     * Key for retreiving boolean flag for whether the view has focus or not.
     */
    private static final String HAS_FOCUS = "b";

    public KnitOnFocusChangedEvent(String tag, View view) {
        super(tag, view);
    }

    public KnitOnFocusChangedEvent() {
        super();
    }

    /**
     * Extract the boolean flag for whether the view has focus or not by using {@value HAS_FOCUS} as a key on {@link this#dataBundle}.
     * @return
     */
    public boolean hasFocus(){
        return getDataBundle().getBoolean(HAS_FOCUS);
    }

    /**
     * Setter for boolean flag for whether the view has focus or not.
     * @param b boolean flag for whether the view has focus or not.
     */
    public void setFocus(boolean b){
        getDataBundle().putBoolean(HAS_FOCUS,b);
    }


}
