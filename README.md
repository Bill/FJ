# FJ—Functional Java—the obligatory tiny functional programming library for Java.

Defines functional interfaces for 0-ary, 1-ary and 2-ary functions: `F0`, `F1`, `F2`.

Operations on functions: are defined as "functions" in the `Core` interface:

* `identity`
* `compose`
* `memoize`
* `partial`

The `constantly` "function" is defined in each functional interface. Its variants couldn't be defined in the `Core` interface since its return type varies but its argument list is the same for each arity.

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

The preference for a functional approach over an object-oriented one has given rise to a number of interesting outcomes in FJ so far:

|                       | Java 8                          | FJ                   |
| ----------------------|---------------------------------|----------------------|
| identity              | methods                         | `identity` "function"|
| functional composition| `compose` and `andThen` methods | `compose` "function" |
| memoization           | no                              | `memoize` "function" |
| partial application   | no                              | `partial` "function" |

The various "functions" are actually static methods defined on the `Core` interface. Most interesting is that a single (family of) `compose` methods on Core can serve syntactically as a single `compose` "function". This aligns better, with what an experienced functional programmer would expect.

FJ is less broad than `java.util.function` though:

|                      | Java 8 | FJ  |
| ---------------------|--------|-----|
| fns on boxed types   | yes    | yes |
| fn arity 0,1,2       | yes    | yes |
| fns on unboxed types | yes    | no  |

Since FJ does not address boxed types, you won't find equivalents of the various `DoubleXXX`, `IntXXX`, or `LongXXX` functions in FJ. Nor will you find boolean-valued functional interfaces like `Predicate` or `XXXPredicate` in FJ. All of these, could in concept, be added of course, but that isn't the point of FJ.

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