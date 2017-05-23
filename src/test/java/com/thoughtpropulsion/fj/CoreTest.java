package com.thoughtpropulsion.fj;

// By importing this way, FJ functions look syntactically like functions where they're used below
import static com.thoughtpropulsion.fj.Core.*;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import org.junit.Test;

/*
 * Happy-paths test of Core
 */
public class CoreTest {

    @Test
    public void identityTest() {
        assertThat(identity(1), is(1));
    }

    @Test
    public void constantlyF0Test() {
        assertThat(F0.constantly(42).apply(), is(42));
    }

    @Test
    public void constantlyF1Test() {
        assertThat(F1.constantly(42).apply(100), is(42));
    }

    @Test
    public void constantlyF2Test() {
        assertThat(F2.constantly(42).apply(100, 200), is(42));
    }

    @Test
    public void composeF0Test() {
        final F0<Double> f = compose((x)->{return x + 1.0;}, ()->{return 2;});
        assertThat(f.apply(), is(3.0));
    }

    @Test
    public void composeF1Test() {
        final F1<Double, Integer> f = compose((x)->{return x + 1.0;}, (x)->{return x * 2;});
        assertThat(f.apply(3), is(7.0));
    }

    @Test
    public void composeF2Test() {
        final F2<Double,Integer,Integer> f = compose((x)->{return x + 1.0;}, (x, y)->{return x * y;});
        assertThat(f.apply(2,3), is(7.0));
    }

    int counter=0;

    @Test
    public void memoizeF1Test() {
        counter = 0;
        final F1<Integer,Integer> f = memoize((x)->{++counter; return x;});
        assertThat(f.apply(3),is(3));
        assertThat(f.apply(3),is(3));
        assertThat(f.apply(4),is(4));
        assertThat(counter,is(2));
    }

    int counter2=0;

    @Test
    public void memoizeF2Test() {
        counter2 = 0;
        final F2<Integer,Integer,Integer> f = memoize((x, y)->{++counter2; return x*y;});
        assertThat(f.apply(3, 4),is(12));
        assertThat(f.apply(3, 4),is(12));
        assertThat(f.apply(4, 4),is(16));
        assertThat(counter2,is(2));
    }

    @Test
    public void partialTest() {
        final F0<Integer> f = partial( (x)->{return x+1;}, 1);
        assertThat(f.apply(), is(2));
    }

    @Test
    public void partialF1ATest() {
        final F1<Integer,Integer> f = partial( (x, y)->{return x + y;}, 2);
        assertThat(f.apply(3), is(5));
    }

    @Test
    public void partialF1BTest() {
        final F0<Integer> f = partial( (x, y)->{return x + y;}, 3, 2);
        assertThat(f.apply(), is(5));
    }
}
