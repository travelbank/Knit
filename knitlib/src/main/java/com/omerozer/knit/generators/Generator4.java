package com.omerozer.knit.generators;

import com.omerozer.knit.KnitResponse;

/**
 * Generator that accepts 4 parameter.
 *
 * @param <K> type of the {@link KnitResponse} body.
 *
 * @see ValueGenerator
 * @author Omer Ozer
 */

public interface Generator4<A,T,S,D,K> extends ValueGenerator {
    KnitResponse<A> generate(T param1, S param2,D param3,K param4);
}
