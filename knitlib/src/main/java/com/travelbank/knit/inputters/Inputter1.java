package com.travelbank.knit.inputters;

/**
 * These are inputter classes that generate no response. They should be used as setters. They provide no async services.
 * To register them with Knit , annotate them with {@link com.travelbank.knit.Inputs}.
 *
 * Inputter that accepts 1 parameter.
 *
 * @author Omer Ozer
 */

public interface Inputter1<A> {
    void input(A param1);
}
