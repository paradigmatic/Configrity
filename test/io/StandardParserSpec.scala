import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers
import org.streum.configrity._
import org.streum.configrity.io.StandardFormat.ParserException

trait StandardParserSpec extends FlatSpec with ShouldMatchers {

  def parse( s: String ): Configuration
  val parserName: String
  

  parserName can "parse empty string" in {
    val config = parse( "" )
    config.data should be ('empty)
  }

  it can "parse a wellformed entry line" in {
    val config = parse( "foo = 2" )
    config[Int]("foo") should be (2)
  }
 
  it should "ignore leading and trailing space" in {
    val config = parse( "    foo = 2" )
    config[Int]("foo") should be (2)
  } 

  it should "ignore extra spaces around the equal sign" in {
    val config = parse( "    foo     =      2" )
    config[Int]("foo") should be (2)
  } 

  it should "tolerate an equal sign without space around" in {
    val config = parse( "    foo=2 " )
    config[Int]("foo") should be (2)
  }

  it can "parse several lines" in {
    val s = 
    """
    foo = true
    bar = 2
    baz = "hello world"
    """
    val config = parse( s )
    config[Boolean]("foo") should be (true)
    config[Int]("bar") should be (2)
    config[String]("baz") should be ("hello world")
  }

  it can "parse several badly spaced lines" in {
    val s = 
    """
       foo  =true
    bar= 2        
                                 baz = "hello world"
     """
    val config = parse( s )
    config[Boolean]("foo") should be (true)
    config[Int]("bar") should be (2)
    config[String]("baz") should be ("hello world")
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
    config[Boolean]("foo") should be (true)
    config.get[Int]("bar") should be (None)
    config[String]("baz") should be ("x")
  }

  it can "accept lists" in {
       val s = 
    """
     # Example
    foo = [ true, false ]
    bar = [1,2,       3,4               ]
    baz = [ "hello", "wo,rld" ]
    """
    val config = parse( s ) 
    config[String]("foo") should be ("[ true, false ]")
    config[String]("bar") should be ("[ 1, 2, 3, 4 ]")
    config[String]("baz") should be ("[ hello, \"wo,rld\" ]")
  }

  it can "accept empty lists" in {
       val s = 
    """
     # Example
    foo = [ "hello" ]
    bar = []
    baz = [ "world" ]
    """
    val config = parse( s ) 
    config[String]("foo") should be ("[ hello ]")
    config[String]("bar") should be ("[  ]")
    config[String]("baz") should be ("[ world ]")
  }
  
    it can "accept lists defined over several lines" in {
       val s = 
    """
     # Example
    foo = [ true, false ]
    bar = [ 1,
            2,
            3,
            4
          ]
    baz = [ "hello", 
                   "wo,rld"
        ]
    """
    val config = parse( s ) 
    config[String]("foo") should be ("[ true, false ]")
    config[String]("bar") should be ("[ 1, 2, 3, 4 ]")
    config[String]("baz") should be ("[ hello, \"wo,rld\" ]")
  }



  it must "choke if a list is not between square brackets" in {
       val s = 
    """
     # Example
    foo = [ true, false ]
    bar = [1,2,       3,4               
    baz = [ "hello", "wo,rld" ]
    """
    intercept[ParserException] {
      val config = parse( s ) 
    }
  }

}
