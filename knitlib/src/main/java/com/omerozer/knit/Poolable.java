package com.omerozer.knit;

/**
 * Objects that can be re-used without being destroyed and recreated can implement Poolable.
 * See {@link MemoryPool}
 *
 * @author Omer Ozer
 */

public interface Poolable {

    /**
     * This method handles how each {@link Poolable} object is recycled.
     */
    void recycle();
}
