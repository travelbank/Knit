package com.omerozer.knit;

import java.util.concurrent.atomic.AtomicReference;

/**
 * Created by omerozer on 4/4/18.
 */

public class EntityInstance<T extends MemoryEntity> {

    private AtomicReference<T> reference;

    public EntityInstance(){
        this.reference = new AtomicReference<>();
    }

    public void nullify(){
        this.reference.set(null);
    }

    public boolean isAvailable(){
        return this.reference.get()!=null;
    }

    public T get() {
        return reference.get();
    }

    public void set(T memoryEntity){
        reference.set(memoryEntity);
    }

}
