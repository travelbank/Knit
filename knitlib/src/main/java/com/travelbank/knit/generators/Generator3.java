package com.travelbank.knit.generators;

import com.travelbank.knit.KnitResponse;

/**
 * Generator that accepts 3 parameter.
 *
 * @param <K> type of the {@link KnitResponse} body.
 *
 * @see ValueGenerator
 * @author Omer Ozer
 */

public interface Generator3<A,T,S,K> extends ValueGenerator {
    KnitResponse<A> generate(T param1, S param2,K param3);
}
