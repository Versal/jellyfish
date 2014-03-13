package com.versal

package object jellyfish {

  import scala.util.continuations.cps
  import scala.util.continuations.shift
  import scala.util.continuations.reset
  import scala.language.implicitConversions

  type program = cps[Program]

  def program[A](ctx: => A @program)(implicit ev: A => Program): Program =
    reset(ev(ctx))

  sealed trait Program
  object Program {
    implicit def toReturn(a: Any): Program = Return(a)
  }
  case class Return(a: Any) extends Program
  case class With[A](c: Class[A], f: A => Program) extends Program

  def read[A](implicit mx: Manifest[A]): A @program = shift { k: (A => Program) =>
    With(mx.runtimeClass.asInstanceOf[Class[A]], k)
  }

  implicit def classy(x: Class[_]): Classy = new Classy(x)
}

class Classy(x: Class[_]) {
  def isA[A](implicit m: Manifest[A]): Boolean =
    x.isAssignableFrom(m.runtimeClass)
}

