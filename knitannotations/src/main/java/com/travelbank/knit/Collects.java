package com.travelbank.knit;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Should be used on Generators that require other models.
 * Umbrella Models .
 *
 * @author Omer Ozer
 */


@Target(ElementType.FIELD)
@Retention(RetentionPolicy.SOURCE)
public @interface Collects {

    /**
     * Tag for the generated value.
     */
    String[] value() default "";

    /**
     * Tag for the values that needed. This helps KnitProcessor assign Umbrella Model dependencies accordingly.
     */
    String[] needs() default "";
}
