package com.thoughtpropulsion.fj;

@FunctionalInterface
public interface F2<A, B, C> {
    A apply(final B x, final C y);

    static <A,B,C> F2<A,B,C> constantly(final A x) { return (_dont_care,_really_dont_care) -> x; }
}
