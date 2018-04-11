package com.travelbank.knit.generators;

import com.travelbank.knit.KnitResponse;

/**
 * Generator that accepts 2 parameter.
 *
 * @param <K> type of the {@link KnitResponse} body.
 *
 * @see ValueGenerator
 * @author Omer Ozer
 */

public interface Generator2<A,T,K> extends ValueGenerator {
    KnitResponse<A> generate(T param1,K param2);
}
