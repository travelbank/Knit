package com.omerozer.knit.inputters;

/**
 * These are inputter classes that generate no response. They should be used as setters. They provide no async services.
 * To register them with Knit , annotate them with {@link com.omerozer.knit.Inputs}.
 *
 * Inputter that accepts 4 parameter.
 *
 * @author Omer Ozer
 */

public interface Inputter4<A,T,S,D> {
    void input(A param1,T param2,S param3,D param4);
}
