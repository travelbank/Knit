package com.travelbank.knit;

import com.travelbank.knit.schedulers.KnitSchedulers;

/**
 *
 * This class is the master class of {@link KnitModel} that is exposed to the user. Knit's annotation processor creates
 * these {@link InternalModel}s that initialize and contain the actual {@link KnitModel}. All {@link KnitModel}s annotated with a {@link Model}
 * will have an internal counterpart that manages them. The master class created by the processor will have a name "ClassName" + "_Model".
 * Actual {@link KnitModel} instance will be wrapped around an Exposer class that will expose all of it's public and package-private methods
 * to the {@link InternalModel} . Instance of these, once initialized, are registered to the {@link com.travelbank.knit.components.ModelManager}.
 *
 * @see Model
 * @see KnitModel
 *
 * @author Omer Ozer
 */

public abstract class InternalModel implements ModelInterface {

    /**
     * Method call that handles how data requests to the model from the {@link KnitPresenter} are done.
     *
     * @param data the tag of the data that is requested. {@link com.travelbank.knit.generators.ValueGenerator} for it must be annotated with a {@link Generates} with the value of this parameter.
     * @param runOn {@link com.travelbank.knit.schedulers.SchedulerInterface} that this request task will be ran on.
     * @param consumeOn {@link com.travelbank.knit.schedulers.SchedulerInterface} that this request task will be consumed on.
     * @param presenterInstance {@link InternalModel} instance that is making the request call.
     * @param params Extra parameters being passed to the {@link com.travelbank.knit.generators.ValueGenerator}
     */
    public abstract void request(String data,KnitSchedulers runOn,KnitSchedulers consumeOn,EntityInstance<InternalPresenter> presenterInstance, Object... params);

    /**
     * Method call that handles how data requests to the model from the {@link KnitPresenter} and other models are done.
     * No async support. Runs on the thread it is called on via a {@link com.travelbank.knit.schedulers.ImmediateScheduler}.
     * Use these in umbrella models.
     *
     * @param data data the tag of the data that is requested. {@link com.travelbank.knit.generators.ValueGenerator} for it must be annotated with a {@link Generates} with the value of this parameter.
     * @param params Extra parameters being passed to the {@link com.travelbank.knit.generators.ValueGenerator}
     * @param <T> type of the response body.
     * @return {@link KnitResponse} being returned from the associated {@link com.travelbank.knit.generators.ValueGenerator}.
     */
    public abstract <T>KnitResponse<T> requestImmediately(String data, Object... params);

    /**
     * Used when simply inputting/storing data. Make sure your task doesn't need to return a response. No async support.
     * If you need to return a response and want your task to run on a {@link com.travelbank.knit.schedulers.SchedulerInterface}, use {@link this#request(String, KnitSchedulers, KnitSchedulers, EntityInstance, Object...)}
     * and return {@code true} or {@code false} depending on how input task went.
     *
     * @param data data the tag of the data that is being input. {@link com.travelbank.knit.inputters.Inputter1} for it must be annotated with a {@link Inputs} with the value of this parameter.
     * @param params Extra parameters being passed to the {@link com.travelbank.knit.inputters.Inputter1}.
     */
    public abstract void input(String data, Object... params);

    /**
     * Returns parent {@link KnitModel} that is exposed to the user.
     * @return parent {@link KnitModel} that is exposed to the user.
     */
    public abstract KnitModel getParent();


    /**
     * An array of Strings that contain the tags for the values that are generated by the {@link com.travelbank.knit.generators.ValueGenerator}s of of this model.
     * @return An array of Strings that contain the tags for the values that are generated by the {@link com.travelbank.knit.generators.ValueGenerator}s of of this model.
     */
    public abstract String[] getHandledValues();

    /**
     * @see MemoryEntity
     */
    @Override
    public void onCreate() {

    }

    /**
     * @see MemoryEntity
     */
    @Override
    public void onLoad() {

    }

    /**
     * @see MemoryEntity
     */
    @Override
    public void onDestroy() {

    }

    /**
     * @see MemoryEntity
     */
    @Override
    public void onMemoryLow() {

    }

    /**
     * @see MemoryEntity
     */
    @Override
    public boolean shouldLoad() {
        return false;
    }

}