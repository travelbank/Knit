package com.travelbank.knit;

/**
 * Created by omerozer on 3/20/18.
 */

public interface Mocker {
    InternalModel mock(Class<? extends InternalModel> internalModelClazz);
}
