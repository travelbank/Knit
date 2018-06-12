package com.travelbank.knit;

import android.content.Intent;

import com.travelbank.knit.viewevents.ViewEventEnv;
import com.travelbank.knit.viewevents.ViewEventPool;

/**
 * Created by omerozer on 3/8/18.
 */

public class TestPresenter_Presenter extends InternalPresenter {

    private Knit knit;

    private KnitNavigator knitNavigator;

    private InternalModel modelManager;

    Object accessor;

    public TestPresenter_Presenter(){

    }

    public TestPresenter_Presenter(Knit knitInstance, KnitNavigator navigator,
            InternalModel modelManager,Object accessor) {
        this.knit = knitInstance;
        this.knitNavigator = navigator;
        this.modelManager = modelManager;
        this.accessor = accessor;
    }


    public Knit getKnit() {
        return knit;
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onLoad() {

    }

    @Override
    public void onMemoryLow() {

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public boolean shouldLoad() {
        return false;
    }

    @Override
    public void onViewApplied(Object viewObject) {

    }

    @Override
    public void onCurrentViewReleased() {

    }

    @Override
    public InternalModel getModelManager() {
        return modelManager;
    }

    @Override
    public KnitNavigator getNavigator() {
        return knitNavigator;
    }

    @Override
    public Object getContract() {
        return null;
    }

    @Override
    public Object getInteractor() {
        return accessor;
    }

    @Override
    public String[] getUpdatableFields() {
        return new String[0];
    }

    @Override
    public KnitPresenter getParent() {
        return null;
    }

    @Override
    public void handle(ViewEventPool eventPool, ViewEventEnv eventEnv, InternalModel modelManager) {

    }

    @Override
    public void onViewStart() {

    }

    @Override
    public void onViewResume() {

    }

    @Override
    public void onViewPause() {

    }

    @Override
    public void onViewStop() {

    }

    @Override
    public void onViewResult(int requestCode, int resultCode, Intent data) {

    }

    @Override
    public void onReturnToView() {

    }

    @Override
    public void receiveMessage(KnitMessage message) {

    }
}
