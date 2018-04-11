package com.travelbank.knit;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *
 * All Knit Presenters need to be tagged with this to be scanned and registered with Knit.
 *
 * @author Omer Ozer
 */

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.SOURCE)
public @interface Presenter {
    /**
     * {@link KnitView} this presenter ties to.
     */
    Class<?> value() default Object.class;

    /**
     * Events this presenter depends on. Helps Knit create dependency graph for this presenter.
     */
    String[] needs() default "";
}
