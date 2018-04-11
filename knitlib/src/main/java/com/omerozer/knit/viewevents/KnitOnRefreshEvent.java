package com.omerozer.knit.viewevents;

import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;

/**
 * {@link ViewEventEnv} for simple {@link SwipeRefreshLayout.OnRefreshListener#onRefresh()} events.
 *
 * @see ViewEventEnv
 * @author Omer Ozer
 */

public class KnitOnRefreshEvent extends ViewEventEnv {

    public KnitOnRefreshEvent(String tag, View view) {
        super(tag, view);
    }

    public KnitOnRefreshEvent() {
        super();
    }

}
