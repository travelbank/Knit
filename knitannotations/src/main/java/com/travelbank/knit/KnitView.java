package com.travelbank.knit;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *
 * All views such as Activities and Fragments need to be tagged with this to be registered with Knit.
 *
 * @author Omer Ozer
 */

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.SOURCE)
public @interface KnitView {}
