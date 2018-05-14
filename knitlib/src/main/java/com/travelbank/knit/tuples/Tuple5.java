package com.travelbank.knit.tuples;

import android.support.annotation.NonNull;

/**
 * {@inheritDoc}
 */
public class Tuple5<A,B,C,D,E> extends Tuple4<A,B,C,D> {

    /**
     * Fifth object instance
     */
    private E e;

    public Tuple5(@NonNull A a, @NonNull B b,
            @NonNull C c, @NonNull D d, @NonNull E e) {
        super(a, b, c,d);
        this.e = e;
    }

    /**
     * Fifth object getter
     * @return returns the instance of the fifth object
     */
    public E getE() {
        return e;
    }
}
