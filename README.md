# FJ—Functional Java—the obligatory tiny functional programming library for Java.

Defines functional interfaces for 0-ary, 1-ary and 2-ary functions: `F0`, `F1`, `F2`.

Operations on functions: are defined as "functions" in the `Core` interface:

* `identity`
* `compose`
* `memoize`
* `partial`

The `constantly` "function" is defined in each functional interface. Its variants couldn't be defined in the `Core` interface since its return type varies but its argument list is the same for each arity.

The library operates on "functions", where a function can be any of:

* any lambda:
  * the result of any lambda expression or statement
  * any method reference
* any object implementing one of the functional interfaces defined in FJ: `F0`, `F1`, `F2`

Because the library functions are themselves available as (static) method references, any function from the library itself, or returned by a function from the library, may be operated upon further.

This works because functions in the library are defined in terms of `F0`, `F1`, `F2` and the functions in the library are closed over those three interfaces plus lambdas.

Unfortunately, due to the way Java 8 works, the library will not work with objects implementing other functional interfaces (not defined in the library). This issue is not unique to FJ--`java.util.function` has the same issue. See `LambdaTest` for details. This constitutes a barrier to interoperability between functional Java libraries.

## Quickie Examples

From `CoreTest`:

Compose two functions:

```java
F1<Double, Integer> f = compose((x)->{return x + 1.0;}, (x)->{return x * 2;});
assertThat(f.apply(3), is(7.0));
```

Partial application:

```java
F1<Integer,Integer> f = partial( (x, y)->{return x + y;}, 2);
assertThat(f.apply(3), is(5));
```

Even more partial application:
```java
F0<Integer> f = partial( (x, y)->{return x + y;}, 3, 2);
assertThat(f.apply(), is(5));
```

And the obiligatory (recursive) fibonacci function with memoization optimization from `FibTest.java`:

```java
public class FibTest {

    static F1<Integer,Integer> m_fib = memoize(FibTest::fib);
    static int fib(int n) {
        switch(n) {
            case 0: case 1: return n;
            default:        return m_fib.apply(n-1) + m_fib.apply(n-2);
        }
    }

    @Test
    public void fibTest() {
        // fib(50)
        assertThat(m_fib.apply(46),is(1836311903));
    }
}
```

## Motivation

Java 8 introduced lambdas, functional interfaces, method references and a library of ready-made functional interfaces in `java.util.function`. That package can be hard to understand all in one go. And while it may represent the definitive intent of the language designers with respect to functional programming, variations on that approach may prove useful, educational, or at least interesting.

FJ is just such a variation. It has two motivations:

1. serve as a minimal illustrative example of functional programming in Java
2. prefer a functional approach over an object-oriented approach

If the first goal is met, then FJ might serve as a tool for understanding `java.util.function`. FJ is small. A recent code count:

|                       | lines of code |
| ----------------------|---------------|
| implementation        | 74            |
| test (happy path only)| 74            |

The preference for a functional approach over an object-oriented one has given rise to some interesting outcomes in FJ so far:

* concision: fewer concepts ("verbs") for users to learn
* centralization of implementation (of functions) in the library itself
* functional invocation syntax

Compare the concepts or "verbs" that the user needs to understand:

| concept               | Java 8                          | FJ                                        |
| ----------------------|---------------------------------|-------------------------------------------|
| identity              | `identity` methods              | `identity`   "function" in `Core`         |
| functional composition| `compose` and `andThen` methods | `compose`    "function" in `Core`         |
| memoization           | no                              | `memoize`    "function" in `Core`         |
| partial application   | no                              | `partial`    "function" in `Core`         |
| `constantly` function | no                              | `constantly` "functions" in `F0`,`F1`,`F2`|

The various "functions" are actually static methods. This makes functional invocation syntax possible with FJ via static import of the `Core` interface.

Not only does this provide better alignment with what an experienced functional programmer would expect, it also leads to concision. For example, a single (family of) static `compose` methods on `Core` serves syntactically as a single `compose` "function". The user need learn only the single name versus two names in `java.util.function`: `compose` and `andThen`. Also the implementation of FJ `compose` is all in one file rather than spread out across dozens of interfaces. This centralization of implementation is also exemplified by the `identity` "function".

FJ is less broad than `java.util.function` though:

|                      | Java 8 | FJ  |
| ---------------------|--------|-----|
| fns on boxed types   | yes    | yes |
| fn arity 0,1,2       | yes    | yes |
| fns on unboxed types | yes    | no  |

Since FJ does not address unboxed types, you won't find equivalents of the various `DoubleXXX`, `IntXXX`, or `LongXXX` functions in FJ. Nor will you find boolean-valued functional interfaces like `Predicate` or `XXXPredicate` in FJ. All of these, could in concept, be added of course, but that isn't the point of FJ.

Also, whereas `java.util.function` parameterizes interfaces on the return value last, FJ parameterizes on the return value first.
 
|      Java 8        |    FJ       |
|--------------------|-------------|
| `Supplier<R>`      | `F0<R>`     |
| `Function<T,R>`    | `F1<R,T>`   |
| `BiFunction<T,U,R>`| `F2<R,T,U>` |

And actually, FJ uses `A`,`B`,`C` as the formal type parameters:

| formal type parameter | semantic meaning     |
|-----------------------|----------------------|
| `A`                   | return type          |
| `B`                   | first argument type  |
| `C`                   | second argument type |

## Examples

For usage examples, see `CoreTest`.

## Java Package

`com.thoughtpropulsion.fj`

## Maven

```
<dependency>
    <groupId>com.thoughtpropulsion</groupId>
    <artifactId>FJ</artifactId>
    <version>1.0-SNAPSHOT</version>
<dependency>
```

## Extras

The `LambdaTest` captures some assumptions about how lambdas, functional interfaces, and method references, work. Not only that, but it documents what I think is the most mysterious part, namely, that invocation of a lambda (or method reference) through a functional interface actually works, even though neither defines the (named) method of interest!

The original thinking that led to the library is captured in the `F00` interface and `F00Test`.