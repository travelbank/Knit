package com.omerozer.knit;

import android.app.Application;

import com.omerozer.knit.classloaders.KnitModelLoader;
import com.omerozer.knit.classloaders.KnitPresenterLoader;
import com.omerozer.knit.classloaders.KnitUtilsLoader;
import com.omerozer.knit.components.KnitMemoryManager;
import com.omerozer.knit.components.ModelManager;
import com.omerozer.knit.components.graph.UsageGraph;
import com.omerozer.knit.schedulers.SchedulerProvider;
import com.omerozer.knit.schedulers.Schedulers;

import java.lang.ref.WeakReference;

/**
 *
 * Main instance of Knit framework. Implements {@link KnitInterface} to provide shared objects such as {@link ViewEvents} & {@link UsageGraph}.
 * Holds an {@link Application} instance for resources that need it such as {@link KnitAppListener}.
 *
 * @author Omer Ozer
 */

public class Knit implements KnitInterface {

    /**
     * Singleton instance of Knit.
     */
    private static Knit instance;

    /**
     * Method that initializes Knit. All resources are created here.
     * @param application Current {@link Application} instance
     */
    public static void init(Application application) {
        instance = new Knit(application);
    }

    /**
     * Method that is used to access {@link this#instance}
     * @return
     */
    public static Knit getInstance(){
        return instance;
    }

    /**
     * Shared {@link KnitModelLoader} instance. Used by {@link UsageGraph}.
     */
    private KnitModelLoader knitModelLoader;

    /**
     * Shared {@link KnitPresenterLoader} instance. Used by {@link UsageGraph}.
     */
    private KnitPresenterLoader knitPresenterLoader;

    /**
     * Shared {@link KnitUtilsLoader} instance. Used by Knit to initialize {@link ViewToPresenterMapInterface} and {@link ModelMapInterface}.
     */
    private KnitUtilsLoader knitUtilsLoader;

    /**
     * Shared {@link UsageGraph} instance that holds dependency maps and {@link EntityInstance}s for components.
     */
    private UsageGraph userGraph;

    /**
     * Shared {@link SchedulerProvider} instance . Provides {@link com.omerozer.knit.schedulers.SchedulerInterface}s to {@link InternalModel}s.
     */
    private SchedulerProvider schedulerProvider;

    /**
     * Shared {@link ModelManager} instance. All active {@link InternalModel}s are registered here. All data requests also go through here.
     */
    private ModelManager modelManager;

    /**
     * Shared {@link KnitNavigator} instance that provides {@link KnitPresenter}s with inter presenter navigation.
     */
    private KnitNavigator navigator;

    /**
     * {@link Application} instance required by resources such as {@link KnitAppListener}.
     */
    private WeakReference<Application> app;

    /**
     * Shared {@link ViewEvents} instance that allows {@link KnitView}s to fire events back to their {@link KnitPresenter}s.
     */
    private ViewEvents viewEvents;

    /**
     * Shared {@link MessagePool} instance that handles recycling of {@link KnitMessage}s. Used by {@link KnitNavigator} via {@link KnitPresenter}.
     */
    private MessagePool messagePool;

    /**
     * Shared {@link MessageTrain} instance that carries messages from one {@link KnitPresenter} to another.
     */
    private MessageTrain messageTrain;

    /**
     * Shared {@link ModelMapInterface} instance generated by KnitProcessor. Contains all dependency data for Models/
     */
    private ModelMapInterface modelMap;

    /**
     * Shared {@link ViewToPresenterMapInterface} instance generated by KnitProcessor. Contains all dependency data for Presenters and Views.
     */
    private ViewToPresenterMapInterface viewToPresenterMap;

    private Knit(Application application){
        this.app = new WeakReference<Application>(application);
        this.modelManager = new ModelManager();
        this.schedulerProvider = new Schedulers();
        this.navigator = new KnitNavigator(this);
        this.knitPresenterLoader = new KnitPresenterLoader(this);
        this.knitUtilsLoader = new KnitUtilsLoader();
        this.knitModelLoader = new KnitModelLoader(this);
        this.messagePool = new MessagePool();
        this.modelMap = knitUtilsLoader.getModelMap(Knit.class);
        this.viewToPresenterMap = knitUtilsLoader.getViewToPresenterMap(Knit.class);
        this.userGraph = new UsageGraph(this);
        application.registerComponentCallbacks(new KnitMemoryManager(userGraph));
        application.registerActivityLifecycleCallbacks(new KnitAppListener(this));
    }

    /**
     * Returns shared {@link Application} instance.
     * @return Shared app instance.
     */
    public Application getApp(){
        return app.get();
    }

    /**
     * Tells {@link UsageGraph} to initialize dependencies of a particular View object.
     * @param viewObject given view object.
     */
    void initViewDependencies(Object viewObject) {
        userGraph.startViewAndItsComponents(viewObject);
    }

    /**
     * Tells {@link UsageGraph} to detach View object from it's dependencies. Dependencies will not be killed.
     * A use case for this is Configuration changes on an {@link android.app.Activity},
     * @param viewObject given view object.
     */
    void releaseViewFromComponent(Object viewObject) {
       userGraph.releaseViewFromComponent(viewObject);
    }

    /**
     * Tells {@link UsageGraph} to release and destroy if needed all dependency instances a given View object,
     * @param viewObject given view object.
     */
    void destroyComponent(Object viewObject){
        userGraph.stopViewAndItsComponents(viewObject);
    }

    /**
     * Returns {@link EntityInstance} of {@link KnitPresenter} for a view object.
     * @param viewObject given view object.
     * @return {@link EntityInstance} of the {@link KnitPresenter} for the given object.
     */
    public EntityInstance<InternalPresenter> findPresenterForView(Object viewObject) {
        return userGraph.getPresenterForView(viewObject);
    }

    /**
     * Returns {@link EntityInstance} of {@link InternalPresenter} of a {@link KnitPresenter} .
     * @param parentPresenter given {@link KnitPresenter}.
     * @return {@link EntityInstance} of the {@link KnitPresenter}.
     */
    public EntityInstance<InternalPresenter> findPresenterForParent(Object parentPresenter) {
        return (EntityInstance<InternalPresenter>) userGraph.getPresenterForObject(parentPresenter);
    }

    /**
     * @see KnitInterface
     */
    @Override
    public SchedulerProvider getSchedulerProvider() {
        return schedulerProvider;
    }

    /**
     * @see KnitInterface
     */
    @Override
    public ModelManager getModelManager(){
        return modelManager;
    }

    /**
     * @see KnitInterface
     */
    @Override
    public KnitNavigator getNavigator() {
        return navigator;
    }

    /**
     * @see KnitInterface
     */
    @Override
    public KnitModelLoader getModelLoader() {
        return knitModelLoader;
    }

    /**
     * @see KnitInterface
     */
    @Override
    public KnitPresenterLoader getPresenterLoader() {
        return knitPresenterLoader;
    }

    /**
     * @see KnitInterface
     */
    @Override
    public KnitUtilsLoader getUtilsLoader() {
        return knitUtilsLoader;
    }

    /**
     * Lazily instantiates and returns shared {@link ViewEvents}.
     * @see KnitInterface
     */
    @Override
    public ViewEvents getViewEvents() {
        if(viewEvents == null){
            viewEvents = new ViewEvents(this);
        }
        return viewEvents;
    }

    /**
     * @see KnitInterface
     */
    @Override
    public MessagePool getMessagePool() {
        return messagePool;
    }

    /**
     * Lazily instantiates and returns shared {@link MessageTrain}.
     * @see KnitInterface
     */
    @Override
    public MessageTrain getMessageTrain() {
        if(messageTrain == null){
            messageTrain = new MessageTrain();
        }
        return messageTrain;
    }

    /**
     * @see KnitInterface
     */
    @Override
    public ViewToPresenterMapInterface getViewToPresenterMap() {
        return viewToPresenterMap;
    }

    /**
     * @see KnitInterface
     */
    @Override
    public ModelMapInterface getModelMap() {
        return modelMap;
    }
}
