package org.streum.configrity.test.io

import org.scalatest.FlatSpec
import org.scalatest.Matchers._
import org.streum.configrity._
import org.streum.configrity.io.BlockFormat
import org.streum.configrity.io.BlockFormat._
import org.streum.configrity.io.StandardFormat.ParserException

class BlockFormatSpec extends FlatSpec  {

  "The block format" can "write and read an empty Configuration" in {
    val config = Configuration( )
    fromText( toText( config ) ) should be (config)
  }

  it can "write and read a Configuration" in {
    val config = Configuration( 
      Map("foo"->"FOO", "bar"->"1234", "baz"->"on", "empty"->"")
    )
    fromText( toText( config ) ) should be (config)
  }

   it can "write and read a Configuration with nested blocks" in {
    val config = Configuration( 
      Map(
        "foo.gnats.gnits"->"FOO", 
        "bar.buzz"->"1234", 
        "bar.baz"->"on",
	"lst" -> """[ on, on, off, off ]"""
      )
    )
    fromText( toText( config ) ) should be (config)
  }

}

class BlockFormatParserSpec extends StandardParserSpec with IOHelper {

  def parse( s: String ) = BlockFormat.parser.parse(s)
  lazy val parserName = "BlockFormatParser"

  it can "parse nested blocks" in {
    val s = 
    """
     # Example
    foo = true
    block {
      bar = 2 
      sub {
        buzz = hello
      }
      baz = x
    }
    """
    val config = parse( s ) 
    config[Boolean]("foo") should be (true)
    config[Int]("block.bar") should be (2)
    config[String]("block.baz") should be ("x")
    config[String]("block.sub.buzz") should be ("hello")
  }

  it can "parse nested blocks mixed with flat notation" in {
    val s = 
    """
     # Example
    foo = true
    block {
      bar = 2 
      sub {
        buzz = hello
      }
      sub.blah = true
    }
    block.baz = x
    """
    val config = parse( s ) 
    config[Boolean]("foo") should be (true)
    config[Int]("block.bar") should be (2)
    config[String]("block.baz") should be ("x")
    config[String]("block.sub.buzz") should be ("hello")
    config[Boolean]("block.sub.blah") should be (true)
  }

  it must "skip all comment with nested blocks" in {
    val s = 
    """
     # Example
    foo = true
    block {
      bar = 2 # ignore
      sub {
        #comment
        buzz = hello 
      }
      baz = x
    }
    """
    val config = parse( s ) 
    config[Boolean]("foo") should be (true)
    config[Int]("block.bar") should be (2)
    config[String]("block.baz") should be ("x")
    config[String]("block.sub.buzz") should be ("hello")
  }

  it should "ignore whitespaces" in {
   val s = 
    """
     # Example
    foo =    true
         block {
  bar= 2 
      sub {
         buzz = hello
               }
     baz =x
                       }
    """
    val config = parse( s ) 
    config[Boolean]("foo") should be (true)
    config[Int]("block.bar") should be (2)
    config[String]("block.baz") should be ("x")
    config[String]("block.sub.buzz") should be ("hello")
  }

  it must "choke if no key is provided for blocks" in {
        val s = 
    """
     # Example
    foo = true
    block {
      bar = 2 
      {
        buzz = hello
      }
      sub.blah = true
    }
    block.baz = x
    """
    intercept[ParserException] {
      val config = parse( s ) 
    }
  }

  it must "choke if a block is not closed" in {
        val s = 
    """
     # Example
    foo = true
    block {
      bar = 2 
      sub {
        buzz = hello
      sub.blah = true
    }
    block.baz = x
    """
    intercept[ParserException] {
      val config = parse( s ) 
    }
  }

  it should "merge blocks with same key" in {
    val s = 
    """
     # Example
    foo = true
    block {
      bar = 2 
    }
    block {
      bar = x
    }
    """
    val config = parse( s )
    config[String]("block.bar") should be ("x")
  }

  it must "ignore empty blocks" in {
    val s = 
    """
     # Example
    foo = true
    block {
      bar = 2 
      sub {
        
      }
      baz = x
      sub2 {
        sub3 {
          hoo = false
          sub4 {
          }
        }
      }
    }
    """
    val config = parse( s ) 
    config[Boolean]("foo") should be (true)
    config[Int]("block.bar") should be (2)
    config.get[String]("block.sub") should be (None)
    config.get[String]("block") should be (None)
    config[String]("block.baz") should be ("x")
    config[Boolean]("block.sub2.sub3.hoo") should be (false)
    config.get[Boolean]("block.sub2.sub3.sub4") should be (None)
  }

  it can "parse include directive" in {
    val parentContent = """
      block {
        foo = true
        bar = 2
      }
    """
    val childContent = """
      include "%s"
      
      block {
        foo = false
        baz = "hello"
      }
    """
    autoFile( parentContent ) { parent =>
      autoFile( childContent.format(parent.getAbsolutePath) ) { child =>
        val config = Configuration.load(child.getAbsolutePath)
        config[Boolean]("block.foo") should be (false)
        config[Int]("block.bar") should be (2)
        config[String]("block.baz") should be ("hello")
      }
    }
  }

  it must "choke if the include points to a non existing file" in {
    val childContent = """
      include "/tmp/parent.conf"
      
      block {
        foo = false
        baz = "hello"
      }
    """
    autoFile( childContent ) { child =>
      intercept[java.io.FileNotFoundException] {
        val config = Configuration.load( child.getAbsolutePath )
      }
    }
  }
}
