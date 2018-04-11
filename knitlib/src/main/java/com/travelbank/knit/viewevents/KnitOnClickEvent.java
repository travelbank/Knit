package com.travelbank.knit.viewevents;

import android.view.View;

/**
 * {@link ViewEventEnv} for simple {@link View.OnClickListener#onClick(View)} events.
 *
 * @see ViewEventEnv
 * @author Omer Ozer
 */


public class KnitOnClickEvent extends ViewEventEnv {

    public KnitOnClickEvent(String tag, View view) {
        super(tag, view);
    }

    public KnitOnClickEvent() {
        super();
    }

}
