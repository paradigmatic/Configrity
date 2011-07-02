import org.scalatest.FlatSpec

import org.scalatest.matchers.ShouldMatchers
import configrity.Configuration
import configrity.JProperties
import configrity.io._
import configrity.ValueConverters._
import java.util.Properties

class PropertiesSpec extends FlatSpec with ShouldMatchers {

  val data = Map("foo"->"FOO", "bar"->"1234", "baz"->"on" )
  val config = Configuration( data )

  "A Configuration" can "be converted in java properties" in {
    val props: Properties = 
      JProperties.configurationToProperties( config )
    val config2: Configuration =
      JProperties.propertiesToConfiguration( props )
    config2 should be (config)

  }

}
