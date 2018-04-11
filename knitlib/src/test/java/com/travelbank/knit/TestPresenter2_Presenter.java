package com.travelbank.knit;

import android.content.Intent;

import com.travelbank.knit.viewevents.ViewEventEnv;
import com.travelbank.knit.viewevents.ViewEventPool;

/**
 * Created by bo_om on 3/12/2018.
 */

public class TestPresenter2_Presenter extends InternalPresenter {
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
    public void onViewApplied(Object viewObject) {

    }

    @Override
    public void onCurrentViewReleased() {

    }

    @Override
    public InternalModel getModelManager() {
        return null;
    }

    @Override
    public KnitNavigator getNavigator() {
        return null;
    }

    @Override
    public Object getContract() {
        return null;
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
    public void receiveMessage(KnitMessage message) {

    }
}
