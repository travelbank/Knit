package com.omerozer.knit.components.graph;

/**
 *
 * Counts the number of times a {@link com.omerozer.knit.MemoryEntity} is referenced.
 *
 * @see com.omerozer.knit.MemoryEntity
 * @author Omer Ozer
 */

public class UserCounter {

    private int count = 0;

    /**
     * Increments the count by 1
     */
    public void use() {
        count++;
    }

    /**
     * Decrements the count by 1. Flooring at 0.
     */
    public void release() {
        count = count > 0 ? count-1 : 0;
    }

    /**
     * Returns the number of references
     * @return returns the number of current references
     */
    public int getCount(){
        return count;
    }


    /**
     * Returns true if counter is greater than 0. Used by {@link UsageGraph} to determine whether the associated {@link com.omerozer.knit.MemoryEntity} should be killed or not.
     * @return Returns true if counter is greater than 0. Otherwise false.
     */
    public boolean isUsed() {
        return count > 0;
    }

}
