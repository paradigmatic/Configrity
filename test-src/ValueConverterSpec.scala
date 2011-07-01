import org.scalatest.WordSpec
import org.scalatest.matchers.ShouldMatchers
import configrity.ValueConverter
import configrity.ValueConverters._


class ValueConverterSpec extends WordSpec with ShouldMatchers{


  def convert[A]( opt: Option[String] )(implicit converter: ValueConverter[A] )  =
    converter(opt)

  "The string converter" should {
    "parse string without changing it" in {
      convert[String](None) should be (None)
      convert[String](Option("foo")) should be (Option("foo")) 
    }
  }

  "The int converter" should {
    "parse a string in Int" in {
      convert[Int](Option("1234")) should be (Option(1234))
    }
    "return a exception when the string cannot be parsed" in {
       intercept[Exception] {
         convert[Int](Option("12.34"))
       }
    }
  }

  "The double converter" should {
    "parse a string into a Double" in {
      convert[Double](Option("1234")) should be (Option(1234.0))
      convert[Double](Option("1e-9")) should be (Option(1e-9))
      convert[Double](Option(".1")) should be (Option(.1))
    }
    "return a exception when the string cannot be parsed" in {
       intercept[Exception] {
         convert[Double](Option("1ef-9"))
       }
    }
  }

  "The boolean converter" should {
    "parse a string with true/false into a Boolean" in {
      convert[Boolean](Option("false")) should be (Option(false))
      convert[Boolean](Option("true")) should be (Option(true))
    }
    "parse a string with T/F into a Boolean" in {
      convert[Boolean](Option("F")) should be (Option(false))
      convert[Boolean](Option("T")) should be (Option(true))
    }
    "parse a string with on/off into a Boolean" in {
      convert[Boolean](Option("off")) should be (Option(false))
      convert[Boolean](Option("on")) should be (Option(true))
    }
    "return a exception when the string cannot be parsed" in {
       intercept[Exception] {
         convert[Boolean](Option("False"))
       }
    }
  }
}
