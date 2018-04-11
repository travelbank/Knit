package com.travelbank.knit.viewevents;

/**
 *
 * {@link ViewEventPool} that supports {@link KnitTextChangedEvent}s.
 * Contained inside {@link com.travelbank.knit.ViewEvents}.
 *
 * @author Omer Ozer
 */

public class KnitOnTextChangedEventPool extends ViewEventPool<KnitTextChangedEvent> {

    /**
     * @see ViewEventPool
     */
    @Override
    protected KnitTextChangedEvent createNewInstance() {
        return new KnitTextChangedEvent();
    }

    /**
     * @see ViewEventPool
     */
    @Override
    protected int getMaxPoolSize() {
        return 3;
    }
}
