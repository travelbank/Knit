package com.travelbank.knit;

/**
 *
 * All KnitModels need to be annotated with this to be scanned and registered with Knit.
 *
 * @author Omer Ozer
 */

public @interface Model {
    InstanceType value() default InstanceType.IN_GRAPH;
}
