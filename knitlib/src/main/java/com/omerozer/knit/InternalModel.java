package com.omerozer.knit;

import com.omerozer.knit.schedulers.KnitSchedulers;

/**
 * Created by omerozer on 2/3/18.
 */

public abstract class InternalModel implements ModelInterface {

    public abstract void request(String data,KnitSchedulers runOn,KnitSchedulers consumeOn,EntityInstance<InternalPresenter> presenterInstance, Object... params);

    public abstract <T>KnitResponse<T> requestImmediately(String data, Object... params);

    public abstract void input(String data, Object... params);

    public abstract KnitModel getParent();

    public abstract String[] getHandledValues();

    @Override
    public void onCreate() {

    }

    @Override
    public void onLoad() {

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public void onMemoryLow() {

    }

    @Override
    public boolean shouldLoad() {
        return false;
    }

}
