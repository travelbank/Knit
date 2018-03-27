package com.omerozer.knit;

import com.omerozer.knit.classloaders.KnitModelLoader;
import com.omerozer.knit.classloaders.KnitPresenterLoader;
import com.omerozer.knit.classloaders.KnitUtilsLoader;
import com.omerozer.knit.components.ModelManager;
import com.omerozer.knit.schedulers.SchedulerProvider;

/**
 * Created by Omer Ozer on 3/12/2018.
 */

public interface KnitInterface {

    SchedulerProvider getSchedulerProvider();

    ModelManager getModelManager();

    KnitNavigator getNavigator();

    KnitModelLoader getModelLoader();

    KnitPresenterLoader getPresenterLoader();

    KnitUtilsLoader getUtilsLoader();

    ViewEvents getViewEvents();

    MessagePool getMessagePool();

    MessageTrain getMessageTrain();

    ViewToPresenterMapInterface getViewToPresenterMap();

    ModelMapInterface getModelMap();
}
