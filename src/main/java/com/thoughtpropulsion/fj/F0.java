package com.thoughtpropulsion.fj;

@FunctionalInterface
public interface F0<A> {
    A apply();

    static <A> F0<A> constantly(final A x) { return () -> x; }
}
