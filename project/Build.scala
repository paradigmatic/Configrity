import sbt._
import Keys._

object ConfigrityBuild extends Build {

  lazy val configrity = Project(
    id = "configrity",
    base = file(".")
   ) settings (
    publish := { },  
    publishLocal := { }
  ) aggregate(core, yaml)


   lazy val core = Project(
     id = "configrity-core",
     base = file("core"),
     settings = standardSettings
   )

   lazy val yaml = Project(
     id = "configrity-yaml",
     base = file("modules/yaml"),
     dependencies = Seq(core),
     settings = standardSettings ++ Seq(
       libraryDependencies +=  "org.yaml" % "snakeyaml" % "1.9"
     )
   )


  lazy val standardSettings = Defaults.defaultSettings ++ Seq(
    organization := "org.streum",
    version := "0.10.0_BETA",
    scalaVersion := "2.9.1",
    crossScalaVersions := Seq("2.9.0-1", "2.9.1"),
    libraryDependencies += "org.scalatest" %% "scalatest" % "1.6.1" % "test",
    scalacOptions ++= Seq( "-deprecation", "-unchecked" ),
    scalaSource in Compile <<= baseDirectory(_ / "src"),
    scalaSource in Test <<= baseDirectory(_ / "test"),
    resourceDirectory in Test <<= baseDirectory { _ / "test-resources" },
    unmanagedClasspath in Compile += 
      Attributed.blank(new java.io.File("doesnotexist")),
    publishTo := Some("Scala Tools Nexus" at "http://nexus.scala-tools.org/content/repositories/releases/"),
    credentials += Credentials(Path.userHome / ".ivy2" / ".credentials"),
    pomExtra := licenseSection
    )

  lazy val licenseSection= {
    <licenses>
      <license>
        <name>GNU LesserGPLv3</name>
        <url>http://www.gnu.org/licenses/lgpl.html</url>
        <distribution>repo</distribution>
      </license>
    </licenses>
  }
}
