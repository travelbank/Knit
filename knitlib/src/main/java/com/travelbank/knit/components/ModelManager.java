package com.travelbank.knit.components;

import com.travelbank.knit.EntityInstance;
import com.travelbank.knit.InternalModel;
import com.travelbank.knit.InternalPresenter;
import com.travelbank.knit.KnitModel;
import com.travelbank.knit.KnitResponse;
import com.travelbank.knit.components.graph.UsageGraph;
import com.travelbank.knit.schedulers.KnitSchedulers;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * This component manages all models. It holds {@link ComponentTag}s for all active models.
 * All presenters have an instance if this. When they make requests to a model data, it goes through
 * here . A model that generates the matching data is found then the {@link EntityInstance} of that presenter is passed
 * on to receive results.
 *
 * @see InternalPresenter
 * @see InternalModel
 *
 * @author Omer Ozer
 */

public class ModelManager extends InternalModel {

    /**
     * {@link UsageGraph} instance to fetch active model {@link ComponentTag}s.
     */
    private UsageGraph usageGraph;

    /**
     * {@link Map} that maps generated values to the {@link ComponentTag} of the {@link InternalModel} that generates it.
     */
    private Map<String, ComponentTag> valueToModelMap;

    public ModelManager() {
        this.valueToModelMap = new ConcurrentHashMap<>();
    }

    /**
     * Setter for {@link UsageGraph}
     * @param usageGraph {@link UsageGraph} instance to be set.
     */
    public void setUsageGraph(UsageGraph usageGraph) {
        this.usageGraph = usageGraph;
    }

    /**
     * Registers the {@link ComponentTag} of an {@link InternalModel} so that model becomes available
     * to all components.
     * @param componentTag Tag of the assoicated {@link InternalModel}.
     */
    public void registerModelComponentTag(ComponentTag componentTag) {
        for (String val : usageGraph.getModelWithTag(componentTag).get().getHandledValues()) {
            valueToModelMap.put(val, componentTag);
        }
        usageGraph.getModelWithTag(componentTag).get().getParent().setModelManager(this);
    }

    /**
     * Removes the {@link ComponentTag} of an {@link InternalModel} so that model is no-longer available.
     * @param componentTag Tag of the assoicated {@link InternalModel}
     */
    public void unregisterComponentTag(ComponentTag componentTag) {
        if(usageGraph.getModelWithTag(componentTag)==null){
            return;
        }
        for (String val : usageGraph.getModelWithTag(componentTag).get().getHandledValues()) {
            valueToModelMap.remove(val);
        }
    }

    /**
     * Returns the {@link InternalModel} that generates a particular data tag.
     * @param data Data dag that {@link InternalModel} is generating.
     * @return {@link InternalModel} that generates the given data tag.
     */
    public InternalModel getModelThatGeneratesData(String data){
        return usageGraph.getModelWithTag(valueToModelMap.get(data)).get();
    }

    /**
     * {@link InternalPresenter}s use this method when requesting data. This method finds the model {@link ComponentTag}
     * that generates the desired value , then gets that {@link InternalModel} instance from the {@link UsageGraph}
     * and calls the {@code .request(...)} method on it.
     *
     * @param data Requested data annotation tag
     * @param runOn {@link com.travelbank.knit.schedulers.Schedulers} to run the task on.
     * @param consumeOn {@link com.travelbank.knit.schedulers.Schedulers} to consume the task on.
     * @param presenterInstance {@link EntityInstance} of the {@link InternalPresenter}.
     * @param params Additional params that may be passed on.
     *
     * @see com.travelbank.knit.Presenter
     * @see com.travelbank.knit.KnitPresenter
     */
    @Override
    public void request(String data,KnitSchedulers runOn,KnitSchedulers consumeOn,EntityInstance<InternalPresenter> presenterInstance, Object... params) {
            if (valueToModelMap.containsKey(data)) {
                usageGraph.getModelWithTag(valueToModelMap.get(data)).get().request(data, runOn,consumeOn,presenterInstance, params);
                return;
            }
        throw new RuntimeException("Knit: No Model Exists that will input the given value: " + data);
    }

    /**
     * Non-Async version of {@code .request(...)} method. Used within other {@link InternalModel}s
     *
     * @param data Requested data annotation tag.
     * @param params Additional params that may be passed on.
     * @param <T> Type of the response body.
     * @return
     */
    @Override
    public <T> KnitResponse<T> requestImmediately(String data, Object... params) {
            if (valueToModelMap.containsKey(data)) {
                return usageGraph.getModelWithTag(valueToModelMap.get(data)).get().requestImmediately(data, params);
            }
        throw new RuntimeException("Knit: No Model Exists that generate the given value: " + data);
    }

    /**
     * Input method is used when simply inputting data into a model without the need of any async or non-async tasks.
     * Returns no response. Use it when inputting values that don't require error handling.
     * @param data Input data annotation tag.
     * @param params Additional params that may be passed on.
     */
    @Override
    public void input(String data, Object... params) {
            if (valueToModelMap.containsKey(data)) {
                usageGraph.getModelWithTag(valueToModelMap.get(data)).get().input(data, params);
                return;
            }
        throw new RuntimeException("Knit: No Model Exists that will input the given value: " + data);
    }


    /**
     * N/A
     * @return
     */
    @Override
    public KnitModel getParent() {
        return null;
    }

    /**
     * N/A
     * @return
     */
    @Override
    public String[] getHandledValues() {
        return new String[0];
    }
}
