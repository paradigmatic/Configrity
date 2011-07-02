import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers
import configrity.Configuration
import configrity.io._
import configrity.ValueConverters._


class ConfigurationSpec extends FlatSpec with ShouldMatchers{

  val data = Map("foo"->"FOO", "bar"->"1234", "baz"->"on" )
  val config = Configuration( data )

  "A configuration" should "return none if it doesn't contain a key" in {
    config[Int]("buzz") should be (None)
  }

  it should "be able to return converted values" in {
    config[String]("foo") should be (Some("FOO"))
    config[Int]("bar") should be (Some(1234))
    config[Boolean]("baz") should be (Some(true))
  }

  it should "return a default value when asked" in {
    config[String]("foo", "HELLO") should be ("FOO")
    config[Int]("bar", 0) should be (1234)
    config[Boolean]("baz", false) should be (true)
    config[Int]("buzz", 12) should be (12)
  }

  it should "be able to add new key" in {
    val c2 = config.set("buzz", 0.5)
    c2[Double]("buzz") should be (Some(0.5))
    config[Double]("buzz") should be (None)
  }

  it should "be able to replace existing key" in {
    val c2 = config.set("foo", 0.5)
    c2[Double]("foo") should be (Some(0.5))
    config[String]("foo") should be (Some("FOO"))
  }

  it should "be able to remove an exitsing key" in {
    val c2 = config.clear("foo")
    config[String]("foo") should be (Some("FOO"))
    c2[String]("foo") should be (None)
  }
  
  it should "not complain when trying to remove an inexistant key" in {
    val c2 = config.clear("buzz")
    c2 should be (config)
  }

  it can "be nicely formatted" in {
    val out = new ExportFormat {
      def toText( c: Configuration ) = "FOOBAR"
    }
    config.format(out) should be ("FOOBAR")
  }
  
  it can "be saved to a file" in {
    val file1 = "/tmp/configrity_configuration_spec_1.conf"
    val file2 = "/tmp/configrity_configuration_spec_2.conf"

    try {
      config.save( file1 )
      config.save( file2, FlatFormat )
      val config2 = Configuration.load( file1 )
      val config3 = Configuration.load( file2 )
      config2 should be (config)
      config3 should be (config)
    } finally {
      ( new java.io.File(file1) ).delete
      ( new java.io.File(file2) ).delete
    }
    
  }



}

class ConfigurationObjectSpec extends FlatSpec with ShouldMatchers{

  "A configuration" can "be created from the system properties" in {
    val config = Configuration.systemProperties
    config[String]("line.separator") should be ('defined)
  }

  it can "be created from environement variables" in {
    val config = Configuration.environment
    config[String]("HOME") should be ('defined)
  }

  it can "be created from a string using a given format" in {
    val s = 
      """
      foo = true
      bar = 2
      baz = hello world
      """
    val config = Configuration.parse( s, FlatFormat )
    config[Boolean]("foo") should be (Some(true))
    config[Int]("bar") should be (Some(2))
    config[String]("baz") should be (Some("hello world"))
  }

  it can "be loaded from a file" in {
    val filename = "/tmp/configrity_configuration_obj_spec.conf"
    val fmt = FlatFormat
    val s = 
      """
      foo = true
      bar = 2
      baz = hello world
      """
    val writer = new java.io.PrintWriter( filename )
    writer.println(s)
    writer.close()
    try {
      val config = Configuration.load(filename,fmt)
      config[Boolean]("foo") should be (Some(true))
      config[Int]("bar") should be (Some(2))
      config[String]("baz") should be (Some("hello world"))
      val config2 = Configuration.load(filename)
      config2 should be (config)
    } finally {
      ( new java.io.File(filename) ).delete
    }
  }


}

