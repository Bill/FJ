package com.thoughtpropulsion.fj;

import java.util.Objects;

/*
 * Works only for value types A, B e.g. Integer, String, Double
 */
class Pair<A,B> {
    private final A a;
    private final B b;
    Pair(final A a, final B b) { this.a = a; this.b = b;}

    @Override
    public boolean equals(final Object o) {
        final Pair<A,B> other = (Pair<A,B>)o;
        return Objects.equals(this.a,other.a) && Objects.equals(this.b,other.b);
    }
    @Override
    public int hashCode() {
        return Objects.hash(a, b);
    }
}
