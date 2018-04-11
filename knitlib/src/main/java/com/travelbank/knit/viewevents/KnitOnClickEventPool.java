package com.travelbank.knit.viewevents;

/**
 *
 * {@link ViewEventPool} that supports {@link KnitOnClickEvent}s.
 * Contained inside {@link com.travelbank.knit.ViewEvents}.
 *
 * @author Omer Ozer
 */

public class KnitOnClickEventPool extends ViewEventPool<KnitOnClickEvent> {

    /**
     * @see ViewEventPool
     */
    @Override
    protected KnitOnClickEvent createNewInstance() {
        return new KnitOnClickEvent();
    }


}
