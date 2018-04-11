package com.travelbank.knit.components.graph;

/**
 * {@link UserCounter} implementation for Singleton Entities
 *
 * @author Omer Ozer
 * @see com.travelbank.knit.InstanceType
 * @see com.travelbank.knit.Model
 */
public class SingletonUserCounter extends UserCounter {

    private boolean isUsed = false;

    /**
     * Instead of incrementing the count, uses a flag instead. Once {@link UserCounter#use()} is called,  {@code boolean isUsed} turns {@code true} and is never released afterwards
     * How many usages it has does not matter since it is a 'Singleton' and won't be released for GC clean up.
     */
    @Override
    public void use() {
        super.use();
        isUsed = true;
    }

    /**
     * Doesn't do anything since {@code boolean isUsed} flag never turns {@code false}
     */
    @Override
    public void release() {

    }

    /**
     * Always returns zero since a singleton can only have a single instance.
     * @return the number of instances available.
     */
    @Override
    public int getCount() {
        return 1;
    }

    /**
     * Returns the value of {@code boolean isUsed}. Mocks lazy instantiation. {@code false} until the first time it used. Then {@code true}.
     * @return Whether this singleton is initialized or not
     */
    @Override
    public boolean isUsed() {
        return isUsed;
    }
}
