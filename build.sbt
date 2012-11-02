organization := "com.versal"

name := "jellyfish"

version := "0.1-SNAPSHOT"

scalaVersion := "2.9.2"

autoCompilerPlugins := true

addCompilerPlugin("org.scala-lang.plugins" % "continuations" % "2.9.2")

scalacOptions += "-P:continuations:enable"

libraryDependencies += "org.scalatest" %% "scalatest" % "2.0.M4" % "test"
