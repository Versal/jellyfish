organization := "com.versal"

name := "jellyfish"

version := "0.1.0"

scalaVersion := "2.10.1"

crossScalaVersions := Seq("2.9.0", "2.9.1", "2.9.2", "2.9.3", "2.10.0", "2.10.1")

autoCompilerPlugins := true

libraryDependencies <+= scalaVersion { v => compilerPlugin("org.scala-lang.plugins" % "continuations" % v) }

scalacOptions += "-P:continuations:enable"

scalacOptions += "-deprecation"

scalacOptions += "-feature"

libraryDependencies += "org.scalatest" %% "scalatest" % "1.9.1" % "test"

