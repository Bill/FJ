package com.thoughtpropulsion.fj;

@FunctionalInterface
public interface F00<T> {

    T apply(final T x);

    static <T>    T  identity(final T x) { return x; }
    static <T> F00<T> constantly(final T x) { return (dont_care) -> x; }

    static <T> F00<T> compose(final F00<T> f, final F00<T> g) {
        return (T x) -> f.apply( g.apply(x) );
    }
}
