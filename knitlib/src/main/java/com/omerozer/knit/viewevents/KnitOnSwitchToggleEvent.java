package com.omerozer.knit.viewevents;

import android.view.View;
import android.widget.CompoundButton;

/**
 * {@link ViewEventEnv} for simple {@link android.widget.Switch.OnCheckedChangeListener#onCheckedChanged(CompoundButton, boolean)} events.
 *
 * @see ViewEventEnv
 * @author Omer Ozer
 */

public class KnitOnSwitchToggleEvent extends ViewEventEnv {

    /**
     * Boolean toggle container for whether the state of the {@link CompoundButton} has changed or not.
     */
    private boolean toggle;

    public KnitOnSwitchToggleEvent(String tag, View view) {
        super(tag, view);
    }

    public KnitOnSwitchToggleEvent() {
        super();
    }

    /**
     * Setter for {@link this#toggle}.
     * @param toggle whether the state of the {@link CompoundButton} has changed or not.
     */
    public void setToggle(boolean toggle){
        this.toggle = toggle;
    }

    /**
     * Getter for {@link this#toggle}.
     * @return whether the state of the {@link CompoundButton} has changed or not.
     */
    public boolean getToggle(){
        return toggle;
    }
}
