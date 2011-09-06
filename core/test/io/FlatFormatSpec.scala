package org.streum.configrity.test.io

import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers
import org.streum.configrity._
import org.streum.configrity.io.FlatFormat._
import org.streum.configrity.io.FlatFormat


class FlatFormatSpec extends FlatSpec with ShouldMatchers{

  "The flat format" can "write and read an empty Configuration" in {
    val config = Configuration()
    fromText( toText( config ) ) should be (config)
  }

  it can "write and read a Configuration" in {
    val config = Configuration( 
      Map("foo"->"FOO", "bar"->"1234", "baz"->"on",
          "lst" -> """[ on, on, off, off ]""" )
    )
    fromText( toText( config ) ) should be (config)
  }

}

class FlatFormatParserSpec extends StandardParserSpec with IOHelper{

  lazy val parserName = "FlatFormatParser"
  def parse( s: String ) = FlatFormat.parser.parse(s)

  it can "parse include directive" in {
    val parentContent = """
        foo = true
        bar = 2
    """
    val childContent = """
      include "/tmp/parent.conf"
      foo = false
      baz = "hello"
    """
    autoFile( "/tmp/parent.conf", parentContent ) { parent =>
      autoFile( "/tmp/child.conf", childContent ) { child =>
        val config = Configuration.load("/tmp/child.conf")
        config[Boolean]("foo") should be (false)
        config[Int]("bar") should be (2)
        config[String]("baz") should be ("hello")
      }
    }
  }

  it must "choke if the include points to a non existing file" in {
    val childContent = """
      include "/tmp/parent.conf"
      foo = false
      baz = "hello"
    """
    autoFile( "/tmp/child.conf", childContent ) { child =>
      intercept[java.io.FileNotFoundException] {
        val config = Configuration.load("/tmp/child.conf")
      }
    }
  }

}
