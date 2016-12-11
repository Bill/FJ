package com.thoughtpropulsion.fj;

import java.util.Objects;

/*
 * Works only for value types A, B e.g. Integer, String, Double
 */
class Pair<A,B> {
    public A a;
    public B b;
    public Pair(A a, B b) { this.a = a; this.b = b;}

    @Override
    public boolean equals(Object o) {
        Pair other = (Pair<A,B>)o;
        return this.a.equals(other.a) && this.b.equals(other.b);
    }
    @Override
    public int hashCode() {
        return Objects.hash(a, b);
    }
}
