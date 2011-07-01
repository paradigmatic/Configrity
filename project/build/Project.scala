import sbt._
import reaktor.scct.ScctProject

class ConfigrityProject(info: ProjectInfo) extends DefaultProject(info)
with ScctProject {

  val scalatest = "org.scalatest" % "scalatest_2.9.0" % "1.6.1"
  
  override def mainScalaSourcePath = "src"
  override def mainResourcesPath = "resources"
        
  override def testScalaSourcePath = "test-src"
  override def testResourcesPath = "test-resources"


}
