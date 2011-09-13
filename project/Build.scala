import sbt._
import Keys._

object ConfigrityBuild extends Build {

  lazy val configrity = Project(
    id = "configrity",
    base = file("."),
    settings = standardSettings,
    aggregate = Seq(core, hello)
  )

   lazy val core = Project(
     id = "configrity-core",
     base = file("core"),
     settings = standardSettings
   )

   lazy val hello = Project(
     id = "configrity-hello",
     base = file("modules/hello"),
     dependencies = Seq(core),
     settings = standardSettings 
   )


  lazy val standardSettings = Defaults.defaultSettings ++ Seq(
    organization := "org.streum",
    version := "0.8.0",
    scalaVersion := "2.9.1",
    crossScalaVersions := Seq("2.9.0-1", "2.9.1"),
    libraryDependencies += "org.scalatest" %% "scalatest" % "1.6.1" % "test",
    scalacOptions += "-deprecation",
    scalaSource in Compile <<= baseDirectory(_ / "src"),
    scalaSource in Test <<= baseDirectory(_ / "test")
    //publishSetting,
    //credentialsSetting,
    )
  /*
  lazy val publishSetting = publishTo <<= (version) {
    version: String =>
      def repo(name: String) = name at "http://nexus-direct.scala-tools.org/content/repositories/" + name
    val isSnapshot = version.trim.endsWith("SNAPSHOT")
    val repoName = if(isSnapshot) "snapshots" else "releases"
    Some(repo(repoName))
  }

  lazy val credentialsSetting = credentials += {
    Seq("build.publish.user", "build.publish.password").map(k => Option(System.getProperty(k))) match {
      case Seq(Some(user), Some(pass)) =>
        Credentials("Sonatype Nexus Repository Manager", "nexus-direct.scala-tools.org", user, pass)
      case _ =>
        Credentials(Path.userHome / ".ivy2" / ".credentials")
    }
  }
*/

}
