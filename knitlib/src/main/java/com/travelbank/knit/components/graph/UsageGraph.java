package com.travelbank.knit.components.graph;

import com.travelbank.knit.EntityInstance;
import com.travelbank.knit.InternalModel;
import com.travelbank.knit.InternalPresenter;
import com.travelbank.knit.KnitInterface;
import com.travelbank.knit.KnitMessage;
import com.travelbank.knit.KnitModel;
import com.travelbank.knit.MemoryEntity;
import com.travelbank.knit.MessagePool;
import com.travelbank.knit.MessageTrain;
import com.travelbank.knit.ModelMapInterface;
import com.travelbank.knit.ViewToPresenterMapInterface;
import com.travelbank.knit.classloaders.KnitModelLoader;
import com.travelbank.knit.classloaders.KnitPresenterLoader;
import com.travelbank.knit.components.ComponentTag;
import com.travelbank.knit.components.ModelManager;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * This is the core of smart life-cycle management of all components. This class creates a
 * dependency tree with all components created by KnitProcessor.
 * Receives all components from {@link ModelMapInterface} and {@link ViewToPresenterMapInterface},
 * assigns a {@link ComponentTag} to each one then inserts them
 * into the graph via {@link EntityNode}s . In the tree hierarchy, Entities annotated with {@link
 * com.travelbank.knit.KnitView} will be at the top. The initialization of
 * the views will trigger the initialization of the entities that the view requires. {@link
 * com.travelbank.knit.KnitView} -> {@link com.travelbank.knit.Presenter} -> {@link
 * com.travelbank.knit.Model}.
 * Initialized components will be put into a {@link Map<ComponentTag,EntityInstance>} that holds
 * their instances until they're released.
 *
 * Each of the annotated entities will have an internal counterpart that Knit uses.
 * {@link com.travelbank.knit.KnitView} -> As is
 * {@link com.travelbank.knit.Presenter} -> {@link com.travelbank.knit.KnitPresenter} -> {@link
 * InternalPresenter}
 * {@link com.travelbank.knit.Model} -> {@link com.travelbank.knit.KnitModel} -> {@link InternalModel}
 *
 *
 * However the initialization will be done in a bottom-up manner. Meaning {@link InternalModel}s
 * will be created first. Then {@link InternalPresenter}
 *
 * @author Omer Ozer
 * @see InternalModel
 * @see InternalPresenter
 * @see ModelManager
 */

public class UsageGraph {


    /**
     * UsageGraph needs this class to determine presenter dependencies.
     */
    private ViewToPresenterMapInterface viewToPresenterMap;

    /**
     * UsageGraph needs this class to determine model dependencies.
     */
    private ModelMapInterface modelMap;

    /**
     * Modelmanager needed to handle {@link ComponentTag} registrations/unregistrations
     */
    private ModelManager modelManager;

    /**
     * KnitModelLoader is used when initializing {@link InternalModel}s on the graph.
     */
    private KnitModelLoader knitModelLoader;

    /**
     * KnitPresenterLoader is used when initializing {@link InternalPresenter}s on the graph.
     */
    private KnitPresenterLoader knitPresenterLoader;

    /**
     * Message train delivers messages from one presenter to another. Since usagegraph is the class that is reponsible for their initialization,
     * it needs an instance of the {@link MessageTrain} also.
     */
    private MessageTrain messageTrain;

    /**
     * {@link MessagePool} holds the messages for the {@link MessageTrain} to the usagegraph needs this also.
     */
    private MessagePool messagePool;

    /**
     * {@link Map} that holds {@link UserCounter} for {@link ComponentTag}
     */
    private Map<ComponentTag, UserCounter> counterMap;

    /**
     * These are all presenters as they are the entry points of usagegraph.
     */
    private Map<ComponentTag, EntityNode> graphBase;

    /**
     * {@link Map} that maps Component classes to their associated {@link ComponentTag}s.
     */
    private Map<Class<?>, ComponentTag> clazzToTagMap;

    /**
     * {@link Map} that maps Component classes to their associated {@link Class}s.
     */
    private Map<ComponentTag, Class<?>> tagToClazzMap;

    /**
     * {@link Map} that maps Component classes to their associated {@link EntityInstance}s.
     * By default the instances will return null. Only once the component is initialized, it will return an actual instance .
     */
    private Map<ComponentTag, EntityInstance> instanceMap;

    /**
     * A {@link Set} of {@link ComponentTag}s for the models currently active in the memory.
     */
    private Set<ComponentTag> activeModelTags;

    /**
     * A {@link Set} of {@link ComponentTag}s for the presenters currently active in the memory.
     */
    private Set<ComponentTag> activePresenterTags;


    /**
     * {@link Map} that tracks whether a component class has it's node initialized or not.
     */
    private Map<Class<?>,EntityNode> clazzToNodeMap;


    public UsageGraph(KnitInterface knitInstance) {
        this.modelManager = knitInstance.getModelManager();
        this.modelManager.setUsageGraph(this);
        this.viewToPresenterMap = knitInstance.getViewToPresenterMap();
        this.modelMap = knitInstance.getModelMap();
        this.knitModelLoader = knitInstance.getModelLoader();
        this.knitPresenterLoader = knitInstance.getPresenterLoader();
        this.messageTrain = knitInstance.getMessageTrain();
        this.messagePool = knitInstance.getMessagePool();
        this.counterMap = new HashMap<>();
        this.graphBase = new HashMap<>();
        this.clazzToTagMap = new HashMap<>();
        this.tagToClazzMap = new HashMap<>();
        this.instanceMap = new HashMap<>();
        this.activeModelTags = new HashSet<>();
        this.activePresenterTags = new HashSet<>();
        this.clazzToNodeMap = new HashMap<>();
        createGraph();
    }

    /**
     * This is the method that creates the graph. Extracts generated values from {@link ModelMapInterface}, extract required values from {@link ViewToPresenterMapInterface}.
     * Even though the entry point of all components is the creation of a view, the graph does not need to hold any kind of view data other than it's presenter. So the base of the graph
     * consists of presenter data. Based on the required values of each presenter , it finds the model that generates those values, Then recursively checks if that model depends on another model(Umbrella Model).
     * If the model indeed depends on another, then it will recurse until dependency graph is created.
     */
    private void createGraph() {
        List<Class<? extends InternalModel>> models = modelMap.getAll();
        List<Class<?>> views = extractViews(viewToPresenterMap.getAllViews());
        Map<Class<? extends InternalModel>, List<String>> generatedValuesMap = new HashMap<>();
        for (Class<? extends InternalModel> clazz : models) {
            generatedValuesMap.put(clazz, modelMap.getGeneratedValues(clazz));
        }
        Map<Class<? extends InternalModel>, List<String>> requiredValues = new HashMap<>();
        for (Class<? extends InternalModel> clazz : models) {
            requiredValues.put(clazz, modelMap.getRequiredValues(clazz));
        }

        for (Class<?> view : views) {
            ComponentTag viewTag = ComponentTag.getNewTag();
            counterMap.put(viewTag, new UserCounter());
            graphBase.put(viewTag, new EntityNode(viewTag, EntityType.VIEW));
            clazzToTagMap.put(view, viewTag);
            tagToClazzMap.put(viewTag, view);
            Class<?> presenterClass = viewToPresenterMap.getPresenterClassForView(view);
            ComponentTag presenterTag = ComponentTag.getNewTag();
            counterMap.put(presenterTag, new UserCounter());
            clazzToTagMap.put(presenterClass, presenterTag);
            tagToClazzMap.put(presenterTag, presenterClass);
            EntityNode presenterEntityNode = new EntityNode(presenterTag, EntityType.PRESENTER);
            graphBase.get(viewTag).next.add(presenterEntityNode);
            instanceMap.put(presenterTag, new EntityInstance<InternalPresenter>());
            for (String updating : viewToPresenterMap.getUpdatingValues(presenterClass)) {
                for(Class<? extends InternalModel> model:  models){
                    createModelTag(model);
                    if (generatedValuesMap.get(model).contains(updating)) {
                        EntityNode node;
                        if(!clazzToNodeMap.containsKey(model)){
                            node = new EntityNode(clazzToTagMap.get(model), EntityType.MODEL);
                            recurseAndCreateModel(model,node,models,generatedValuesMap,requiredValues);
                            clazzToNodeMap.put(model,node);
                        }
                        presenterEntityNode.next.add(clazzToNodeMap.get(model));
                    }

                }
            }
        }

    }

    /**
     * This is the recursive method that creates the model dependencies.
     *
     * @param modelClazz Model class that is being worked on
     * @param modelNode Node for that contains the current model.
     * @param models List of all models in the environment.
     * @param generatedValuesMap {@link Map} of all values generated by each {@link InternalModel}
     * @param requiredValuesMap {@link Map} of all values required by each {@link InternalModel}
     */
    private void recurseAndCreateModel(Class<? extends InternalModel> modelClazz ,EntityNode modelNode,
            List<Class<? extends InternalModel>> models,
            Map<Class<? extends InternalModel>, List<String>> generatedValuesMap,
            Map<Class<? extends InternalModel>, List<String>> requiredValuesMap) {

        for(String req :requiredValuesMap.get(modelClazz)){
            for(Class<? extends InternalModel> model : models){
                if(generatedValuesMap.get(model).contains(req)){
                    EntityNode node;
                    if(!clazzToNodeMap.containsKey(model)){
                        createModelTag(model);
                        node = new EntityNode(clazzToTagMap.get(model), EntityType.MODEL);
                        recurseAndCreateModel(model,node,models,generatedValuesMap,requiredValuesMap);
                        clazzToNodeMap.put(model,node);
                    }
                    modelNode.next.add(clazzToNodeMap.get(model));
                }
            }
        }

    }


    /**
     * Creates all required objects that {@link UsageGraph} uses for the given {@link InternalModel} class.
     * If it already exists, then does nothing.
     * @param clazz Given {@link InternalModel} class.
     */
    private void createModelTag(Class<? extends InternalModel> clazz) {
        if (!clazzToTagMap.containsKey(clazz)) {
            ComponentTag modelTag = ComponentTag.getNewTag();
            clazzToTagMap.put(clazz, modelTag);
            counterMap.put(modelTag, modelMap.isModelSingleton(clazz) ? new SingletonUserCounter()
                    : new UserCounter());
            tagToClazzMap.put(modelTag, clazz);
            instanceMap.put(modelTag, new EntityInstance<InternalModel>());
        }
    }


    /**
     * Returns a {@link Collection} of entities currently living in the memory.
     *
     * @return entities currently living in the memory.
     */
    public Collection<EntityInstance> activeEntities() {
        return instanceMap.values();
    }

    /**
     * Returns {@link InternalModel} associated with the given {@link ComponentTag}.
     *
     * @param componentTag Tag that is being searched for.
     * @return returning the model associated with the tag.
     * @see EntityInstance
     */
    public EntityInstance<InternalModel> getModelWithTag(ComponentTag componentTag) {
        return instanceMap.get(componentTag);
    }

    /**
     * Returns {@link InternalPresenter} associated with the given {@link ComponentTag}.
     *
     * @param componentTag Tag that is being searched for.
     * @return returning the presenter associated with the tag.
     */
    public EntityInstance<InternalPresenter> getPresenterTag(ComponentTag componentTag) {
        return instanceMap.get(componentTag);
    }


    /**
     * Finds the internal counterpart({@link InternalPresenter}) of a {@link
     * com.travelbank.knit.KnitPresenter} instance.
     *
     * @param presenterObject {@link com.travelbank.knit.KnitPresenter} object exposed to the
     *                        developer.
     * @return {@link} returning the internal part Knit uses.
     * @see UsageGraph
     */
    public EntityInstance getPresenterForObject(Object presenterObject) {
        return instanceMap.get(clazzToTagMap.get(
                viewToPresenterMap.getPresenterClassForPresenter(presenterObject.getClass())));
    }


    /**
     * Finds {@link InternalPresenter} for a view object .
     *
     * @param viewObject View object (Either a {@link android.app.Activity} or a {@link
     *                   android.app.Fragment}).
     * @return {@link InternalPresenter} returning the presenter for the object.
     */
    public EntityInstance getPresenterForView(Object viewObject) {
        return instanceMap.get(clazzToTagMap.get(
                viewToPresenterMap.getPresenterClassForView(viewObject.getClass())));
    }


    /**
     * Attaches a view object to an underlying presenter({@link InternalPresenter}.
     * Increments the {@link UserCounter} for each entity.
     *
     * @param viewObject View object (Either a {@link android.app.Activity} or a {@link
     *                   android.app.Fragment})
     */
    public void attachViewToComponent(Object viewObject) {
        if (!graphBase.containsKey(clazzToTagMap.get(viewObject.getClass()))) {
            return;
        }

        for (EntityNode presenter : graphBase.get(clazzToTagMap.get(viewObject.getClass())).next) {
            if (instanceMap.get(presenter.tag).isAvailable()) {
                ((InternalPresenter) instanceMap.get(presenter.tag).get()).onViewApplied(
                        viewObject);
            }
        }
    }


    /**
     * Calls the {@link InternalPresenter#onCurrentViewReleased()} on the presenter of the viewObject.
     *
     * @param viewObject View object (Either a {@link android.app.Activity} or a {@link
     *                   android.app.Fragment})
     * @see EntityInstance
     * @see com.travelbank.knit.MemoryEntity
     */
    public void releaseViewFromComponent(Object viewObject) {
        if (!graphBase.containsKey(clazzToTagMap.get(viewObject.getClass()))) {
            return;
        }
        for (EntityNode presenter : graphBase.get(clazzToTagMap.get(viewObject.getClass())).next) {
            if (instanceMap.get(presenter.tag).isAvailable()) {
                ((InternalPresenter) instanceMap.get(presenter.tag).get()).onCurrentViewReleased();
            }
        }
    }

    /**
     * Returns true if there already is an active(Initialized) Component for a particular view object.
     * Since views may be destroyed during events such as configuration changes, we need this method to determine
     * whether components exists or not.
     *
     * Checks the graph base for the particular view, returns false if it doesn't exists as this this means that view isn't a part
     * of Knit domain(Does not have {@link com.travelbank.knit.KnitView} annotation).
     *
     * Otherwise iterates through all next fields of the {@link EntityNode} associated with that view and if there is an available
     * instance, returns true. If not, returns false.
     *
     */
    private boolean isComponentCreated(Object viewObject) {
        if (!graphBase.containsKey(clazzToTagMap.get(viewObject.getClass()))) {
            return false;
        }
        for (EntityNode entityNode : graphBase.get(clazzToTagMap.get(viewObject.getClass())).next) {
            if (instanceMap.get(entityNode.tag).isAvailable()) {
                return true;

            }
        }
        return false;
    }

    /**
     * Attaches a view object to an underlying presenter({@link InternalPresenter} & required {@link
     * InternalModel}s). Initializes them if they aren't already initialized.
     * For each entity that's initialized, an {@link MemoryEntity#onCreate()} method is called.
     * Increments the {@link UserCounter} for each entity.
     *
     * @param viewObject View object (Either a {@link android.app.Activity} or a {@link
     *                   android.app.Fragment}).
     * @see EntityInstance
     * @see com.travelbank.knit.MemoryEntity
     */
    public void startViewAndItsComponents(Object viewObject) {
        if (isComponentCreated(viewObject)) {
            attachViewToComponent(viewObject);
            return;
        }

        Class<?> clazz = viewObject.getClass();
        if (!clazzToTagMap.containsKey(clazz)) {
            return;
        }
        recurseTraverseTheGraphAndStartIfNeeded(clazzToTagMap.get(clazz), viewObject);
    }

    /**
     * Entry method for {@link this#recurseForStart(EntityNode, Object)} method. Starts recursion for initializing all components({@link InternalModel},{@link InternalPresenter})
     * for a particular view.
     * @param tag Tag of the given View.
     * @param viewObject instance of the given view. This is only needed as this gets passed to the {@link com.travelbank.knit.KnitPresenter} inside {@link InternalPresenter#onViewApplied} method.
     *
     */
    private void recurseTraverseTheGraphAndStartIfNeeded(ComponentTag tag, Object viewObject) {
        recurseForStart(graphBase.get(tag), viewObject);
    }


    /**
     * Recursive method that initializes all components and stores their instances. Runs a DFS to start all models first.
     * Also handles the lifecylce methods such as {@link MemoryEntity#onCreate()}.
     * @param entityNode Entry point of the graph. {@link EntityNode} of a view.
     * @param viewObject Instance of the view that is getting it's dependencies started.
     */
    private void recurseForStart(EntityNode entityNode, Object viewObject) {
        //DFS to create models first

        if (entityNode == null) {
            return;
        }

        for (EntityNode node : entityNode.next) {
            recurseForStart(node, viewObject);
        }

        switch (entityNode.type) {
            case MODEL:
                if (!counterMap.get(entityNode.tag).isUsed()) {
                    InternalModel internalModel = knitModelLoader.loadModel(
                            tagToClazzMap.get(entityNode.tag));
                    instanceMap.get(entityNode.tag).set(internalModel);
                    activeModelTags.add(entityNode.tag);
                    modelManager.registerModelComponentTag(entityNode.tag);
                    internalModel.onCreate();
                }
                break;

            case PRESENTER:
                InternalPresenter internalPresenter;
                if (!counterMap.get(entityNode.tag).isUsed()) {
                    internalPresenter = knitPresenterLoader.loadPresenter(
                            tagToClazzMap.get(entityNode.tag));
                    instanceMap.get(entityNode.tag).set(internalPresenter);
                    activePresenterTags.add(entityNode.tag);
                    internalPresenter.onCreate();
                }
                internalPresenter = ((InternalPresenter) instanceMap.get(entityNode.tag).get());
                internalPresenter.onViewApplied(viewObject);
                handleMessageDelivery(internalPresenter, entityNode.tag);
                internalPresenter.onViewCreated();
                break;
        }
        counterMap.get(entityNode.tag).use();

    }


    /**
     * This method delivers the message sent from one {@link com.travelbank.knit.InternalPresenter} to another.
     * Message is contained inside {@link MessageTrain}.
     * @param presenter Presenter instance that will be receiving the message.
     * @param tag Tag of the Presenter. Used to fetch the class type.
     */
    private void handleMessageDelivery(InternalPresenter presenter, ComponentTag tag) {
        if (messageTrain.hasMessage(tagToClazzMap.get(tag))) {
            KnitMessage message = messageTrain.getMessageForView(tagToClazzMap.get(tag));
            presenter.receiveMessage(message);
            messagePool.pool(message);
        }
    }

    /**
     * Decrements {@link UserCounter} of all entities depended on this view. If the counter reaches
     * to 0, destroys them calling {@link MemoryEntity#onDestroy()}
     *
     * @see com.travelbank.knit.MemoryEntity
     */
    public void stopViewAndItsComponents(Object viewObject) {
        recurseTraverseTheGraphAndDestroyIfNeeded(clazzToTagMap.get(viewObject.getClass()));
    }

    /**
     * Entry method for {@link this#recurseForFinish(EntityNode)} method. Starts recursion for releasing all components({@link InternalModel},{@link InternalPresenter})
     * for a particular view. If the {@link UserCounter} becomes 0 for a particular component,they get destroyed.
     * @param tag Tag of the given View.
     *
     */
    private void recurseTraverseTheGraphAndDestroyIfNeeded(ComponentTag tag) {
        recurseForFinish(graphBase.get(tag));
    }


    /**
     * Recursive method that releases all components. Runs a DFS to release all models first.
     * Also handles the lifecycle methods such as {@link MemoryEntity#onDestroy()}. If
     * @param entityNode Entry point of the graph. {@link EntityNode} of a view.
     */
    private void recurseForFinish(EntityNode entityNode) {

        if (entityNode == null) {
            return;
        }

        //Again DFS to finish models first
        for (EntityNode node : entityNode.next) {
            recurseForFinish(node);
        }

        counterMap.get(entityNode.tag).release();
        switch (entityNode.type) {
            case MODEL:
                if (!counterMap.get(entityNode.tag).isUsed()) {
                    instanceMap.get(entityNode.tag).get().onDestroy();
                    activeModelTags.remove(entityNode.tag);
                    modelManager.unregisterComponentTag(entityNode.tag);
                    instanceMap.get(entityNode.tag).nullify();
                }
                break;
            case PRESENTER:
                if (!counterMap.get(entityNode.tag).isUsed()) {
                    instanceMap.get(entityNode.tag).get().onDestroy();
                    activePresenterTags.remove(entityNode.tag);
                    instanceMap.get(entityNode.tag).nullify();
                }
                break;
        }


    }

    /**
     * ExtractViews from all component classes. Since views are indented by an Object.class object.
     * @param views List of all views.
     * @return returns a sublist of views. Without the Object.class .
     */
    private List<Class<?>> extractViews(List<Class<?>> views) {
        return views.subList(0, views.size() - 1);
    }


}
