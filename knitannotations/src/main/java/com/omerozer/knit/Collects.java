package com.omerozer.knit;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by omerozer on 2/14/18.
 */



@Target(ElementType.FIELD)
@Retention(RetentionPolicy.SOURCE)
public @interface Collects {
    String[] value() default "";
    String[] needs() default "";
}
