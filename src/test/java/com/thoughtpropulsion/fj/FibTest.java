package com.thoughtpropulsion.fj;

// By importing this way, FJ functions look syntactically like functions where they're used below
import static com.thoughtpropulsion.fj.Core.*;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import org.junit.Test;

public class FibTest {

    static final F1<Integer,Integer> m_fib = memoize(FibTest::fib);
    static int fib(final int n) {
        switch(n) {
            case 0: case 1: return n;
            default:        return m_fib.apply(n-1) + m_fib.apply(n-2);
        }
    }

    @Test
    public void fibTest() {
        assertThat(m_fib.apply(46),is(1836311903));
    }
}
