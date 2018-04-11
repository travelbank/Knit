package com.travelbank.knit;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Internal Use. Do not use in code.
 *
 * Knit will create InternalPresenters and tag them with this to rescan them to generate methods to be used/exposed.
 *
 * @author Omer Ozer
 */

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.SOURCE)
public @interface Use {
}
