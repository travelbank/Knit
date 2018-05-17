package com.travelbank.knit;


/**
 * {@link KnitPresenter} object that allows the use of Interactors.
 *
 * {@inheritDoc}
 */
public class KnitPresenter2<I, T> extends KnitPresenter<T> {

    private I interactor;

    protected I getInteractor() {
        if (interactor == null) {
            interactor = (I) getKnitInstance().findPresenterForParent(this).get().getInteractor();
        }
        return interactor;
    }

}
