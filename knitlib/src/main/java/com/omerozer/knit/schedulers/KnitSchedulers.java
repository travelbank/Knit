package com.omerozer.knit.schedulers;

/**
 * Scheduler types that {@link com.omerozer.knit.Knit} supports.
 *
 * @author Omer Ozer
 */

public enum KnitSchedulers {

    /**
     * Represents {@link AndroidMainThreadScheduler}
     */
    MAIN("main()"),

    /**
     * Represents {@link ImmediateScheduler}
     */
    IMMEDIATE("immediate()"),

    /**
     * Represents {@link IOScheduler}
     */
    IO("io()"),

    /**
     * Represents {@link com.omerozer.knit.schedulers.heavy.HeavyTaskScheduler}
     */
    HEAVY("heavy()");

    /**
     * Method that represents the method that returns the associated {@link SchedulerInterface} from {@link Schedulers}.
     *
     * ToDo: remove
     */
    String method;

    KnitSchedulers(String method){
        this.method = method;
    }

    /**
     * Returns the method string
     * @return  Method that represents the method that returns the associated {@link SchedulerInterface} from {@link Schedulers}.
     */
    public String getMethod(){
        return this.method;
    }
}
