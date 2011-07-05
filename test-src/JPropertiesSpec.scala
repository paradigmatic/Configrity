import org.scalatest.FlatSpec

import org.scalatest.matchers.ShouldMatchers
import configrity._
import configrity.JProperties
import configrity.io._
import java.util.Properties

class JPropertiesSpec extends FlatSpec with ShouldMatchers {

  val data = Map("foo"->"FOO", "bar"->"1234", "baz"->"on" )
  val config = Configuration( data )

  "A Configuration" can "be converted in java properties" in {
    val props: Properties = 
      JProperties.configurationToProperties( config )
    val config2: Configuration =
      JProperties.propertiesToConfiguration( props )
    config2 should be (config)
  }

  it can "be saved and read in java properties format" in {
    val fmt = JProperties.format
    val s = config format fmt
    val config2 = Configuration.parse( s, fmt )
    config2 should be (config)
  }

}
