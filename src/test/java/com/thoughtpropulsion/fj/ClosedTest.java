package com.thoughtpropulsion.fj;

// By importing this way, FJ functions look syntactically like functions where they're used below
import static com.thoughtpropulsion.fj.Core.*;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import org.junit.Test;

/*
 * Verify that the functions in Core can be used as arguments to the functions in Core
 */
public class ClosedTest {

    @Test
    public void composeIdentityConstantlyTest() {
        // Kinda sad we have to qualify identity here but that's the Java syntax for a method ref
        // we needn't qualify compose because it is not a method ref: we are invoking the method
        F1<Integer, Integer> f = compose(Core::identity, F1.constantly(5));
        assertThat( f.apply(3), is(5));
    }

    /*
     * compose(memoize, partial) returns a fn of two args: a fn and some arg to partially apply
     * we then apply the lambda function (of two args) and the value 4 to bind to the second arg,
     * producing a fn of one arg that implements a memoized x*4
     */
    static int counter = 0;
    @Test
    public void composeMemoizePartialTest() {
    counter = 0;
        /*
         * FIXME: the definition of h (as a single statement) won't compile:
         * Error:(25, 40) java: incompatible types: cannot infer type-variable(s) A,B
         *      (argument mismatch; java.lang.Object cannot be converted to com.thoughtpropulsion.fj.F1<A,B>)
         * F1<Integer,Integer> h = compose(Core::memoize,Core::partial).apply((x,y)->{++counter; return x*y;}, 4);
         *
         * But breaking it into two statements allows Java to understand it.
         * I don't know any Java syntax that'll let me tell the compiler the type of the intermediate expression
         * short of just breaking it into two separate expressions:
         */
        F2<F1<Integer,Integer>,F2<Integer,Integer,Integer>,Integer> g = compose(Core::memoize,Core::partial);
        F1<Integer,Integer> h = g.apply((x,y)->{++counter; return x*y;}, 4);
        assertThat(h.apply(3), is(12));
        assertThat(h.apply(3), is(12));
        assertThat(h.apply(4), is(16));
        assertThat(counter, is(2));
    }
}
