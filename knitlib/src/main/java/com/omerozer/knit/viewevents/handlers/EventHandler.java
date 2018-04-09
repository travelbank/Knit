package com.omerozer.knit.viewevents.handlers;

import com.omerozer.knit.InternalModel;
import com.omerozer.knit.viewevents.ViewEventEnv;
import com.omerozer.knit.viewevents.ViewEventPool;

/**
 *
 * Provides callback for {@link com.omerozer.knit.ViewEvents}.
 * {@link com.omerozer.knit.InternalPresenter} implements this.
 *
 * @author Omer Ozer
 */

public interface EventHandler {

    /**
     * {@link ViewEventEnv}s received by a {@link com.omerozer.knit.InternalPresenter} get sent to this callback.
     * @param eventPool {@link ViewEventPool} that will recycle/pool the sent event.
     * @param eventEnv {@link ViewEventEnv} that contains all data regarding the event.
     * @param modelManager Shared {@link InternalModel} instance.
     */
    void handle(ViewEventPool eventPool,ViewEventEnv eventEnv,InternalModel modelManager);
}
