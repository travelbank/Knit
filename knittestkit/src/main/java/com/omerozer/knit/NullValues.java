package com.omerozer.knit;

import android.content.Intent;
import android.os.Bundle;

import com.omerozer.knit.schedulers.KnitSchedulers;
import com.omerozer.knit.viewevents.ViewEventEnv;
import com.omerozer.knit.viewevents.ViewEventPool;

/**
 * Created by omerozer on 2/19/18.
 */

public final class NullValues {

    public static final Object[] NULL_PARAMS = new Object[0];

    public static final EntityInstance<InternalModel> NULL_MODEL = new EntityInstance<>();

    public static final EntityInstance<InternalPresenter> NULL_PRESENTER = new EntityInstance<>();
}
