package com.thoughtpropulsion.fj;

@FunctionalInterface
public interface F1<A, B> {
    A apply(final B x);

    static <A,B> F1<A,B> constantly(final A x) { return _dont_care -> x; }
}
