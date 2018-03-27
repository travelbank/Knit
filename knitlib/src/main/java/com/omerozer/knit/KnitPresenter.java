package com.omerozer.knit;

import android.content.Intent;
import android.os.Bundle;

import com.omerozer.knit.schedulers.KnitSchedulers;
import com.omerozer.knit.viewevents.ViewEventEnv;
import com.omerozer.knit.viewevents.ViewEventPool;
import com.omerozer.knit.viewevents.handlers.EventHandler;

import java.lang.ref.WeakReference;

/**
 * Created by omerozer on 2/12/18.
 */

public abstract class KnitPresenter<T> implements PresenterInterface,MessageReceiver {

    private WeakReference<Object> viewObjectRef;

    private Knit knitInstance;

    private InternalModel modelManager;

    private KnitNavigator navigator;

    private Object contract;

    public void setKnit(Knit knit) {
        this.knitInstance = knit;
    }

    @Override
    public void onViewApplied(Object viewObject) {
        this.viewObjectRef = new WeakReference<Object>(viewObject);
    }

    protected Object getView(){
        return viewObjectRef.get();
    }

    protected void request(String data,KnitSchedulers runOn, KnitSchedulers consumeOn,Object... params) {
        InternalPresenter instance = knitInstance.findPresenterForParent(this);
        getModelManager().request(data, runOn, consumeOn ,instance, params);
    }

    protected void request(String data,Object... params) {
        InternalPresenter instance = knitInstance.findPresenterForParent(this);
        getModelManager().request(data, KnitSchedulers.IMMEDIATE, KnitSchedulers.IMMEDIATE ,instance, params);
    }

    protected void inputData(String data, Object... params) {
        getModelManager().input(data, params);
    }

    protected T getContract() {
        if(contract == null){
            contract = knitInstance.findPresenterForParent(this).getContract();
        }
        return (T) contract;
    }

    protected InternalModel getModelManager() {
        if(modelManager == null){
            modelManager = knitInstance.findPresenterForParent(this).getModelManager();
        }
        return modelManager;
    }

    void setModelManager(InternalModel modelManager) {
        this.modelManager = modelManager;
    }

    void setNavigator(KnitNavigator navigator) {
        this.navigator = navigator;
    }

    void setContract(Object contract) {
        this.contract = contract;
    }

    protected KnitNavigator getNavigator() {
        if(navigator == null){
            navigator =  knitInstance.findPresenterForParent(this).getNavigator();
        }
        return navigator;
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onLoad() {

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public boolean shouldLoad() {
        return false;
    }

    @Override
    public void onMemoryLow() {

    }

    public KnitPresenter() {
        super();
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
    public void onCurrentViewReleased() {
        contract = null;
    }

    @Override
    public void onReturnToView() {

    }

    @Override
    public void onViewResult(int requestCode, int resultCode, Intent data) {

    }
    protected KnitMessage newMessage(){
        return knitInstance.getMessagePool().getObject();
    }


    @Override
    public void receiveMessage(KnitMessage message) {

    }

}
