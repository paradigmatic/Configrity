import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers
import configrity.Configuration
import configrity.ValueConverters._


class ConfigurationSpec extends FlatSpec with ShouldMatchers{

  val data = Map("foo"->"FOO", "bar"->"1234", "baz"->"on" )
  val config = Configuration( data )

  "A configuration" should "return none if it doesn't contain a key" in {
    config.get[Int]("buzz") should be (None)
  }

  it should "be able to return converted values" in {
    config.get[String]("foo") should be (Some("FOO"))
    config.get[Int]("bar") should be (Some(1234))
    config.get[Boolean]("baz") should be (Some(true))
  }

  it should "return a default value when asked" in {
    config.get[String]("foo", "HELLO") should be ("FOO")
    config.get[Int]("bar", 0) should be (1234)
    config.get[Boolean]("baz", false) should be (true)
    config.get[Int]("buzz", 12) should be (12)
  }

  it should "be able to add new key" in {
    val c2 = config.set("buzz", 0.5)
    c2.get[Double]("buzz") should be (Some(0.5))
    config.get[Double]("buzz") should be (None)
  }

  it should "be able to replace existing key" in {
    val c2 = config.set("foo", 0.5)
    c2.get[Double]("foo") should be (Some(0.5))
    config.get[String]("foo") should be (Some("FOO"))
  }

  it should "be able to remove an exitsing key" in {
    val c2 = config.clear("foo")
    config.get[String]("foo") should be (Some("FOO"))
    c2.get[String]("foo") should be (None)
  }
  
  it should "not complain when trying to remove an inexistant key" in {
    val c2 = config.clear("buzz")
    c2 should be (config)
  }

}
