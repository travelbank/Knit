package com.omerozer.knit.schedulers;

/**
 *
 * This class is used in Models an anonymous inner classes and will handle the response delivery to
 * Presenters.
 *
 * @param <T> The type received from {@link com.omerozer.knit.KnitResponse}
 * @see com.omerozer.knit.InternalModel
 * @see com.omerozer.knit.InternalPresenter
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
