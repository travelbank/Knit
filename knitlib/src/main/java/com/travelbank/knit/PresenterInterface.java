package com.travelbank.knit;


/**
 * Provides methods that handle Presenter specific functionality.
 *
 * @author Omer Ozer
 */

public interface PresenterInterface extends MemoryEntity,NativeViewCallbacks {


    /**
     * When a view is created/recreated this callback is called and given the instance of that view.
     * @param viewObject Created view instance.
     */
    void onViewApplied(Object viewObject);

    /**
     * When the current view attached to the presenter is killed for any reason, this callback is called.
     * Most common scenario is configuration changes.
     */
    void onCurrentViewReleased();
}
