package com.travelbank.knit;

import java.util.Stack;

/**
 * A pool of objects that can be reset instead of being destroyed and recreated to reduce
 * allocation overhead. When an object is needed it will only be created if the pool is empty.
 * Objects that are no longer needed are reset and returned to the pool of available objects.
 *
 * @author Vincent Williams
 */

public abstract class MemoryPool<T extends Poolable> {
    private static final int MAX = 5;

    /**
     * Pool of available objects
     */
    Stack<T> objects;

    public MemoryPool() {
        objects = new Stack<>();
    }

    /**
     * Returns an object from the pool, or creates a new one if the pool is empty
     *
     * @return Poolable object.
     */
    public T getObject() {
        if (objects.isEmpty()) {
            return createNewInstance();
        }
        return objects.pop();
    }

    /**
     * Recycle an object and send it back to the pool
     *
     * @param object Object to be pooled
     */
    public void pool(T object) {
        object.recycle();
        if (objects.size() < getMaxPoolSize()) {
            objects.push(object);
        }
    }

    /**
     * @return New poolable object
     */
    protected abstract T createNewInstance();


    /**
     * @return Number of available objects in the pool
     */
    public int availableObjects() {
        return objects.size();
    }

    /**
     * @return Max pool capacity
     */
    protected int getMaxPoolSize() {
        return MAX;
    }
}
