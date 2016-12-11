package com.thoughtpropulsion.fj;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import org.junit.Test;

public class LambdaTest {

    @FunctionalInterface
    interface FI1 {
        int scooby(int x);
    }

    static class FC1 implements FI1 {
        public int scooby(int x) { return x + 1;}
        public static int Scooby(int x) { return x + 2;}
    }

    static int callWith4(FI1 f) { return f.scooby(4);}

    @Test
    public void objectMethodRefLambdaEquivalenceTest() {
        // this makes perfect sense
        assertThat(callWith4(new FC1()), is(5));

        /*
         * These next two are surprising. They are both lambdas (ok).
         * What's surprising is callWith4() appears to invoke the "scooby"
         * method on each one in turn. Lambda's appear to respond to any
         * method invocation, regardless of name. That being the case
         * it seems there should be an invocation syntax that elides the name
         * like just f(4) instead of f.scooby(4).
         */
        assertThat(callWith4(FC1::Scooby), is(6));
        assertThat(callWith4((x) -> x + 3), is(7));
    }

    @FunctionalInterface
    interface FI2 {
        int shaggy(int x);
    }

    static class FC2 implements FI2 {
        public int shaggy(int x) { return x + 3;}
    }

    @Test
    public void incompatibleFunctionalInterfacesTest() {
        /*
         * But this doesn't compile. You might think that since FI2 is a functional
         * interface, it would be usable as an FI1, the way a lambda/method reference is.
         * But the compiler says FC2 cannot be converted to FI1.
         * This presents a barrier to combining multiple functional libraries.
         * It means if you have a lib that is defined i.t.o. some functional e.g. FI1,
         * you can pass lambdas to it, but you can't pass otherwise-compatible
         * objects implementing some other functional interface FI2.
         * Error:(58, 32) java: incompatible types: com.thoughtpropulsion.fj.LambdaTest.FC2 cannot be converted to com.thoughtpropulsion.fj.LambdaTest.FI1
         */
        //assertThat( callWith4( new FC2() ),   is(7));
    }
}