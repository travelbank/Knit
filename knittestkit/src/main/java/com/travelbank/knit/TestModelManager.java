package com.travelbank.knit;

import com.travelbank.knit.classloaders.KnitModelLoader;
import com.travelbank.knit.components.ModelManager;
import com.travelbank.knit.schedulers.KnitSchedulers;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by omerozer on 2/20/18.
 */

public final class TestModelManager extends ModelManager {

    private Map<String,InternalModel> dataToModelMap = new HashMap<>();
    private KnitModelLoader modelLoader;
    private ModelMapInterface modelMapInterface;

    public TestModelManager(KnitInterface knitInterface){
        this.dataToModelMap = new HashMap<>();
        this.modelLoader = new KnitModelLoader(knitInterface);
        this.modelMapInterface = knitInterface.getModelMap();
    }

    KnitModel registerModel(Class<? extends KnitModel> modelClazz){
        InternalModel internalModel = modelLoader.loadModel(modelLoader.getModelForModel(modelClazz));
        registerInternalModel(internalModel);
        internalModel.getParent().setModelManager(this);
        return internalModel.getParent();
    }


    InternalModel registerModelMock(Class<? extends KnitModel> modelClazz,Mocker mocker){
        Class<? extends InternalModel> internalClazz = modelMapInterface.getModelClassForModel(modelClazz);
        InternalModel internalModel = mocker.mock(internalClazz);
        registerInternalModel(internalModel);
        return internalModel;
    }

    private void registerInternalModel(InternalModel internalModel){
        for(String val: internalModel.getHandledValues()){
            dataToModelMap.put(val,internalModel);
        }
    }


    @Override
    public void input(String data, Object... params) {
        if(dataToModelMap.containsKey(data)){
            dataToModelMap.get(data).input(data,params);
        }
    }

    @Override
    public void request(String data, KnitSchedulers runOn, KnitSchedulers consumeOn,
            EntityInstance<InternalPresenter> instance, Object... params) {
        if(dataToModelMap.containsKey(data)){
            dataToModelMap.get(data).request(data,runOn,consumeOn,instance,params);
        }
    }

    @Override
    public <T> KnitResponse<T> requestImmediately(String data, Object... params) {
        if(dataToModelMap.containsKey(data)){
            return dataToModelMap.get(data).requestImmediately(data,params);
        }
        return null;
    }
}
