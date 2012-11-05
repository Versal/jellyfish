Jellyfish is a Scala library for dependency injection via delimited continuations.

# Example

First, write a program which retrieves dependencies via the `read` function:

```scala
case class Foo(x: Int)
case class Bar(x: String)

object SimpleProgram {

  import com.versal.jellyfish.{program, read, Return}

  // create a program with some dependencies
  val simpleProgram = program {
    val bar: Bar = read[Bar]  // retrieve the `Bar` dependency
    val foo: Foo = read[Foo]  // retrieve the `Foo` dependency
    Return("foo is " + foo.x + ", bar is " + bar.x)
  }

}
```

Second, write an interpreter provides the dependencies to the program:

```scala
object SimpleInterpreter {

  import com.versal.jellyfish.{classy, Program, Return, With}

  val foo = Foo(42)
  val bar = Bar("baz")

  // run a program, injecting dependencies as needed
  def run(p: Program): Any = p match {
    case With(c, f) if c.isA[Foo] => run(f(foo)) // inject the `Foo` dependency and continue
    case With(c, f) if c.isA[Bar] => run(f(bar)) // inject the `Bar` dependency and continue
    case Return(a)                => a           // all done - return the result
  }

}
```

Third, run the interpreter:

```scala
val result = SimpleInterpreter.run(SimpleProgram.simpleProgram)
println(result) // prints "foo is 42, bar is baz"
```

# How it works

A Jellyfish program is represented as an instance of the `Program` trait, which has two implementations:

```scala
case class Return(a: Any) extends Program
case class With[A](c: Class[A], f: A => Program) extends Program
```

The `read` function, which wraps Scala's `shift` function, takes a generic function of type `X => Program` and wraps it in a `With` which tracks the type of `X`.  This can happen an arbitrary number of times, resulting in a data structure analogous to a curried function.

This:

```scala
val bar: Bar = read[Bar]  // retrieve the `Bar` dependency
val foo: Foo = read[Foo]  // retrieve the `Foo` dependency
Return("foo is " + foo.x + ", bar is " + bar.x)
```

becomes:

```scala
bar: Bar => {
  val foo: Foo = read[Foo]  // retrieve the `Foo` dependency
  Return("foo is " + foo.x + ", bar is " + bar.x)
}
```

which becomes:

```scala
bar: Bar => {
  foo: Foo => {
    Return("foo is " + foo.x + ", bar is " + bar.x)
  }
}
```

which is a curried function with two dependencies.

An interpreter is then built to unwrap each nested `With`, extract the function of type `X => Program`, provide the appropriate instance of `X`, and continue until the program completes with a `Return`. 