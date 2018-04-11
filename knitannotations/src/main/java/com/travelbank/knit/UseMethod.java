package com.travelbank.knit;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Internal Use. Do not use in code.
 *
 * Knit will create InternalPresenters and tag their event methods with these so they can be rescanned and exposed.
 *
 * @author Omer Ozer
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.SOURCE)
public @interface UseMethod {
    /**
     * String event tag.
     */
    String value() default "";
}
