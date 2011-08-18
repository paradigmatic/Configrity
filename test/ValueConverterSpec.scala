import org.scalatest.WordSpec
import org.scalatest.matchers.ShouldMatchers
import org.streum.configrity.ValueConverter
import org.streum.configrity.DefaultConverters


class ValueConverterSpec extends WordSpec with ShouldMatchers with DefaultConverters{


  def convert[A]( opt: Option[String] )(implicit converter: ValueConverter[A] )  =
    converter(opt)

  "The string converter" should {
    "parse string without changing it" in {
      convert[String](None) should be (None)
      convert[String](Option("foo")) should be (Option("foo")) 
    }
  }

  "The byte converter" should {
    "parse a string in Byte" in {
      convert[Byte](Option("12")) should be (Option(12:Byte))
    }
    "return a exception when the string cannot be parsed" in {
       intercept[Exception] {
         convert[Byte](Option("129"))
       }
    }
  }

  "The short converter" should {
    "parse a string in short" in {
      convert[Short](Option("1234")) should be (Option(1234:Short))
    }
    "return a exception when the string cannot be parsed" in {
       intercept[Exception] {
         convert[Short](Option("12341234"))
       }
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

  "The long converter" should {
    "parse a string in Long" in {
      convert[Long](Option("1234")) should be (Option(1234:Long))
    }
    "return a exception when the string cannot be parsed" in {
       intercept[Exception] {
         convert[Long](Option("12.34"))
       }
    }
  }

  "The float converter" should {
    "parse a string into a Float" in {
      convert[Float](Option("1234")) should be (Option(1234.0f))
      convert[Float](Option("1e-9")) should be (Option(1e-9f))
      convert[Float](Option(".1")) should be (Option(.1f))
    }
    "return a exception when the string cannot be parsed" in {
       intercept[Exception] {
         convert[Float](Option("1ef-9"))
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
