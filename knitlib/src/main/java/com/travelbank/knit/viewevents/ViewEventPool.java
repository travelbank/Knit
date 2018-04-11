package com.travelbank.knit.viewevents;

import com.travelbank.knit.MemoryPool;

/**
 *
 * Base {@link MemoryPool} class for {@link com.travelbank.knit.ViewEvents}
 *
 * @param <T> {@link ViewEventEnv} type this pool supports.
 *
 * @author Omer Ozer
 */

public abstract class ViewEventPool<T extends ViewEventEnv> extends MemoryPool<T> {

    /**
     * Default maximum number of items to be pooled.
     */
    private static final int MAX = 5;

    /**
     * Return maximum number of items to be pooled.
     * @return max number of items to be pooled.
     * @see MemoryPool
     */
    @Override
    protected int getMaxPoolSize(){
        return MAX;
    }

}
