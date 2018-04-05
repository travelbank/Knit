package com.omerozer.knit.schedulers.heavy;

import static com.omerozer.knit.schedulers.heavy.HeavyTaskScheduler.HEAVY_THREAD_NAME4;

/**
 * A {@link HeavyThread} inside the thread pool of {@link HeavyTaskScheduler}.
 * @author Omer Ozer
 */

public class HThread4 extends HeavyThread {

    public HThread4() {super(HEAVY_THREAD_NAME4);}
}
