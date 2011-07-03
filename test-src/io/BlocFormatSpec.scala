import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers
import configrity.Configuration
import configrity.ValueConverters._
import configrity.io.BlockFormat
import configrity.io.BlockFormat._

class BlockFormatSpec extends FlatSpec with ShouldMatchers{

  "The block format" can "write and read an empty Configuration" in {
    val config = Configuration( Map() )
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
        "bar.baz"->"on"
      )
    )
    fromText( toText( config ) ) should be (config)
  }

}

class BlockFormatParserSpec extends FlatSpec with ShouldMatchers{

  def parse( s: String ) = new Parser().parse(s)

  "The flat format parse" can "parse empty string" in {
    val config = parse( "" )
    config.data should be ('empty)
  }

  it can "parse a wellformed entry line" in {
    val config = parse( "foo = 2" )
    config[Int]("foo") should be (Some(2))
  }
 
  it should "ignore leading and trailing space" in {
    val config = parse( "    foo = 2" )
    config[Int]("foo") should be (Some(2))
  } 

  it should "ignore extra spaces around the equal sign" in {
    val config = parse( "    foo     =      2" )
    config[Int]("foo") should be (Some(2))
  } 

  it should "tolerate an equal sign without space around" in {
    val config = parse( "    foo=2 " )
    config[Int]("foo") should be (Some(2))
  }

  it can "parse several lines" in {
    val s = 
    """
    foo = true
    bar = 2
    baz = "hello world"
    """
    val config = parse( s )
    config[Boolean]("foo") should be (Some(true))
    config[Int]("bar") should be (Some(2))
    config[String]("baz") should be (Some("hello world"))
  }

  it can "parse several badly spaced lines" in {
    val s = 
    """
       foo  =true
    bar= 2        
                                 baz = "hello world"
     """
    val config = parse( s )
    config[Boolean]("foo") should be (Some(true))
    config[Int]("bar") should be (Some(2))
    config[String]("baz") should be (Some("hello world"))
  }

  it must "choke when encoutering an unquoted value with spaces" in {
       val s = 
    """
    foo = true
    bar = 2
    baz = hello world
    """
    intercept[ParserException] {
      val config = parse( s ) 
    }
  }

  it must "choke when encountering lines with two equals" in {
       val s = 
    """
    foo = true
    bar = 2
    baz = x = 2
    """
    intercept[ParserException] {
      val config = parse( s ) 
    }
  }

  it must "choke when encoutering a line without value" in {
       val s = 
    """
    foo = true
    bar = 
    baz = x 
    """
    intercept[ParserException] {
      val config = parse( s ) 
    }
  }

  it must "choke when encoutering a line without key" in {
       val s = 
    """
    foo = true
    = 2 
    baz = x 
    """
    intercept[ParserException] {
      val config = parse( s ) 
    }
  }

  it must "choke when encoutering a line without equals sign" in {
       val s = 
    """
    foo = true
    bar  2 
    baz = x 
    """
    intercept[ParserException] {
      val config = parse( s ) 
    }
  }

  it must "skip comments starting with a '#'" in {
       val s = 
    """
     # Example
    foo = true
    #bar = 2 
    baz = x 
    """
    val config = parse( s ) 
    config[Boolean]("foo") should be (Some(true))
    config[Int]("bar") should be (None)
    config[String]("baz") should be (Some("x"))
  }

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
    config[Boolean]("foo") should be (Some(true))
    config[Int]("block.bar") should be (Some(2))
    config[String]("block.baz") should be (Some("x"))
    config[String]("block.sub.buzz") should be (Some("hello"))
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
    config[Boolean]("foo") should be (Some(true))
    config[Int]("block.bar") should be (Some(2))
    config[String]("block.baz") should be (Some("x"))
    config[String]("block.sub.buzz") should be (Some("hello"))
    config[Boolean]("block.sub.blah") should be (Some(true))
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
    config[Boolean]("foo") should be (Some(true))
    config[Int]("block.bar") should be (Some(2))
    config[String]("block.baz") should be (Some("x"))
    config[String]("block.sub.buzz") should be (Some("hello"))
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
    config[Boolean]("foo") should be (Some(true))
    config[Int]("block.bar") should be (Some(2))
    config[String]("block.baz") should be (Some("x"))
    config[String]("block.sub.buzz") should be (Some("hello"))
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
    config[String]("block.bar") should be (Some("x"))
  }
}
