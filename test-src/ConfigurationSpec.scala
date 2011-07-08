import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers
import configrity.Configuration
import configrity.DefaultConverters
import configrity.io._


class ConfigurationSpec extends FlatSpec with ShouldMatchers with DefaultConverters{

  val data = Map("foo"->"FOO", "bar"->"1234", "baz"->"on" )
  val config = Configuration( data )

  "A configuration" should "return none if it doesn't contain a key" in {
    config.get[Int]("buzz") should be (None)
  }

  it should "be able to return converted values" in {
    config[String]("foo") should be ("FOO")
    config[Int]("bar") should be (1234)
    config[Boolean]("baz") should be (true)
  }

  it should "return a default value when asked" in {
    config[String]("foo", "HELLO") should be ("FOO")
    config[Int]("bar", 0) should be (1234)
    config[Boolean]("baz", false) should be (true)
    config[Int]("buzz", 12) should be (12)
  }

  it should "be able to add new key" in {
    val c2 = config.set("buzz", 0.5)
    c2[Double]("buzz") should be (0.5)
    config.get[Double]("buzz") should be (None)
  }

  it should "be able to replace existing key" in {
    val c2 = config.set("foo", 0.5)
    c2[Double]("foo") should be (0.5)
    config[String]("foo") should be ("FOO")
  }

  it should "be able to remove an exitsing key" in {
    val c2 = config.clear("foo")
    config[String]("foo") should be ("FOO")
    c2.get[String]("foo") should be (None)
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

  "A sub configuration" can "be attached at a given prefix" in {
    val data2 = Map( "one" -> "1", "two" -> "2" )
    val config2 = Configuration( data2 )
    val config3 = config attach ("nums", config2)
    config3[String]("foo") should be ("FOO")
    config3[Int]("nums.one") should be (1)
  }


  it can "will replace values when attached at an existing prefix" in {
    val data2 = Map( "one" -> "1", "two" -> "2" )
    val config2 = Configuration( data2 )
    val config3 = config attach ("nums", config2)
    val data4 = Map( "one" -> "I", "five" -> "V" )
    val config4 = Configuration( data4 )
    val config5 = config3 attach( "nums", config4 )
    config5[String]("foo") should be ("FOO")
    config5[String]("nums.one") should be ("I")
    config5[Int]("nums.two") should be (2)
    config5[String]("nums.five") should be ("V")
  }

  it can "be dettach from a configuration" in {
    val data2 = Map( "one" -> "1", "two" -> "2" )
    val config2 = Configuration( data2 )
    val config3 = config attach ("nums", config2 )
    val config4 = config3 dettach ("nums")
    config4 should be (config2)
  }

  it can "include another configuration" in {
    val config2 = Configuration( "foo" -> "fu", "buzz" -> 122 )
    val config3 = config include config2
    config3[String]("foo") should be ("FOO")
    config3[Int]("bar") should be (1234)
    config3[Int]("buzz") should be (122)
    (config2 include config) should not be (config include config2)
  }

}

class ConfigurationObjectSpec extends FlatSpec with ShouldMatchers{

  "A configuration" can "be created from the system properties" in {
    val config = Configuration.systemProperties
    config.get[String]("line.separator") should be ('defined)
  }

  it can "be created from environement variables" in {
    val config = Configuration.environment
    config.get[String]("HOME") should be ('defined)
  }

  it can "be created empty" in {
    val config = Configuration()
    config.data.size should be (0)
  }

  it can "be created with key value pairs" in {
    val config = Configuration("foo"->"bar", "bazz"->2)
    config[String]("foo") should be ("bar")
    config[Int]("bazz") should be (2)
  }


  it can "be created from a string using a given format" in {
    val s = 
      """
      foo = true
      bar = 2
      baz = "hello world"
      """
    val config = Configuration.parse( s, FlatFormat )
    config.get[Boolean]("foo") should be (Some(true))
    config.get[Int]("bar") should be (Some(2))
    config.get[String]("baz") should be (Some("hello world"))
  }

  it can "be loaded from a file" in {
    val filename = "/tmp/configrity_configuration_obj_spec.conf"
    val fmt = FlatFormat
    val s = 
      """
      foo = true
      bar = 2
      baz = "hello world"
      """
    val writer = new java.io.PrintWriter( filename )
    writer.println(s)
    writer.close()
    try {
      val config = Configuration.load(filename,fmt)
      config.get[Boolean]("foo") should be (Some(true))
      config.get[Int]("bar") should be (Some(2))
      config.get[String]("baz") should be (Some("hello world"))
      val config2 = Configuration.load(filename)
      config2 should be (config)
    } finally {
      ( new java.io.File(filename) ).delete
    }
  }


}

