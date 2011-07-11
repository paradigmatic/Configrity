import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers
import configrity._


class ReeaderSpec extends FlatSpec with ShouldMatchers with DefaultConverters{

  val config = Configuration( "foo"->"FOO", "bar"->"1234", "baz"->"on" )

  "A configuration reader" can "be obtained out of a key" in {
    val bar = read[Int]("bar")
    bar(config) should be (1234)
  }

  it should "throw an exception if the key does not exists" in {
    val buzz = read[Int]("buzz")
    intercept[Exception] {
      buzz(config) should be (1234)
    }
  }
  
  it can "have a default value" in {
    val bar = read("bar", -101)
    val buzz = read("buzz", 12 )
    bar(config) should be (1234)
    buzz(config) should be (12)
  }

  def extract[A]( reader: Reader[A] ) = reader(config)
  def unit[A](a: A) = new Reader[A] {
    def apply( c: Configuration ) = a
  }
  def twice( i: Int ) = unit( 2*i )
  def mkString( i: Int) = unit( i.toString )

  "Readers" must "respect monad 1st axiom" in {
    extract( unit(12).flatMap( twice ) ) should be ( extract(twice(12)) )
    extract( unit(147).flatMap( unit ) ) should be ( extract(unit(147) ) )
  }

  it must "respect monad 2nd axiom" in {
    val m = unit( 41 )
    val readerA = m flatMap twice flatMap mkString
    val readerB = m flatMap { x => ( twice(x) flatMap mkString ) }
    extract( readerA ) should be (extract(readerB))
  }

  it can "be composed" in {
    val fooBarBaz = for( s <- read[String]("foo");
                         i <- read[Int]("bar");
                         b <- read[Boolean]("baz") 
                      ) yield { if( b ) i.toString else s }
    
    fooBarBaz( config ) should be ("1234")
  }



}
