Jellyfish is a Scala library for dependency injection via delimited continuations.

# Example

Write a program which retrieves dependencies via the `read` function:

```scala
object SimpleProgram {

  import com.versal.jellyfish._

  case class Foo(x: Int)
  case class Bar(x: String)

  def simpleProgram = program {
    val bar: Bar = read[Bar]
    val foo: Foo = read[Foo]
    Return("foo is " + foo.x + ", bar is " + bar.x)
  }

}
```

Write an interpreter provides the dependencies to the program:

```scala
object SimpleInterpreter {

  import com.versal.jellyfish._

  import SimpleProgram.{Foo, Bar}

  val foo = Foo(42)
  val bar = Bar("baz")

  def run(p: Program): Any = p match {
    case Return(a) => a
    case With(c, f) if c.isA[Foo] => run(f(foo))
    case With(c, f) if c.isA[Bar] => run(f(bar))
  }

}
```

Run the interpreter:

```scala
object SimpleExample extends App {

  val result = SimpleInterpreter.run(SimpleProgram.simpleProgram)

  println(result) // prints "foo is 42, bar is baz"

}
```
