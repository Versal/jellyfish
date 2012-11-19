publishMavenStyle := true

publishTo <<= version { (v: String) =>
  val nexus = "https://oss.sonatype.org/"
  if (v.trim.endsWith("SNAPSHOT"))
    Some("snapshots" at nexus + "content/repositories/snapshots")
  else
    Some("releases"  at nexus + "service/local/staging/deploy/maven2")
}

pomIncludeRepository := { _ => false }

pomExtra := (
  <url>https://github.com/Versal/jellyfish</url>
  <licenses>
    <license>
      <name>BSD-style</name>
      <url>http://www.opensource.org/licenses/bsd-license.php</url>
      <distribution>repo</distribution>
    </license>
  </licenses>
  <scm>
    <url>git@github.com:Versal/jellyfish.git</url>
    <connection>scm:git:git@github.com:Versal/jellyfish.git</connection>
  </scm>
  <developers>
    <developer>
      <id>JamesEarlDouglas</id>
      <name>James Earl Douglas</name>
      <url>https://github.com/JamesEarlDouglas</url>
    </developer>
  </developers>)
