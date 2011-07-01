import sbt._
class Plugins(info: ProjectInfo) extends PluginDefinition(info) {
  val scctRepo = "scct-repo" at "http://mtkopone.github.com/scct/maven-repo/"
  lazy val scctPlugin = "reaktor" % "scct-sbt-for-2.9" % "0.1-SNAPSHOT"
}			
