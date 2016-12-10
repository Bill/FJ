package com.thoughtpropulsion.fj;

// By importing this way, FJ functions look syntactically like functions where they're used below
import static com.thoughtpropulsion.fj.Core.*;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import org.junit.Test;

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
        F0<Double> f = compose((x)->{return x + 1.0;}, ()->{return 2;});
        assertThat(f.apply(), is(3.0));
    }

    @Test
    public void composeF1Test() {
        F1<Double, Integer> f = compose((x)->{return x + 1.0;}, (x)->{return x * 2;});
        assertThat(f.apply(3), is(7.0));
    }

    @Test
    public void composeF2Test() {
        F2<Double,Integer,Integer> f = compose((x)->{return x + 1.0;}, (x, y)->{return x * y;});
        assertThat(f.apply(2,3), is(7.0));
    }

    @Test
    public void composeIdentityConstantlyTest() {
        // FIXME: shouldn't have to qualify identity fn here
        F1<Integer, Integer> f = compose(Core::identity, F1.constantly(5));
        assertThat( f.apply(3), is(5));
    }

    int counter=0;

    @Test
    public void memoizeF1Test() {
        counter = 0;
        F1<Integer,Integer> f = memoize((x)->{counter+=1; System.out.println("bump");return counter;});
        assertThat(f.apply(3),is(1));
        assertThat(f.apply(3),is(1));
    }

    int counter2=0;

    @Test
    public void memoizeF2Test() {
        counter2 = 0;
        F2<Integer,Integer,Integer> f = memoize((x, t)->{counter2+=1; System.out.println("bump");return counter2;});
        assertThat(f.apply(3, 4),is(1));
        assertThat(f.apply(3, 4),is(1));
    }

    @Test
    public void partialTest() {
        F0<Integer> f = partial( (x)->{return x+1;}, 1);
        assertThat(f.apply(), is(2));
    }

    @Test
    public void partialF1ATest() {
        F1<Integer,Integer> f = partial( (x, y)->{return x + y;}, 2);
        assertThat(f.apply(3), is(5));
    }

    @Test
    public void partialF1BTest() {
        F0<Integer> f = partial( (x, y)->{return x + y;}, 3, 2);
        assertThat(f.apply(), is(5));
    }
}
