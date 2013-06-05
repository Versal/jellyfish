package com.versal.jellyfish

object `package` {

  import scala.util.continuations.cpsParam
  import scala.util.continuations.shift
  import scala.util.continuations.reset

  type program[A] = cpsParam[Program[A], Program[A]]

  def program[A](ctx: => Program[A] @program[Any]): Program[A] = reset(ctx).asInstanceOf[Program[A]]

  sealed trait Program[+A]
  case class Return[A](a: A) extends Program[A]
  case class With[A,B](c: Class[A], f: A => Program[B]) extends Program[B]

  def read[A](implicit mx: Manifest[A]) = shift { k: (A => Program[_]) =>
    With[A,Any](mx.erasure.asInstanceOf[Class[A]], k)
  }

  implicit def classy(x: Class[_]): Classy = new Classy(x)
}

class Classy(x: Class[_]) {
  def isA[A](implicit m: Manifest[A]): Boolean = {
    x.isAssignableFrom(m.erasure)
  }
}
