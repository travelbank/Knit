package com.omerozer.knit.viewevents;

/**
 *
 * {@link ViewEventPool} that supports {@link GenericEvent}s.
 * Contained inside {@link com.omerozer.knit.ViewEvents}.
 *
 * @author Omer Ozer
 */

public class GenericEventPool extends ViewEventPool<GenericEvent> {

    /**
     * @see ViewEventPool
     */
    @Override
    protected GenericEvent createNewInstance() {
        return new GenericEvent();
    }

    /**
     * @see ViewEventPool
     */
    @Override
    protected int getMaxPoolSize() {
        return 4;
    }
}
