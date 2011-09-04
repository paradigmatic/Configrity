organization := "org.streum"

name := "configrity"

version := "0.8.0"

scalaVersion := "2.9.1"

crossScalaVersions := Seq("2.9.0-1", "2.9.1")

libraryDependencies += "org.scalatest" %% "scalatest" % "1.6.1"

scalacOptions += "-deprecation"

scalaSource in Compile <<= baseDirectory(_ / "src")

scalaSource in Test <<= baseDirectory(_ / "test")

publishTo := Some("Scala Tools Nexus" at "http://nexus.scala-tools.org/content/repositories/releases/")

credentials += Credentials(Path.userHome / ".ivy2" / ".credentials")

pomExtra :=
    <licenses>
      <license>
        <name>GNU LesserGPLv3</name>
        <url>http://www.gnu.org/licenses/lgpl.html</url>
        <distribution>repo</distribution>
      </license>
    </licenses>
