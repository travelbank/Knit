package com.omerozer.knit;

import java.util.concurrent.atomic.AtomicReference;

/**
 * Container class for all {@link MemoryEntity} instances registered to {@link com.omerozer.knit.components.graph.UsageGraph}.
 * Provides atomicity when nullifying an instance as it's being used by a Thread.
 *
 * @param <T> type of the {@link MemoryEntity} that is being stored.
 */

public class EntityInstance<T extends MemoryEntity> {

    /**
     * {@link AtomicReference} that holds the {@link MemoryEntity}
     */
    private AtomicReference<T> reference;

    /**
     * {@link Class} type of the entitiy being held.
     */
    private Class<? extends MemoryEntity> clazz;

    public EntityInstance(){
        this.reference = new AtomicReference<>();
    }

    /**
     * Nullifies the instance stored in {@link this#reference}.
     */
    public void nullify(){
        this.reference.set(null);
    }

    /**
     * Returns whether {@link this#reference} contains a null object.
     * @return whether {@link this#reference} contains a null object.
     */
    public boolean isAvailable(){
        return this.reference.get()!=null;
    }

    /**
     * Returns the instance contained by {@link this#reference}
     * @return
     */
    public T get() {
        return reference.get();
    }

    /**
     * Sets the instance contained inside {@link this#reference} and the class type of {@link this#clazz}.
     * @param memoryEntity
     */
    public void set(T memoryEntity){
        this.reference.set(memoryEntity);
        this.clazz = memoryEntity.getClass();
    }

    /**
     * Compares {@link this#clazz} to the given class.
     * Used inside generated {@link InternalModel}s to determine which {@link com.omerozer.knit.generators.ValueGenerator} needs to be accessed
     *
     * @param inc given class to compare {@link this#clazz} to.
     * @return Whether the class types match or not.
     */
    public boolean instanceOf(Class<?> inc){
        return clazz.equals(inc);
    }


}
