package com.omerozer.knit.schedulers.heavy;

import static com.omerozer.knit.schedulers.heavy.HeavyTaskScheduler.HEAVY_THREAD_NAME2;

/**
 * A {@link HeavyThread} inside the thread pool of {@link HeavyTaskScheduler}.
 * @author Omer Ozer
 */

public class HThread2 extends HeavyThread {

    public HThread2() {super(HEAVY_THREAD_NAME2);}
}
