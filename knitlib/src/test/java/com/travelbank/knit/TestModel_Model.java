package com.travelbank.knit;

import com.travelbank.knit.schedulers.KnitSchedulers;
import com.travelbank.knit.schedulers.SchedulerProvider;

/**
 * Created by omerozer on 3/8/18.
 */

public class TestModel_Model extends InternalModel {

    private SchedulerProvider schedulerProvider;

    public TestModel_Model(SchedulerProvider schedulerProvider){
        this.schedulerProvider = schedulerProvider;
    }

    public SchedulerProvider getSchedulerProvider() {
        return schedulerProvider;
    }


    @Override
    public void request(String data, KnitSchedulers runOn, KnitSchedulers consumeOn,
            EntityInstance<InternalPresenter> presenterInstance, Object... params) {

    }

    @Override
    public <T> KnitResponse<T> requestImmediately(String data, Object... params) {
        return null;
    }

    @Override
    public void input(String data, Object... params) {

    }

    @Override
    public KnitModel getParent() {
        return null;
    }

    @Override
    public String[] getHandledValues() {
        return new String[0];
    }

}
