organization := "org.streum"

name := "configrity"

version := "0.9.0"

scalaVersion := "2.9.1"

crossScalaVersions := Seq("2.9.0-1", "2.9.1")

libraryDependencies += "org.scalatest" %% "scalatest" % "1.6.1" % "test"

scalacOptions += "-deprecation"

scalaSource in Compile <<= baseDirectory(_ / "src")

scalaSource in Test <<= baseDirectory(_ / "test")

resourceDirectory in Test <<= baseDirectory { _ / "test-resources" }

unmanagedClasspath in Compile += Attributed.blank(new java.io.File("doesnotexist"))

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
