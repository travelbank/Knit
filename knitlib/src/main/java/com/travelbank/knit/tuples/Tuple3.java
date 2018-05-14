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


}
