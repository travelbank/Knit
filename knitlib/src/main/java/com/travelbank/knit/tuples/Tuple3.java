package com.travelbank.knit.tuples;

import android.support.annotation.NonNull;

/**
 * {@inheritDoc}
 */

public class Tuple3<A,B,C> extends Tuple2<A,B> {

    /**
     * Third object instance
     */
    private C c;

    public Tuple3(@NonNull A a,@NonNull B b,@NonNull C c){
       super(a,b);
       this.c = c;
    }

    /**
     * Third object getter
     * @return returns the instance of the third object
     */
    public C getC() {
        return c;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        if (!super.equals(object)) return false;

        Tuple3<?, ?, ?> tuple3 = (Tuple3<?, ?, ?>) object;

        return c.equals(tuple3.c);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + c.hashCode();
        return result;
    }
}
