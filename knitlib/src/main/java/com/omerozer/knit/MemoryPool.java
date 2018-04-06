package com.omerozer.knit;

import java.util.Stack;

/**
 * Created by omerozer on 3/16/18.
 */

public abstract class MemoryPool<T extends Poolable> {
    private static final int MAX = 5;

    Stack<T> events;

    public MemoryPool(){
        events = new Stack<>();
    }

    public T getObject(){
        if(events.isEmpty()){
            return createNewInstance();
        }
        return events.pop();
    }

    public void pool(T event){
        event.recycle();
        if(events.size()< getMaxPoolSize()){
            events.push(event);
        }
    }

    protected abstract T createNewInstance();

    public int availableObjects(){
        return events.size();
    }

    protected int getMaxPoolSize(){
        return MAX;
    }
}
