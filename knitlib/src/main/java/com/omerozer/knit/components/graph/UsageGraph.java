package com.omerozer.knit.components.graph;

import com.omerozer.knit.EntityInstance;
import com.omerozer.knit.InternalModel;
import com.omerozer.knit.InternalPresenter;
import com.omerozer.knit.KnitInterface;
import com.omerozer.knit.KnitMessage;
import com.omerozer.knit.MessagePool;
import com.omerozer.knit.MessageTrain;
import com.omerozer.knit.ModelMapInterface;
import com.omerozer.knit.ViewToPresenterMapInterface;
import com.omerozer.knit.classloaders.KnitModelLoader;
import com.omerozer.knit.classloaders.KnitPresenterLoader;
import com.omerozer.knit.components.ComponentTag;
import com.omerozer.knit.components.ModelManager;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * This is the core of smart life-cycle management of all components. This class creates a dependency tree with all components created by KnitProcessor.
 * Receives all components from {@link ModelMapInterface} and {@link ViewToPresenterMapInterface}, assigns a {@link ComponentTag} to each one then inserts them
 * into the graph via {@link EntityNode}s . In the tree hierarchy, Entities annotated with {@link com.omerozer.knit.KnitView} will be at the top. The initialization of
 * the views will trigger the initialization of the entities that the view requires. {@link com.omerozer.knit.KnitView} -> {@link com.omerozer.knit.Presenter} -> {@link com.omerozer.knit.Model}.
 * Initialized components will be put into a {@link Map<ComponentTag,EntityInstance>} that holds their instances until they're released.
 *
 * Each of the annotated entities will have an internal counterpart that Knit uses.
 * {@link com.omerozer.knit.KnitView} -> As is
 * {@link com.omerozer.knit.Presenter} -> {@link com.omerozer.knit.KnitPresenter} -> {@link InternalPresenter}
 * {@link com.omerozer.knit.Model} -> {@link com.omerozer.knit.KnitModel} -> {@link InternalModel}
 *
 *
 * However the initialization will be done in a bottom-up manner. Meaning {@link InternalModel}s will be created first. Then {@link InternalPresenter}
 *
 * @see InternalModel
 * @see InternalPresenter
 * @see ModelManager
 * @author Omer Ozer
 */

public class UsageGraph {

    private ViewToPresenterMapInterface viewToPresenterMap;

    private ModelMapInterface modelMap;

    private ModelManager modelManager;

    private KnitModelLoader knitModelLoader;

    private KnitPresenterLoader knitPresenterLoader;

    private MessageTrain messageTrain;

    private MessagePool messagePool;

    private Map<ComponentTag, UserCounter> counterMap;

    private Map<ComponentTag, EntityNode> graphBase;

    private Map<Class<?>, ComponentTag> clazzToTagMap;

    private Map<ComponentTag, Class<?>> tagToClazzMap;

    private Map<ComponentTag, EntityInstance> instanceMap;

    private Set<ComponentTag> activeModelTags;

    private Set<ComponentTag> activePresenterTags;


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
        createGraph();
    }

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
            instanceMap.put(presenterTag,new EntityInstance<InternalPresenter>());
            for (String updating : viewToPresenterMap.getUpdatingValues(presenterClass)) {
                Iterator<Class<? extends InternalModel>> modelOuterItr = models.iterator();
                while (modelOuterItr.hasNext()) {
                    Class<? extends InternalModel> modelClazz = modelOuterItr.next();
                    createModelTag(modelClazz);
                    if (generatedValuesMap.get(modelClazz).contains(updating)) {
                        EntityNode node = new EntityNode(clazzToTagMap.get(modelClazz),
                                EntityType.MODEL);
                        Iterator<Class<? extends InternalModel>> modelInnerItr = models.iterator();
                        while (modelInnerItr.hasNext()) {
                            Class<? extends InternalModel> innerClazz = modelInnerItr.next();
                            for (String req : requiredValues.get(modelClazz)) {
                                if (generatedValuesMap.get(innerClazz).contains(req)) {
                                    createModelTag(innerClazz);
                                    EntityNode reqM = new EntityNode(clazzToTagMap.get(innerClazz),
                                            EntityType.MODEL);
                                    node.next.add(reqM);
                                }
                            }

                        }

                        presenterEntityNode.next.add(node);
                    }

                }
            }
        }

    }

    private void createModelTag(Class<? extends InternalModel> clazz) {
        if (!clazzToTagMap.containsKey(clazz)) {
            ComponentTag modelTag = ComponentTag.getNewTag();
            clazzToTagMap.put(clazz, modelTag);
            counterMap.put(modelTag, modelMap.isModelSingleton(clazz) ? new SingletonUserCounter()
                    : new UserCounter());
            tagToClazzMap.put(modelTag, clazz);
            instanceMap.put(modelTag,new EntityInstance<InternalModel>());
        }
    }




            /**
             *  Returns a {@link Collection} of entities currently living in the memory.
             * @return entities currently living in the memory.
             */
    public Collection<EntityInstance> activeEntities() {
        return instanceMap.values();
    }

            /**
             * Returns {@link InternalModel} associated with the given {@link ComponentTag}.
             * @param componentTag Tag that is being searched for.
             * @return returning the model associated with the tag.
             * @see EntityInstance
             */
    public EntityInstance<InternalModel> getModelWithTag(ComponentTag componentTag) {
        return instanceMap.get(componentTag);
    }

            /**
             * Returns {@link InternalPresenter} associated with the given {@link ComponentTag}.
             * @param componentTag Tag that is being searched for.
             * @return returning the presenter associated with the tag.
             */
    public EntityInstance<InternalPresenter> getPresenterTag(ComponentTag componentTag) {
        return instanceMap.get(componentTag);
    }


            /**
             * Finds the internal counterpart({@link InternalPresenter}) of a {@link com.omerozer.knit.KnitPresenter} instance.
             * @param presenterObject {@link com.omerozer.knit.KnitPresenter} object exposed to the developer.
             * @return {@link} returning the internal part Knit uses.
             * @see UsageGraph
             */
    public EntityInstance getPresenterForObject(Object presenterObject) {
        return instanceMap.get(clazzToTagMap.get(
                viewToPresenterMap.getPresenterClassForPresenter(presenterObject.getClass())));
    }


        /**
         * Finds {@link InternalPresenter} for a view object .
         * @param viewObject View object (Either a {@link android.app.Activity} or a {@link android.app.Fragment}).
         * @return {@link InternalPresenter} returning the presenter for the object.
         */
    public EntityInstance getPresenterForView(Object viewObject) {
        return instanceMap.get(clazzToTagMap.get(
                viewToPresenterMap.getPresenterClassForView(viewObject.getClass())));
    }


        /**
         * Attaches a view object to an underlying presenter({@link InternalPresenter}.
         * Increments the {@link UserCounter} for each entity.
         * @param viewObject View object (Either a {@link android.app.Activity} or a {@link android.app.Fragment})
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
     * Calls the {@code .onViewReleased()} on the presenter of the viewObject.
     * @param viewObject View object (Either a {@link android.app.Activity} or a {@link android.app.Fragment})
     * @see EntityInstance
     * @see com.omerozer.knit.MemoryEntity
     */
    public void releaseViewFromComponent(Object viewObject){
        if(!graphBase.containsKey(clazzToTagMap.get(viewObject.getClass()))){
            return;
        }
        for (EntityNode presenter : graphBase.get(clazzToTagMap.get(viewObject.getClass())).next) {
            if(instanceMap.get(presenter.tag).isAvailable()){
                ((InternalPresenter) instanceMap.get(presenter.tag).get()).onCurrentViewReleased();
            }
        }
    }

    private boolean isComponentCreated(Object viewObject) {
        if (!graphBase.containsKey(clazzToTagMap.get(viewObject.getClass()))) {
            return false;
        }
        for (EntityNode entityNode : graphBase.get(clazzToTagMap.get(viewObject.getClass())).next) {
            if(instanceMap.get(entityNode.tag).isAvailable()){
                return true;

            }
        }
        return false;
    }

    /**
     * Attaches a view object to an underlying presenter({@link InternalPresenter} & required {@link InternalModel}s). Initializes them if they aren't already initialized.
     * For each entity that's initialized, an {@code .onCreate()} method is called.
     * Increments the {@link UserCounter} for each entity.
     * @param viewObject View object (Either a {@link android.app.Activity} or a {@link android.app.Fragment}).
     * @see EntityInstance
     * @see com.omerozer.knit.MemoryEntity
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

    private void recurseTraverseTheGraphAndStartIfNeeded(ComponentTag tag, Object viewObject) {
        recurseForStart(graphBase.get(tag), viewObject);
    }


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
                    InternalModel internalModel = knitModelLoader.loadModel(tagToClazzMap.get(entityNode.tag));
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
                break;
        }
        counterMap.get(entityNode.tag).use();

    }

    private void handleMessageDelivery(InternalPresenter presenter, ComponentTag tag) {
        if (messageTrain.hasMessage(tagToClazzMap.get(tag))) {
            KnitMessage message = messageTrain.getMessageForView(tagToClazzMap.get(tag));
            presenter.receiveMessage(message);
            messagePool.pool(message);
        }
    }

    /**
     * Decrements {@link UserCounter} of all entities depended on this view. If the counter reaches to 0, destroys them calling {@code .onDestroy()}
     * @param viewObject
     * @see com.omerozer.knit.MemoryEntity
     */
    public void stopViewAndItsComponents(Object viewObject) {
        recurseTraverseTheGraphAndDestroyIfNeeded(clazzToTagMap.get(viewObject.getClass()));
    }

    private void recurseTraverseTheGraphAndDestroyIfNeeded(ComponentTag tag) {
        recurseForFinish(graphBase.get(tag));
    }


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


    private List<Class<?>> extractViews(List<Class<?>> views) {
        return views.subList(0, views.size() - 1);
    }


}
