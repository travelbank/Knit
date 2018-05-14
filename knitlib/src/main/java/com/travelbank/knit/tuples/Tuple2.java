package com.travelbank.knit.tuples;

import android.support.annotation.NonNull;

/**
 *
 * N-Tuple data container to wrap multiple object into one quickly.
 *
 * @param <A> Type of the first object.
 * @param <B> Type of the second object.
 *
 * @author Omer Ozer
 */

public class Tuple2<A,B> {

    /**
     * First object instance
     */
    private A a;

    /**
     * Second object instance
     */
    private B b;

    public Tuple2(@NonNull A a,@NonNull B b){
        this.a = a;
        this.b = b;
    }

    /**
     * First object getter
     * @return returns the instance of the first object
     */
    public A getA() {
        return a;
    }

    /**
     * Second object getter
     * @return returns the instance of the second object
     */
    public B getB() {
        return b;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;

        Tuple2<?, ?> tuple2 = (Tuple2<?, ?>) object;

        if (!a.equals(tuple2.a)) return false;
        return b.equals(tuple2.b);
    }

    @Override
    public int hashCode() {
        int result = a.hashCode();
        result = 31 * result + b.hashCode();
        return result;
    }


}
