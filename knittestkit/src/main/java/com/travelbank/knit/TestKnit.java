package com.travelbank.knit;

import com.travelbank.knit.classloaders.KnitModelLoader;
import com.travelbank.knit.classloaders.KnitPresenterLoader;
import com.travelbank.knit.classloaders.KnitUtilsLoader;
import com.travelbank.knit.components.ModelManager;
import com.travelbank.knit.schedulers.SchedulerProvider;

/**
 * Created by Omer Ozer on 4/3/2018.
 */

public class TestKnit implements KnitInterface {
    @Override
    public SchedulerProvider getSchedulerProvider() {
        return new TestSchedulers();
    }

    @Override
    public ModelManager getModelManager() {
        return new TestModelManager(this);
    }

    @Override
    public KnitNavigator getNavigator() {
        return null;
    }

    @Override
    public KnitModelLoader getModelLoader() {
        return new KnitModelLoader(this);
    }

    @Override
    public KnitPresenterLoader getPresenterLoader() {
        return new KnitPresenterLoader(this);
    }

    @Override
    public KnitUtilsLoader getUtilsLoader() {
        return new KnitUtilsLoader();
    }

    @Override
    public ViewEvents getViewEvents() {
        return null;
    }

    @Override
    public MessagePool getMessagePool() {
        return null;
    }

    @Override
    public MessageTrain getMessageTrain() {
        return null;
    }

    @Override
    public ViewToPresenterMapInterface getViewToPresenterMap() {
        return getUtilsLoader().getViewToPresenterMap(Knit.class);
    }

    @Override
    public ModelMapInterface getModelMap() {
        return getUtilsLoader().getModelMap(Knit.class);
    }

    @Override
    public AttachmentMap getAttachmentMap() {
        return new AttachmentMap();
    }
}
