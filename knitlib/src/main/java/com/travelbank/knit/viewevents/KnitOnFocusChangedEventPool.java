package com.travelbank.knit.viewevents;

/**
 *
 * {@link ViewEventPool} that supports {@link KnitOnFocusChangedEvent}s.
 * Contained inside {@link com.travelbank.knit.ViewEvents}.
 *
 * @author Omer Ozer
 */

public class KnitOnFocusChangedEventPool extends ViewEventPool<KnitOnFocusChangedEvent> {

    /**
     * @see ViewEventPool
     */
    @Override
    protected KnitOnFocusChangedEvent createNewInstance() {
        return new KnitOnFocusChangedEvent();
    }

    /**
     * @see ViewEventPool
     */
    @Override
    protected int getMaxPoolSize() {
        return 2;
    }
}
