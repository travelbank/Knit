package com.travelbank.knit.viewevents;

/**
 *
 * {@link ViewEventPool} for {@link TabSelectedEvent}s.
 *
 * @author Omer Ozer
 */

public class TabSelectionEventPool extends ViewEventPool<TabSelectedEvent> {

    /**
     * @see ViewEventPool
     */
    @Override
    protected TabSelectedEvent createNewInstance() {
        return new TabSelectedEvent();
    }

    /**
     * @see ViewEventPool
     */
    @Override
    protected int getMaxPoolSize() {
        return 3;
    }
}
