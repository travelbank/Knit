package com.omerozer.knit.viewevents;

/**
 *
 * {@link ViewEventPool} that supports {@link KnitOnRefreshEvent}s.
 * Contained inside {@link com.omerozer.knit.ViewEvents}.
 *
 * @author Omer Ozer
 */

public class KnitSwipeRefreshLayoutEventPool extends ViewEventPool<KnitOnRefreshEvent> {

    /**
     * @see ViewEventPool
     */
    @Override
    protected KnitOnRefreshEvent createNewInstance() {
        return new KnitOnRefreshEvent();
    }

    /**
     * @see ViewEventPool
     */
    @Override
    protected int getMaxPoolSize() {
        return 2;
    }
}
