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

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        if (!super.equals(object)) return false;

        Tuple5<?, ?, ?, ?, ?> tuple5 = (Tuple5<?, ?, ?, ?, ?>) object;

        return e.equals(tuple5.e);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + e.hashCode();
        return result;
    }
}
