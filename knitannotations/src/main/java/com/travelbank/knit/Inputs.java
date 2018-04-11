package com.travelbank.knit;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *
 * All inputters need to be annotated with this to be scanned by KnitProcessor and get registered into a Model Manager.
 *
 * @author Omer Ozer
 */

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.SOURCE)
public @interface Inputs {
    /**
     * Tag for the input value.
     */
    String[] value() default "";
}
