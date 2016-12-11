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

For more usage examples, see `CoreTest`, `ClosedTest`.

## Motivation

Java 8 introduced lambdas, functional interfaces, method references and a library of ready-made functional interfaces in `java.util.function`. That package can be hard to understand all in one go. And while it may represent the definitive intent of the language designers with respect to functional programming, variations on that approach may prove useful, educational, or at least interesting.

FJ is just such a variation. Two constraints differentiate FJ from `java.util.function` and other Java functional programming libraries:

1. prefer a functional approach over an object-oriented approach
2. be tiny: implement only enough to illustrate feasibility

If FJ is small, it might serve as a tool for understanding other functional programming libraries like `java.util.function`.

FJ is small. A recent code count:

|                       | lines of code |
| ----------------------|---------------|
| implementation        | 74            |
| test (happy path only)| 73            |

The preference for a functional approach is exemplified in FP's complete avoidance of instance methods. Whereas `java.util.function` exposes all its behavior as instance methods, FP exposes all its behavior as static methods.

This approach has yielded some interesting results in FJ so far:

* concise interface: fewer concepts ("verbs") for users to learn
* cohesive implementation of each concept
* functional invocation syntax of `Core` "functions" via static import

This table summarizes the the concision (of use) and cohesiveness (of implementation) of the FJ appraoch versus `java.util.function`:

| concept               | Java 8 `java.util.function`            | FJ                                 |
| ----------------------|----------------------------------------|------------------------------------|
| identity              | `identity` methods in 5 interfaces     | 1 `identity` method  in `Core`     |
| functional composition| `compose` and `andThen` methods...     | 3 `compose`  methods in `Core`     |
|                       | ...in 4 and 10 interfaces respectively |                                    |
| memoization           | no                                     | 2 `memoize`  methods in `Core`     |
| partial application   | no                                     | 3 `partial`  methods in `Core`     |
| `constantly` function | no                                     | `constantly` methods...            |
|                       |                                        | ...in 3 interfaces: `F0`,`F1`,`F2` |

Users have one `compose` concept to learn versus 2 for `java.util.function` (`compose` and `andThen`).

Users have one place to go to learn about `compose` versus 14 for `java.util.function`. A user (or maintainer) seeking the to understand the implementation reads 3 methods, together in a single file versus 14 methods in 14 separate files. 

Users have one place to go to learn about `identity` (versus 5 places in `java.util.function`). A user (or maintainer) seeking the to understand the implementation reads one method versus 5 methods in 5 separate files.

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

## Gotchas

Java syntax for invoking a static method is concise. By statically importing `Core`, `compose()` can be invoked like this:

```java
identity(...)
```

Unfortunately, Java syntax for acquiring a method reference is not only verbose, but it requires the class name:
 
```java
compose( Core::identity,...)
```
 
In Lisps, these two kinds of use are unified syntactically:

```lisp
(identity ...)
(compose identity ...)
```

Java type erasure means you can't cast expressions to parameterized types. That, coupled with Java's limited type inference means that this statement:

```java
F1<Integer,Integer> h = compose(Core::memoize,Core::partial).apply((x,y)->{++counter; return x*y;}, 4);
```

Results in this compile error:

```
Error:(25, 40) java: incompatible types: cannot infer type-variable(s) A,B
      (argument mismatch; java.lang.Object cannot be converted to com.thoughtpropulsion.fj.F1<A,B>)
```

Since there was no way to cast the result of `compose()`, the statement had to be split in two:

```java
F2<F1<Integer,Integer>,F2<Integer,Integer,Integer>,Integer> g = compose(Core::memoize,Core::partial);
F1<Integer,Integer> h = g.apply((x,y)->{++counter; return x*y;}, 4);
```


## Extras

The `LambdaTest` captures some assumptions about how lambdas, functional interfaces, and method references, work.

It documents what I think is the most mysterious part, namely, that invocation of a lambda (or method reference) through a functional interface actually works, even though neither defines the (named) method of interest!

That would have seemed a lot less mysterious if Java provided a syntax for invoking through a functional interface, without specifying the method name. Proposals were discussed on the lambda-dev mailing list back in 2012 but none were adopted. See [f(x) syntax sugar in Lambda](http://mail.openjdk.java.net/pipermail/lambda-dev/2012-February/004518.html)

Also documented, is the unfortunate fact that not all functional interfaces are created equal. While you can pass any lambda to a method expecting a functional interface, you can't pass any object implementing any functional interface. If you pass a non-lambda, it has to implement the particular functional interface the method is declared to accept. This is a barrier to interoperability between functional libraries.

The original thinking that led to the FJ library is captured in the `F00` interface and `F00Test`.