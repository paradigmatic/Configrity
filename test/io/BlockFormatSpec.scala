import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers
import org.streum.configrity._
import org.streum.configrity.io.BlockFormat
import org.streum.configrity.io.BlockFormat._
import org.streum.configrity.io.StandardFormat.ParserException

class BlockFormatSpec extends FlatSpec with ShouldMatchers{

  "The block format" can "write and read an empty Configuration" in {
    val config = Configuration( )
    fromText( toText( config ) ) should be (config)
  }

  it can "write and read a Configuration" in {
    val config = Configuration( 
      Map("foo"->"FOO", "bar"->"1234", "baz"->"on")
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

class BlockFormatParserSpec extends StandardParserSpec {

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

  it must "choke on empty blocks" in {
    val s = 
    """
     # Example
    foo = true
    block {
      bar = 2 
      sub {
        
      }
      baz = x
    }
    """
    intercept[ParserException] {
      val config = parse( s ) 
    }
  }
}
