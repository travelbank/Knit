package com.omerozer.knit;

import java.util.concurrent.atomic.AtomicReference;

/**
 * Created by omerozer on 4/4/18.
 */

public class EntityInstance<T extends MemoryEntity> {

    private AtomicReference<T> reference;
    private Class<? extends MemoryEntity> clazz;

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
        this.reference.set(memoryEntity);
        this.clazz = memoryEntity.getClass();
    }

    public boolean instanceOf(Class<?> inc){
        return clazz.equals(inc);
    }

}
