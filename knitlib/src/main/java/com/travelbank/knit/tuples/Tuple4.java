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

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        if (!super.equals(object)) return false;

        Tuple4<?, ?, ?, ?> tuple4 = (Tuple4<?, ?, ?, ?>) object;

        return d.equals(tuple4.d);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + d.hashCode();
        return result;
    }
}
