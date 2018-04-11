package com.travelbank.knit;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *
 * Methods that listen to events fired from Models need to be annotated with these.
 *
 * @author Omer Ozer
 */

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.SOURCE)
public @interface ModelEvent {

    /**
     * Event that is being listened to.
     */
    String value();
}
