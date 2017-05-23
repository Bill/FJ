package com.thoughtpropulsion.fj;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import org.junit.Test;

public class F00Test {

    @Test
    public void identityTest() {
        assertThat(F00.identity(1), is(1));
    }

    @Test
    public void constantlyTest() {
        assertThat(F00.constantly(42).apply(100), is(42));
    }

    @Test
    public void composeTest() {
        final F00<Integer> f = F00.compose((x)->{return x + 1;}, (x)->{return x * 2;});
        assertThat(f.apply(3), is(7));
    }

    @Test
    public void composeIdentityConstantlyTest() {
        final F00<Integer> f = F00.compose(F00::identity, F00.constantly(5));
        assertThat( f.apply(3), is(5));
    }
}
