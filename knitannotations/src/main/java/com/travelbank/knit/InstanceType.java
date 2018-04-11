package com.travelbank.knit;

/**
 *
 * Instance type for {@link Model}. This determines their lifecycles.
 *
 * @author Omer Ozer
 */

public enum InstanceType {

    /**
     * Lifecycles tied to the usage graph. Will get initialized and destroyed as needed.
     */
    IN_GRAPH,

    /**
     * Singleton lifecylce. Once initiated, will always remain in the memory.
     */
    SINGLETON;
}
