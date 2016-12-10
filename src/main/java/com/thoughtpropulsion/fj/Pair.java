package com.thoughtpropulsion.fj;

import java.util.Objects;

/*
 * Works only for value types A, B e.g. Integer, String, Double
 */
public class Pair<A,B> {
    public A a;
    public B b;
    public Pair(A a, B b) { this.a = a; this.b = b;}

    @Override
    public boolean equals(Object o) {
//        if (!(this.getClass().isAssignableFrom(o.getClass())))
//            return false;
        Pair other = (Pair<A,B>)o;
        return this.a.equals(other.a) && this.b.equals(other.b);
    }
    @Override
    public int hashCode() {
        return Objects.hash(a, b);
    }
}
