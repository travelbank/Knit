package com.omerozer.knit.generators;

import com.omerozer.knit.KnitResponse;

/**
 * Generator that accepts no parameters. Mainly used as a getter.
 *
 * @param <K> type of the {@link KnitResponse} body.
 *
 * @see ValueGenerator
 * @author Omer Ozer
 */

public interface Generator0<K> extends ValueGenerator {
    KnitResponse<K> generate();
}
