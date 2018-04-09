package com.omerozer.knit.generators;

import com.omerozer.knit.KnitResponse;

/**
 * Generator that accepts 1 parameter.
 *
 * @param <K> type of the {@link KnitResponse} body.
 *
 * @see ValueGenerator
 * @author Omer Ozer
 */

public interface Generator1<T,K> extends ValueGenerator {
    KnitResponse<T> generate(K param1);
}
