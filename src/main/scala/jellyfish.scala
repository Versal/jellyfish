package com.versal.jellyfish

object `package` {

  import scala.util.continuations.cpsParam
  import scala.util.continuations.shift
  import scala.util.continuations.reset

  type program[A] = cpsParam[Program[A], Program[A]]

  def program[A](ctx: => Program[A] @program[A]): Program[A] = reset[Program[A], Program[A]](ctx)

  sealed trait Program[A]
  case class Return[A](a: A) extends Program[A]
  case class With[A,B](c: Class[A], f: A => Program[B]) extends Program[B]

  def read[A,B](implicit mx: Manifest[A]): A @program[B] = shift { k: (A => Program[B]) =>
    With[A,B](mx.erasure.asInstanceOf[Class[A]], k).asInstanceOf[Program[B]]
  }

  implicit def classy(x: Class[_]): Classy = new Classy(x)
}

class Classy(x: Class[_]) {
  def isA[A](implicit m: Manifest[A]): Boolean = {
    x.isAssignableFrom(m.erasure)
  }
}
