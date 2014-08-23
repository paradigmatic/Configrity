package org.streum.configrity.test.io

import org.scalatest.FlatSpec
import org.scalatest.Matchers._
import org.streum.configrity._
import org.streum.configrity.io.FlatFormat._
import org.streum.configrity.io.FlatFormat


class FlatFormatSpec extends FlatSpec {

  "The flat format" can "write and read an empty Configuration" in {
    val config = Configuration()
    fromText( toText( config ) ) should be (config)
  }

  it can "write and read a Configuration" in {
    val config = Configuration( 
      Map("foo"->"FOO", "bar"->"1234", 
	  "baz"->"on",
      "lst" -> """[ on, on, off, off ]""",
	  "empty" -> ""
	)
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
      include "%s"
      foo = false
      baz = "hello"
    """
    autoFile( parentContent ) { parent =>
      autoFile( childContent.format(parent.getAbsolutePath) ) { child =>
        val config = Configuration.load(child.getAbsolutePath)
        config[Boolean]("foo") should be (false)
        config[Int]("bar") should be (2)
        config[String]("baz") should be (""""hello"""")
      }
    }
  }

  it must "choke if the include points to a non existing file" in {
    val childContent = """
      include "/tmp/parent.conf"
      foo = false
      baz = "hello"
    """
    autoFile( childContent ) { child =>
      intercept[java.io.FileNotFoundException] {
        val config = Configuration.load(child.getAbsolutePath)
      }
    }
  }

}
