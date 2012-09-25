package org.streum.configrity.test

import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers
import org.streum.configrity.Configuration
import org.streum.configrity.converter.DefaultConverters
import org.streum.configrity.io._


class ConfigurationSpec extends FlatSpec with ShouldMatchers with DefaultConverters with io.IOHelper {

  val data = Map("foo"->"FOO", "bar"->"1234", "baz"->"on" )
  val config = Configuration( data )

  "A configuration" should "return none if it doesn't contain a key when 'getted'" in {
    config.get[Int]("buzz") should be (None)
  }

  it can "tell if a key is defined" in {
    config.contains("foo") should be (true)
    config.contains("bar") should be (true)
    config.contains("baz") should be (true)
    config.contains("buzz") should be (false)
  }

  it should "throw exception if it doesn't contain a key when 'applied'" in {
    intercept[java.util.NoSuchElementException] {
      config[Int]("buzz")
    }.getMessage should be ("buzz")
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

  it should "be able to remove an existing key" in {
    val c2 = config.clear("foo")
    config[String]("foo") should be ("FOO")
    c2.get[String]("foo") should be (None)
  }
  
  it should "not complain when trying to remove an non-existent key" in {
    val c2 = config.clear("buzz")
    c2 should be (config)
  }

  it should "format lists correctly" in {
    val lst = List( 1, 2, 3, 5 )
    val c2 = config.set( "list", lst )
    val lst2 = c2[List[Int]]( "list" )
    lst should be (lst2)
  }

  it should "format empty lists correctly" in {
    val lst = List[Int]( )
    val c2 = config.set( "list", lst )
    val lst2 = c2[List[Int]]( "list" )
    lst should be (lst2)
  }

  it should "format lists with empty spaces in values correctly" in {
    val lst = List( "hello world" )
    val c2 = config.set( "list", lst )
    val lst2 = c2[List[String]]( "list" )
    lst should be (lst2)
  }


  it can "be nicely formatted" in {
    val out = new ExportFormat {
      def toText( c: Configuration ) = "FOOBAR"
    }
    config.format(out) should be ("FOOBAR")
  }
  
  it can "be saved to a file" in {
    val fn1 = "/tmp/configrity_configuration_spec_1.conf"
    val fn2 = "/tmp/configrity_configuration_spec_2.conf"
    autoFile( fn1 ){ file1 =>
      autoFile( fn2 ){ file2 =>
        config.save( file1 )
        config.save( file2, FlatFormat ) 
        val config2 = Configuration.load( file1.getAbsolutePath )
        val config3 = Configuration.load( file2.getAbsolutePath )
        config2 should be (config)
        config3 should be (config)
      }
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

  it can "be detach from a configuration" in {
    val data2 = Map( "one" -> "1", "two" -> "2" )
    val config2 = Configuration( data2 )
    val config3 = config attach ("nums", config2 )
    val config4 = config3 detach ("nums")
    config4.data should be (config2.data)
  }

  it can "include another configuration" in {
    val config2 = Configuration( "foo" -> "fu", "buzz" -> 122 )
    val config3 = config include config2
    config3[String]("foo") should be ("FOO")
    config3[Int]("bar") should be (1234)
    config3[Int]("buzz") should be (122)
    (config2 include config) should not be (config include config2)
  }
  
  it should "carry its original prefix" in {
    val sup = Configuration()
    val sub1 = Configuration()
    val sub2 = Configuration()
    val full = sup.attach("foo", sub1.attach("bar", sub2))
    full.detach("foo.bar").prefix should be (Some("foo.bar"))
  }

  "Sub configurations" can "be detached from a configuration" in {
    val sup = Configuration( "foo" -> "bar" )
    val sub1 = Configuration(Map( "one" -> "1", "two" -> "2" ))
    val sub2 = Configuration(Map( "first" -> "", "second" -> "b" ))
    val full = sup attach ("nums", sub1) attach ("letters", sub2)
    full.detachAll should be (Map(
      "nums" -> sub1.copy(prefix = Some("nums")),
      "letters" -> sub2.copy(prefix = Some("letters"))
    ))
  }
}

class ConfigurationObjectSpec extends FlatSpec with ShouldMatchers with io.IOHelper {

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

  it can "support lists directly with key value pairs" in {
    val config = Configuration(
      "foo"-> List(10), 
      "bar"-> ("hello"::"world"::Nil)
    )
    config[List[Int]]("foo") should be (List(10)) 
    config[List[String]]("bar") should be (List("hello","world")) 
  }

  it can "support lists directly with empty spaces in values" in {
    val config = Configuration(
      "foo"-> List(10), 
      "bar"-> ("hello world"::Nil)
    )
    config[List[Int]]("foo") should be (List(10)) 
    config[List[String]]("bar") should be (List("hello world")) 
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
    val fmt = FlatFormat
    val s = 
      """
      foo = true
      bar = 2
      baz = "hello world"
      """
    autoFile( s ){ file =>
      val fn = file.getAbsolutePath
      val config = Configuration.load(fn,fmt)
      config.get[Boolean]("foo") should be (Some(true))
      config.get[Int]("bar") should be (Some(2))
      config.get[String]("baz") should be (Some("hello world"))
      val config2 = Configuration.load(fn)
      config2 should be (config)
    }
  }

 it can "be loaded from the classpath" in {  
   val fmt = FlatFormat
   val resName = "/test-config.conf"
   val config = Configuration.loadResource( resName, fmt )
   config.get[Boolean]("foo") should be (Some(true))
   config.get[Int]("bar") should be (Some(2))
   config.get[String]("baz") should be (Some("hello world"))
   val config2 = Configuration.loadResource( resName )
   config2 should be (config)
 }

 it must "throw FileNotFoundException when loading non-existing resource from the classpath" in {
   val resName = "/non-existing.conf"
   val ex = evaluating {
     Configuration.loadResource( resName )
   } should produce [java.io.FileNotFoundException]
   ex.getMessage should be (resName)
 }
}

