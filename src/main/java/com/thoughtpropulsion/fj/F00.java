package com.thoughtpropulsion.fj;

@FunctionalInterface
public interface F00<T> {

    T apply(T x);

    static <T>    T  identity(  T x) { return x; }
    static <T> F00<T> constantly(T x) { return (dont_care) -> x; }

    static <T> F00<T> compose(F00<T> f, F00<T> g) {
        return (T x) -> f.apply( g.apply(x) );
    }
}
