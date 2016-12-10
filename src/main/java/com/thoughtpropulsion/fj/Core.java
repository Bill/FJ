package com.thoughtpropulsion.fj;

import java.util.HashMap;

public interface Core {
    static <A> A             identity(A x) { return x; }

    /*
     * See functional interfaces F0,F1,F2 for constantly(). They had
     * to be defined in each functional interface instead of here because
     * the method varies only in its return type (not in its argument list)
     */

    static <A,B> F0<A>     compose(F1<A,B> f, F0<B> g) {
        return () -> f.apply( g.apply() );
    }
    static <A,B,C> F1<A,C> compose(F1<A,B> f, F1<B,C> g) {
        return (C x) -> f.apply( g.apply(x) );
    }
    static <A,B,C,D> F2<A,C,D> compose(F1<A,B> f, F2<B,C,D> g) {
        return (C x, D y) -> f.apply( g.apply(x,y) );
    }

    static <A,B>   F1<A,B> memoize(F1<A,B> f) {
        HashMap<B,A> memory = new HashMap<>();
        return (B key) -> {
            if(! memory.containsKey(key))
                memory.put(key,f.apply(key));
            return memory.get(key);
        };
    }
    static <A,B,C>   F2<A,B,C> memoize(F2<A,B,C> f) {
        HashMap<Pair<B,C>,A> memory = new HashMap<>();
        return (B x, C y) -> {
            Pair key = new Pair(x,y);
            if(! memory.containsKey(key))
                memory.put(key,f.apply(x,y));
            return memory.get(key);
        };
    }

    static <A,B>   F0<A>   partial( F1<A,B> f, B x) {
        return () -> f.apply(x);
    }
    static <A,B,C> F0<A>   partial( F2<A,B,C> f, B x, C y) {
        return () -> f.apply(x,y);
    }
    static <A,B,C> F1<A,B> partial( F2<A,B,C> f, C y) {
        return (B x) -> f.apply(x,y);
    }
}
