import sbt._
import reaktor.scct.ScctProject

class ConfigrityProject(info: ProjectInfo) extends DefaultProject(info)
with ScctProject {
  
  val scalatest = "org.scalatest" % "scalatest_2.9.0" % "1.6.1"

  override def managedStyle = ManagedStyle.Maven
  val publishTo = 
    "Scala Tools Nexus" at 
    "http://nexus.scala-tools.org/content/repositories/releases/"
  Credentials(Path.userHome / ".ivy2" / ".credentials", log)

  override def pomExtra =
    <licenses>
      <license>
        <name>GNU LesserGPLv3</name>
        <url>http://www.gnu.org/licenses/lgpl.html</url>
        <distribution>repo</distribution>
      </license>
    </licenses>

  override def mainScalaSourcePath = "src"
  override def mainResourcesPath = "resources"
        
  override def testScalaSourcePath = "test-src"
  override def testResourcesPath = "test-resources"


}
