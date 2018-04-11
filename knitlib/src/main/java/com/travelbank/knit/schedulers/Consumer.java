package com.travelbank.knit.schedulers;

/**
 *
 * This class is used in Models an anonymous inner classes and will handle the response delivery to
 * Presenters.
 *
 * @param <T> The type received from {@link com.travelbank.knit.KnitResponse}
 * @see com.travelbank.knit.InternalModel
 * @see com.travelbank.knit.InternalPresenter
 *
 * @author Omer Ozer
 */

public interface Consumer<T> {

    /**
     * Callback that consumes the result of the task ran inside a {@link SchedulerInterface}
     * @param t type of the result
     */
    void consume(T t);


    /**
     * Callback that is called if the task errors.
     * @param throwable
     */
    void error(Throwable throwable);
}
