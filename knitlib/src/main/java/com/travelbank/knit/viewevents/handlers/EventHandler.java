package com.travelbank.knit.viewevents.handlers;

import com.travelbank.knit.InternalModel;
import com.travelbank.knit.viewevents.ViewEventEnv;
import com.travelbank.knit.viewevents.ViewEventPool;

/**
 *
 * Provides callback for {@link com.travelbank.knit.ViewEvents}.
 * {@link com.travelbank.knit.InternalPresenter} implements this.
 *
 * @author Omer Ozer
 */

public interface EventHandler {

    /**
     * {@link ViewEventEnv}s received by a {@link com.travelbank.knit.InternalPresenter} get sent to this callback.
     * @param eventPool {@link ViewEventPool} that will recycle/pool the sent event.
     * @param eventEnv {@link ViewEventEnv} that contains all data regarding the event.
     * @param modelManager Shared {@link InternalModel} instance.
     */
    void handle(ViewEventPool eventPool,ViewEventEnv eventEnv,InternalModel modelManager);
}
