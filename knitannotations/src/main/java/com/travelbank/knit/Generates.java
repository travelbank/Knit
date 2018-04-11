package com.travelbank.knit;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *
 * All generators need to be annotated with this or {@link Collects} to be used with Knit.
 * @author Omer Ozer
 *
 */

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.SOURCE)
public @interface Generates {

    /**
     * Tag for the generated value.
     */
    String[] value() default "";
}
