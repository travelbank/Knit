package com.omerozer.knit.components.graph;

import com.omerozer.knit.EntityInstance;
import com.omerozer.knit.InternalModel;
import com.omerozer.knit.InternalPresenter;
import com.omerozer.knit.KnitInterface;
import com.omerozer.knit.KnitMessage;
import com.omerozer.knit.MemoryEntity;
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
 * Created by omerozer on 2/16/18.
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

    public Collection<EntityInstance> activeEntities() {
        return instanceMap.values();
    }

    public EntityInstance<InternalModel> getModelWithTag(ComponentTag componentTag) {
        return instanceMap.get(componentTag);
    }

    public EntityInstance<InternalPresenter> getPresenterTag(ComponentTag componentTag) {
        return instanceMap.get(componentTag);
    }

    public EntityInstance getPresenterForObject(Object presenterObject) {
        return instanceMap.get(clazzToTagMap.get(
                viewToPresenterMap.getPresenterClassForPresenter(presenterObject.getClass())));
    }

    public EntityInstance getPresenterForView(Object viewObject) {
        return instanceMap.get(clazzToTagMap.get(
                viewToPresenterMap.getPresenterClassForView(viewObject.getClass())));
    }

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

    public void releaseViewFromComponent(Object viewObject) {
        if (!graphBase.containsKey(clazzToTagMap.get(viewObject.getClass()))) {
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
                    instanceMap.remove(entityNode.tag);
                    activeModelTags.remove(entityNode.tag);
                    modelManager.unregisterComponentTag(entityNode.tag);
                }
                break;
            case PRESENTER:
                if (!counterMap.get(entityNode.tag).isUsed()) {
                    instanceMap.get(entityNode.tag).get().onDestroy();
                    instanceMap.remove(entityNode.tag);
                    activePresenterTags.remove(entityNode.tag);
                }
                break;
        }


    }


    private List<Class<?>> extractViews(List<Class<?>> views) {
        return views.subList(0, views.size() - 1);
    }


}
