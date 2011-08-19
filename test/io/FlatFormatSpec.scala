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

class FlatFormatParserSpec extends StandardParserSpec {

  lazy val parserName = "FlatFormatParser"
  def parse( s: String ) = FlatFormat.parser.parse(s)

}
