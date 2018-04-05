package com.omerozer.knit.schedulers.heavy;

import static com.omerozer.knit.schedulers.heavy.HeavyTaskScheduler.HEAVY_THREAD_NAME1;

/**
 * A {@link HeavyThread} inside the thread pool of {@link HeavyTaskScheduler}.
 * @author Omer Ozer
 */

public class HThread1 extends HeavyThread {

    public HThread1() {super(HEAVY_THREAD_NAME1);}
}
