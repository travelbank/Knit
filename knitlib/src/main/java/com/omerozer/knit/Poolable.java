package com.omerozer.knit;

/**
 * Objects that can be re-used without being destroyed and recreated can implement Poolable.
 * See {@link MemoryPool}
 */

public interface Poolable {
    void recycle();
}
