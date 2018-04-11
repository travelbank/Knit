package com.travelbank.knit;

import com.travelbank.knit.viewevents.handlers.EventHandler;

/**
 *
 * This class is the master class of {@link KnitPresenter} that is exposed to the user. Knit's annotation processor creates
 * these {@link InternalPresenter}s that initialize and contain the actual {@link KnitPresenter}. All {@link KnitPresenter}s annotated with a {@link Presenter}
 * will have an internal counterpart that manages them. The master class created by the processor will have a name "ClassName" + "_Presenter".
 * Actual {@link KnitPresenter} instance will be wrapped around an Exposer class that will expose all of it's public and package-private methods
 * to the {@link InternalPresenter} . T
 *
 * @see Presenter
 * @see KnitPresenter
 * @see PresenterInterface
 *
 * @author Omer Ozer
 */

public abstract class InternalPresenter implements EventHandler,PresenterInterface,MessageReceiver {


    /**
     * Returns the shared {@link com.travelbank.knit.components.ModelManager} instance.
     * @return
     */
    public abstract InternalModel getModelManager();

    /**
     * Returns the shared {@link KnitNavigator} instance.
     * @return the shared {@link KnitNavigator} instance.
     */
    public abstract KnitNavigator getNavigator();

    /**
     * Returns the current contract instance. {@link KnitPresenter} will cast it accordingly based on it's need.s
     * @return Current contract instance.
     */
    public abstract Object getContract();

    /**
     * Returns a String array that contains all data tags that this presenter listens to.
     * @return A String array that contains all data tags that this presenter listens to.
     */
    public abstract String[] getUpdatableFields();

    /**
     * Returns the instance of the parent {@link KnitPresenter} that is exposed to the user.
     * @return the instance of the parent {@link KnitPresenter} that is exposed to the user.
     */
    public abstract KnitPresenter getParent();

}
