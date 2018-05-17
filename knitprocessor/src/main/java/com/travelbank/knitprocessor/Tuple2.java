package com.travelbank.knitprocessor;



public class Tuple2<A,B> {


    private A a;


    private B b;

    public Tuple2(A a,B b){
        this.a = a;
        this.b = b;
    }


    public A getA() {
        return a;
    }



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
