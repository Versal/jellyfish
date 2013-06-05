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
    Return("foo is " + foo.x + ", bar is " + bar.x)
  }

}

object TypeCasingInterpreter {

  import SimpleProgram.{Foo, Bar}

  val foo = Foo(42)
  val bar = Bar("baz")

  def run[A](p: Program[A]): A = p match {
    case Return(a) => a
    case With(c, f) if c.isA[Foo] => run(f(foo))
    case With(c, f) if c.isA[Bar] => run(f(bar))
  }
}

object DependencyMapInterpreter {

  import SimpleProgram.{Foo, Bar}

  val foo = Foo(42)
  val bar = Bar("baz")

  val deps: Map[Class[_], Any] = Map(classOf[Foo] -> foo, classOf[Bar] -> bar)

  def run[A](p: Program[A]): A = p match {
    case Return(a) => a
    case With(c, f) => run(f(deps(c)))
  }
}

class SimpleTest extends FunSuite {

  test("simpleProgram : TypeCasingInterpreter") {
    val program: Program[String] = SimpleProgram.simpleProgram
    val result: String = TypeCasingInterpreter.run(program)
    assert("foo is 42, bar is baz" === result)
  }

  test("simpleProgram : DependencyMapInterpreter") {
    val program: Program[String] = SimpleProgram.simpleProgram
    val result: String = TypeCasingInterpreter.run(program)
    assert("foo is 42, bar is baz" === result)
  }
}
