package com.versal.jellyfish

object `package` {

  import scala.util.continuations.cpsParam
  import scala.util.continuations.shift

  type program = cpsParam[Program, Program]

  trait Program
  case class Return(a: Any) extends Program
  case class With[A](c: Class[A], f: A => Program) extends Program

  def read[X](implicit mx: Manifest[X]): X @program = shift { k: (X => Program) =>
    val f = { x: X => k(x) }
    With(mx.erasure.asInstanceOf[Class[X]], k)
  }

  implicit def classy(x: Class[_]): Classy = new Classy(x)

}

class Classy(x: Class[_]) {
  def isA[A](implicit m: Manifest[A]): Boolean = {
    x.isAssignableFrom(m.erasure)
  }
}

