import org.scalatest.WordSpec
import org.scalatest.matchers.ShouldMatchers
import org.streum.configrity.converter._
import java.io.File
import java.awt.Color
import java.net.URI
import java.net.URL

class ExtraConverterSpec extends WordSpec with ShouldMatchers with DefaultConverters{

  import ConverterHelper._
  import Extra._

  "The file converter" should {
    "parse a string to File" in {
      convert[File]("/tmp") should be (new File("/tmp"))
      convert[File]("") should be (new File(""))
    }
  }

  "The color converter" should {
    "parse a string to Color" in {
      convert[Color]("000000") should be (new Color(0))
    }
    "ignore preceding hash" in {
      convert[Color]("#000000") should be (new Color(0))
      convert[Color]("#ffffff") should be (new Color(255,255,255))
    }  
    "accept a mix of both case" in {
      convert[Color]("#fFFfFF") should be (new Color(255,255,255))
    }
    "return a exception when the string has invalid chars" in {
       intercept[Exception] {
         convert[Color]("#00FFX2")
       }
    }
    "return a exception when the string has not 6 hex digits (except hash)" in {
       intercept[Exception] {
         convert[Color]("#fab")
       }
    }
  }

  "The URI converter" should {
    "parse a string to an URI" in {
      convert[URI]("http://www.example.com/hello") should 
      be (new URI("http://www.example.com/hello"))
      convert[URI]("./hello") should 
      be (new URI("./hello"))
    }
  }

  "The URL converter" should {
    "parse a string to an URI" in {
      convert[URL]("http://www.example.com/hello") should 
      be (new URL("http://www.example.com/hello"))
    }
   "return a exception when the url is not aboslute" in {
      intercept[IllegalArgumentException] {
	convert[URL]("./hello")
      }
    } 
   "return a exception when the string is empty" in {
      intercept[IllegalArgumentException] {
	convert[URL]("")
      }
    }
  }

}
