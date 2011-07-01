import sbt._

class ConfigrityProject(info: ProjectInfo) extends DefaultProject(info)
{

  val scalatest = "org.scalatest" % "scalatest_2.9.0" % "1.6.1"
  
  override def mainScalaSourcePath = "src"
  override def mainResourcesPath = "resources"
        
  override def testScalaSourcePath = "test-src"
  override def testResourcesPath = "test-resources"


}
