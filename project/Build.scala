import sbt._
import Keys._

import sbtscalashim.Plugin._

import com.typesafe.tools.mima.plugin.MimaPlugin.mimaDefaultSettings
import com.typesafe.tools.mima.plugin.MimaKeys.previousArtifact

object ConfigrityBuild extends Build {

  lazy val configrity = Project(
    id = "configrity",
    base = file("."),
    settings = rootSettings
   ).aggregate(core, yaml)


   lazy val core = Project(
     id = "configrity-core",
     base = file("core"),
     settings = 
       standardSettings ++ 
       publishSettings ++ 
       scalaShimSettings ++
       mimaDefaultSettings ++
       Seq( 
	 sourceGenerators in Compile <+= scalaShim,
	 previousArtifact := Some("org.streum" % "configrity-core_2.9.1" % "0.10.2")
       ) 
   )

   lazy val yaml = Project(
     id = "configrity-yaml",
     base = file("modules/yaml"),
     dependencies = Seq(core),
     settings = standardSettings ++ publishSettings ++ Seq(
       libraryDependencies +=  "org.yaml" % "snakeyaml" % "1.9"
     )
   )

  lazy val minimalSettings = Defaults.defaultSettings ++ Seq(
    organization := "org.streum",
    version := "0.10.2",
    scalaVersion := "2.10.0",
    crossScalaVersions := Seq("2.9.2", "2.10.0" )
  )

  lazy val rootSettings = minimalSettings ++ Seq(
    publish := { },  
    publishLocal := { }
  )


  lazy val standardSettings = minimalSettings ++  Seq(
    libraryDependencies += "org.scalatest" %% "scalatest" % "1.9.1" % "test",
    scalacOptions ++= Seq( "-deprecation", "-unchecked" ),
    scalaSource in Compile <<= baseDirectory(_ / "src"),
    scalaSource in Test <<= baseDirectory(_ / "test"),
    resourceDirectory in Test <<= baseDirectory { _ / "test-resources" },
    unmanagedClasspath in Compile += 
      Attributed.blank(new java.io.File("doesnotexist"))
    ) ++ net.virtualvoid.sbt.graph.Plugin.graphSettings


  lazy val publishSettings = Seq(
    publishMavenStyle := true,
    publishTo <<= version { (v: String) =>
      val nexus = "https://oss.sonatype.org/"
      if (v.trim.endsWith("SNAPSHOT")) 
	Some("snapshots" at nexus + "content/repositories/snapshots") 
      else
	Some("releases"  at nexus + "service/local/staging/deploy/maven2")
    },
    publishArtifact in Test := false,
    pomIncludeRepository := { x => false },
    pomExtra := requiredPOMextra    
  )

  lazy val requiredPOMextra = {
    <url>https://github.com/paradigmatic/Configrity</url>
    <licenses>
      <license>
        <name>GNU LesserGPLv3</name>
        <url>http://www.gnu.org/licenses/lgpl.html</url>
        <distribution>repo</distribution>
      </license>
    </licenses>
    <scm>
    <url>https://github.com/paradigmatic/Configrity</url>
    <connection>scm:git:git@github.com:paradigmatic/Configrity.git</connection>
    </scm>
    <developers>
    <developer>
    <id>jlfalcone</id>
    <name>Jean-Luc Falcone</name>
    <url>http://paradigmatic.streum.org/</url>
    </developer>
    </developers>
  }

}
