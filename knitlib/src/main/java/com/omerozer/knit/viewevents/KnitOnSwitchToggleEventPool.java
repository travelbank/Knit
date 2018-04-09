package com.omerozer.knit.viewevents;

/**
 *
 * {@link ViewEventPool} that supports {@link KnitOnSwitchToggleEvent}s.
 * Contained inside {@link com.omerozer.knit.ViewEvents}.
 *
 * @author Omer Ozer
 */

public class KnitOnSwitchToggleEventPool extends ViewEventPool<KnitOnSwitchToggleEvent> {

    /**
     * @see ViewEventPool
     */
    @Override
    protected KnitOnSwitchToggleEvent createNewInstance() {
        return new KnitOnSwitchToggleEvent();
    }
}
