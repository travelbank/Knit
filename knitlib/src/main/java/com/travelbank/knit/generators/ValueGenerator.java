package com.travelbank.knit.generators;

/**
 *
 * Base class for all value generators used by {@link com.travelbank.knit.KnitModel}s. These need to be annotated with {@link com.travelbank.knit.Model} to be registered with Knit.
 * They accept params and in return , return a {@link com.travelbank.knit.KnitResponse} with either a body with the required data or an error.
 *
 * Currently Knit supports Generators that accept up tp 4 parameters. If more needed , consider wrapping them up in wrapper objects.
 *
 * @author Omer Ozer
 */

interface ValueGenerator {}
