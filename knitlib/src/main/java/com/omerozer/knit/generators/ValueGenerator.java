package com.omerozer.knit.generators;

/**
 *
 * Base class for all value generators used by {@link com.omerozer.knit.KnitModel}s. These need to be annotated with {@link com.omerozer.knit.Model} to be registered with Knit.
 * They accept params and in return , return a {@link com.omerozer.knit.KnitResponse} with either a body with the required data or an error.
 *
 * Currently Knit supports Generators that accept up tp 4 parameters. If more needed , consider wrapping them up in wrapper objects.
 *
 * @author Omer Ozer
 */

interface ValueGenerator {}
