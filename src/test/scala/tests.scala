package com.versal.jellyfish.tests

import org.scalatest.FunSuite
import scala.util.continuations.reset
import com.versal.jellyfish._

object SimpleProgram {

  case class Foo(x: Int)
  case class Bar(x: String)

  def simpleProgram = program {
    val bar: Bar = read[Bar]
    val foo: Foo = read[Foo]
    "foo is " + foo.x + ", bar is " + bar.x
  }

}

object SimpleInterpreter {

  import SimpleProgram.{Foo, Bar}

  val foo = Foo(42)
  val bar = Bar("baz")

  def run(p: Program): Any = p match {
    case Return(a) => a
    case With(c, f) if c.isA[Foo] => run(f(foo))
    case With(c, f) if c.isA[Bar] => run(f(bar))
  }

}

class SimpleTest extends FunSuite {

  test("simpleProgram") {
    val result = SimpleInterpreter.run(SimpleProgram.simpleProgram)
    assert("foo is 42, bar is baz" === result)
  }
}

