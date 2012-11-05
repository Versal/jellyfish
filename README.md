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
    case Return(a) => a
    case With(c, f) if c.isA[Foo] => run(f(foo)) // inject the `Foo` dependency
    case With(c, f) if c.isA[Bar] => run(f(bar)) // inject the `Bar` dependency
  }

}
```

Third, run the interpreter:

```scala
val result = SimpleInterpreter.run(SimpleProgram.simpleProgram)
println(result) // prints "foo is 42, bar is baz"
```
