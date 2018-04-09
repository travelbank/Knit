package com.omerozer.knit.schedulers;

import com.omerozer.knit.schedulers.heavy.HeavyTaskScheduler;

/**
 * @see SchedulerProvider
 * @author Omer Ozer
 */

public class Schedulers implements SchedulerProvider{

    /**
     * {@link KnitIOThreadPool} required by {@link IOScheduler}.
     */
    private KnitIOThreadPool ioThreadPool = new KnitIOThreadPool();

    /**
     * {@link KnitIOReceiverThread} required by {@link IOScheduler}.
     */
    private KnitIOReceiverThread ioReceiverThread = new KnitIOReceiverThread();

    /**
     * {@link MainHandlerSupplier} required by {@link AndroidMainThreadScheduler}.
     */
    private MainHandlerSupplier mainThreadSupplier = new MainHandlerSupplier();

    /**
     * @see SchedulerProvider
     */
    @Override
    public SchedulerInterface io(){
        return new IOScheduler(ioThreadPool,ioReceiverThread);
    }

    /**
     * @see SchedulerProvider
     */
    @Override
    public SchedulerInterface main(){
        return new AndroidMainThreadScheduler(mainThreadSupplier);
    }

    /**
     * @see SchedulerProvider
     */
    @Override
    public SchedulerInterface immediate(){
        return new ImmediateScheduler();
    }

    /**
     * @see SchedulerProvider
     */
    @Override
    public SchedulerInterface heavy(){
        return new HeavyTaskScheduler();
    }

    /**
     * @see SchedulerProvider
     */
    @Override
    public SchedulerInterface forType(KnitSchedulers type) {
        switch (type){
            case IO:
                return io();
            case MAIN:
                return main();
            case IMMEDIATE:
                return immediate();
            case HEAVY:
                return heavy();
        }
        return immediate();
    }
}
