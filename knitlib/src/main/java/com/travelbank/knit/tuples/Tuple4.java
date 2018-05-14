package com.travelbank.knit.tuples;

import android.support.annotation.NonNull;

/**
 * {@inheritDoc}
 */
public class Tuple4<A,B,C,D> extends Tuple3<A,B,C> {

    /**
     * Third object instance
     */
    private D d;

    public Tuple4(@NonNull A a, @NonNull B b,
            @NonNull C c, @NonNull D d) {
        super(a, b, c);
        this.d = d;
    }

    /**
     * Fourth object getter
     * @return returns the instance of the fourth object
     */
    public D getD() {
        return d;
    }
}
