package com.travelbank.knit.viewevents;

/**
 *
 * {@link ViewEventPool} that supports {@link AdapterItemSelectedEvent}s.
 * Contained inside {@link com.travelbank.knit.ViewEvents}.
 *
 * @see {@link android.widget.AdapterView.OnItemClickListener}
 * @author Omer Ozer
 */

public class AdapterItemSelectedEventPool extends ViewEventPool<AdapterItemSelectedEvent> {

    /**
     * @see ViewEventPool
     */
    @Override
    protected AdapterItemSelectedEvent createNewInstance() {
        return new AdapterItemSelectedEvent();
    }

    /**
     * @see ViewEventPool
     */
    @Override
    protected int getMaxPoolSize() {
        return 3;
    }
}
