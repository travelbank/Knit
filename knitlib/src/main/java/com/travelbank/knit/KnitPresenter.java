package com.travelbank.knit;

import android.content.Intent;

import com.travelbank.knit.schedulers.KnitSchedulers;

import java.lang.ref.WeakReference;

/**
 * Presenter classes that are exposed to the developer by Knit. These need to be annotated with
 * {@link Presenter} and a target view class to be registered with Knit.
 * All {@link KnitPresenter}s will have a {@link InternalPresenter} counterpart that is actually
 * used by Knit framework to manage these.
 * Views that are annotated with {@link KnitView} will automatically have their contracts created
 * by Knit. Those contracts should be be given to
 * this class. Contracts will be have all non-android, public & package-private methods of the
 * view exposed.
 *
 * @param <T> Contract type class generated by Knit.
 * @author Omer Ozer
 */


public abstract class KnitPresenter<T,I> implements PresenterInterface,MessageReceiver {

    /**
     * Shared {@link Knit} instance.
     */
    private Knit knitInstance;

    /**
     * Instance of the current active view.
     */
    private WeakReference<Object> viewObjectRef;

    /**
     * Shared {@link com.travelbank.knit.components.ModelManager} instance.
     */
    private InternalModel modelManager;

    /**
     * Shared {@link KnitNavigator} instance. All navigation should be handled using this via
     * {@link this#getNavigator()}.
     */
    private KnitNavigator navigator;

    /**
     * Contract instance.
     */
    private Object contract;

    private Object interactor;

    public void setKnit(Knit knit) {
        this.knitInstance = knit;
        this.modelManager = knit.getModelManager();
        this.navigator = knit.getNavigator();
    }

    /**
     * @see PresenterInterface
     */
    @Override
    public void onViewApplied(Object viewObject) {
        this.viewObjectRef = new WeakReference<>(viewObject);
    }

    /**
     * Returns the current view instance.
     *
     * @return Current view instance.
     */
    protected Object getView() {
        return viewObjectRef.get();
    }

    /**
     * Method call that handles how data requests to the model from the {@link KnitPresenter} are
     * done.
     *
     * @param data      the tag of the data that is requested.
     * @param runOn     {@link com.travelbank.knit.schedulers.SchedulerInterface} that this
     *                                                                           request task
     *                                                                           will be ran on.
     * @param consumeOn {@link com.travelbank.knit.schedulers.SchedulerInterface} that this
     *                                                                           request task
     *                                                                           will be consumed
     *                                                                           on.
     * @param params    Extra parameters being passed to the
     * {@link com.travelbank.knit.generators.ValueGenerator}
     */
    protected void request(String data, KnitSchedulers runOn, KnitSchedulers consumeOn, Object...
            params) {
        EntityInstance<InternalPresenter> instance = knitInstance.findPresenterForParent(this);
        modelManager.request(data, runOn, consumeOn, instance, params);
    }

    /**
     * Request method with no async support. Use this if you want to go around the event based
     * system. Discouraged!
     * No async support. Runs on the thread it is called on via a
     * {@link com.travelbank.knit.schedulers.ImmediateScheduler}.
     *
     * @param data   data the tag of the data that is requested.
     * @param params Extra parameters being passed to the
     * {@link com.travelbank.knit.generators.ValueGenerator}
     * @return {@link KnitResponse} being returned from the associated
     * {@link com.travelbank.knit.generators.ValueGenerator}.
     */
    protected void request(String data, Object... params) {
        EntityInstance<InternalPresenter> instance = knitInstance.findPresenterForParent(this);
        modelManager.request(data, KnitSchedulers.IMMEDIATE, KnitSchedulers.IMMEDIATE, instance,
                params);
    }

    /**
     * Request method with no async support. Use this if you want to go around the event based
     * system. Discouraged!
     * No async support. Runs on the thread it is called on.
     *
     * @param data   the tag of the data that is requested.
     * @param params Extra parameters being passed to the
     * {@link com.travelbank.knit.generators.ValueGenerator}
     * @return {@link KnitResponse} being returned from the associated
     * {@link com.travelbank.knit.generators.ValueGenerator}.
     */
    protected <A> KnitResponse<A> requestImmediately(String data, Object... params) {
        return (KnitResponse<A>) modelManager.requestImmediately(data, params);
    }

    /**
     * Used when simply inputting/storing data. Make sure your task doesn't need to return a
     * response. No async support.
     *
     * @param data   data the tag of the data that is being input.
     * {@link com.travelbank.knit.inputters.Inputter1} for it must be annotated with a
     * {@link Inputs} with the value of this parameter.
     * @param params Extra parameters being passed to the
     * {@link com.travelbank.knit.inputters.Inputter1}.
     */
    protected void inputData(String data, Object... params) {
        modelManager.input(data, params);
    }

    /**
     * Casts then returns contract {@link this#contract} instance.
     *
     * @return Cast contract instance.
     */

    protected T getViewWrapper() {
        if(contract == null){
            contract = knitInstance.findPresenterForParent(this).get().getContract();
        }
        return (T) contract;
    }

    protected I getAccessor() {
        if (interactor == null) {
            interactor = knitInstance.findPresenterForParent(this).get().getInteractor();
        }
        return (I) interactor;
    }


    /**
     * Setter for {@link this#modelManager}
     *
     * @param modelManager {@link com.travelbank.knit.components.ModelManager} being set.
     */
    void setModelManager(InternalModel modelManager) {
        this.modelManager = modelManager;
    }

    /**
     * Setter for {@link this#navigator}.
     *
     * @param navigator {@link KnitNavigator} being set.
     */
    void setNavigator(KnitNavigator navigator) {
        this.navigator = navigator;
    }

    /**
     * Setter for {@link this#contract}
     *
     * @param contract Contract that's being set.
     */
    void setViewWrapper(Object contract) {
        this.contract = contract;
    }

    /**
     * Setter for {@link this#contract}
     *
     * @param accessor Contract that's being set.
     */
    void setAccessor(Object accessor) {
        this.interactor = accessor;
    }

    /**
     * Getter for {@link this#navigator}
     */
    protected KnitNavigator getNavigator() {
        return navigator;
    }

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
    public boolean shouldLoad() {
        return false;
    }

    /**
     * @see MemoryEntity
     */
    @Override
    public void onMemoryLow() {

    }

    public KnitPresenter() {
        super();
    }

    /**
     * @see NativeViewCallbacks
     */
    @Override
    public void onViewStart() {

    }

    /**
     * @see NativeViewCallbacks
     */
    @Override
    public void onViewResume() {

    }

    /**
     * @see NativeViewCallbacks
     */
    @Override
    public void onViewPause() {

    }

    /**
     * @see NativeViewCallbacks
     */
    @Override
    public void onViewStop() {

    }

    /**
     * @see PresenterInterface
     */
    @Override
    public void onCurrentViewReleased() {
        contract = null;
    }

    /**
     * @see NativeViewCallbacks
     */
    @Override
    public void onReturnToView() {

    }

    /**
     * @see NativeViewCallbacks
     */
    @Override
    public void onViewResult(int requestCode, int resultCode, Intent data) {

    }

    /**
     * Method that fetches a blank {@link KnitMessage} fromm {@link MemoryPool} inside {@link Knit}
     *
     * @return Blank {@link KnitMessage} instance.
     */
    protected KnitMessage newMessage() {
        return knitInstance.getMessagePool().getObject();
    }


    /**
     * @see MessageReceiver
     */
    @Override
    public void receiveMessage(KnitMessage message) {

    }

}
