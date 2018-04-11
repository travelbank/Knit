package com.travelbank.knit;

import java.util.List;

/**
 *
 * When KnitProcessor scans classpath, it creates a class called {@code ViewToPresenterMap} that implements {@link ViewToPresenterMapInterface}.
 * This class contains all data such as which presenter requires which data and what their parent classes are. Also maps all views to their {@link KnitPresenter}s.
 * This is how {@link com.travelbank.knit.components.graph.UsageGraph} determines dependencies.
 * This class is loaded via {@link com.travelbank.knit.classloaders.KnitUtilsLoader}.
 *
 * @author Omer Ozer
 */


public interface ViewToPresenterMapInterface {

    /**
     * Returns which {@link KnitPresenter} the given view should be attached to.
     * @param viewClass given view class.
     * @return Class of the {@link KnitPresenter}.
     */
    Class getPresenterClassForView(Class viewClass);

    /**
     * Returns which {@link InternalPresenter} given {@link KnitPresenter} attaches to.
     * @param parentClass Given {@link KnitPresenter} class.
     * @return Class of the corresponding {@link InternalPresenter}.
     */
    Class getPresenterClassForPresenter(Class parentClass);

    /**
     * Returns all view class annotated with {@link KnitView}.
     * @return A list of all classes annotated with {@link KnitView}.
     */
    List<Class<?>> getAllViews();

    /**
     * Returns a list of all data tags that a given {@link KnitPresenter} has it's {@link ModelEvent} methods tagged with.
     * @param clazz Given {@link KnitPresenter} class.
     * @return A list of all String tags given {@link KnitPresenter} has it's {@link ModelEvent} methods tagged with.
     */
    List<String> getUpdatingValues(Class clazz);
}
