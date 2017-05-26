package com.thoughtpropulsion.fj;

import java.util.HashMap;

public interface Core {
    static <A> A identity(final A x) { return x; }

    /*
     * See functional interfaces F0,F1,F2 for constantly(). They had
     * to be defined in each functional interface instead of here because
     * the method varies only in its return type (not in its argument list)
     */

    static <A,B> F0<A>         compose(final F1<A,B> f, final F0<B>     g) {
        return () -> f.apply( g.apply() );
    }
    static <A,B,C> F1<A,C>     compose(final F1<A,B> f, final F1<B,C>   g) {
        return x -> f.apply( g.apply(x) );
    }
    static <A,B,C,D> F2<A,C,D> compose(final F1<A,B> f, final F2<B,C,D> g) {
        return (x, y) -> f.apply( g.apply(x,y) );
    }

    static <A,B,C> F2<A,B,C> memoize(final F2<A,B,C> f) {
        final HashMap<Pair<B,C>,A> memory = new HashMap<>();
        return (B x, C y) -> {
            final Pair<B,C> key = new Pair<>(x,y);
            if(! memory.containsKey(key))
                memory.put(key,f.apply(x,y));
            return memory.get(key);
        };
    }
    static <A,B> F1<A,B>     memoize(final F1<A,B>   f) {
        // define one-arg memoization i.t.o. two-arg memoization
        final F2<A,B,Object> m = memoize((x, _dont_care) -> f.apply(x));
        return x -> m.apply(x, null);
    }
    static <A> F0<A>         memoize(final F0<A>     f) {
        // define zero-arg memoization i.t.o. one-arg memoization
        final F1<A,Object> m = memoize((_dont_care) -> f.apply());
        return () -> m.apply(null);
    }

    static <A,B>   F0<A>   partial( final F1<A,B>   f, final B x) {
        return () -> f.apply(x);
    }
    static <A,B,C> F0<A>   partial( final F2<A,B,C> f, final B x, final C y) {
        return () -> f.apply(x,y);
    }
    static <A,B,C> F1<A,B> partial( final F2<A,B,C> f, final C y) {
        return x -> f.apply(x,y);
    }
}
