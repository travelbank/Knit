package com.travelbank.knit.schedulers;

/**
 * Scheduler types that {@link com.travelbank.knit.Knit} supports.
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
     * Represents {@link com.travelbank.knit.schedulers.heavy.HeavyTaskScheduler}
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
