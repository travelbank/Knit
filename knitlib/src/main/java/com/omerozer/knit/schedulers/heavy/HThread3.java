package com.omerozer.knit.schedulers.heavy;

import static com.omerozer.knit.schedulers.heavy.HeavyTaskScheduler.HEAVY_THREAD_NAME3;

/**
 * A {@link HeavyThread} inside the thread pool of {@link HeavyTaskScheduler}.
 * @author Omer Ozer
 */

public class HThread3 extends HeavyThread {

    public HThread3() {super(HEAVY_THREAD_NAME3);}
}
