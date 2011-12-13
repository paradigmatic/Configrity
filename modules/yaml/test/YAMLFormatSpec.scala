package org.streum.configrity.test.yaml

import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers
import org.streum.configrity._

import org.streum.configrity.yaml._


class YAMLFormatSpec extends FlatSpec with ShouldMatchers {

  val FMT = YAMLFormat

 "The yaml format" can "parse a simple  YAML file" in {
   val yml = 
"""
foo: 1
bar: true
baz: hello
"""
   val config = FMT.fromText( yml )
   config[Int]("foo") should be (1)
   config[Boolean]("bar") should be (true)
   config[String]("baz") should be ("hello")
   
 }

  it should "throw an exception if YAML top level is not a map" in {
    val yml = 
"""
- hello
- world
- 1
"""
    intercept[YAMLFormatException]{
      val config = FMT.fromText( yml )
    }
  }

  it can "parse list values" in {
   val yml = 
"""
foo: 1
bar: 
  - true
  - false
  - true
baz: hello
"""
    val config = FMT.fromText( yml )
    config[Int]("foo") should be (1)
    config[List[Boolean]]("bar") should be ( List(true,false,true) )
    config[String]("baz") should be ("hello")
  }

  it can "parse inline list values" in {
   val yml = 
"""
foo: 1
bar: [true, false, true]
baz: hello
"""
    val config = FMT.fromText( yml )
    config[Int]("foo") should be (1)
    config[List[Boolean]]("bar") should be ( List(true,false,true) )
    config[String]("baz") should be ("hello")
  }

  it should "throw an exception if list elements are maps" in {
   val yml = 
"""
foo: 1
bar: 
  - inner:
    hello: world
    tl: dr
baz: hello
"""
    intercept[YAMLFormatException]{
      val config = FMT.fromText( yml )
    }
  }

  it should "throw an exception if list elements are lists" in {
   val yml = 
"""
foo: 1
bar: 
  - inner: 2
  - [1, 3, 5]
baz: hello
"""
    intercept[YAMLFormatException]{
      val config = FMT.fromText( yml )
    }
  }

  it must "parse nested maps as Config blocks" in {
  val yml = 
"""
foo: 1
bar: 
  hello: 12
  tl: dr
  too: 
    deep: map
    also: works
baz: true
"""
    val config = FMT.fromText( yml )
    config[Int]("foo") should be (1)
    config[String]("baz") should be ("true")  
    val inner = config.detach("bar")
    inner[Int]("hello") should be (12)
    inner[String]("tl") should be ("dr")
    val inner2 = inner.detach("too")
    inner2[String]("deep") should be ("map")
  }

  it must "parse only the first YAML document if several present" in {
   val yml = 
"""
---
foo: 1
bar: true
baz: hello
---
foo: 2
bar: false
baz: greetings
"""
   val config = FMT.fromText( yml )
   config[Int]("foo") should be (1)
   config[Boolean]("bar") should be (true)
   config[String]("baz") should be ("hello")
  }

  it can "write a config into YAML" in {
    val config = Configuration( 
      "foo.gnats.gnits" -> "FOO", 
      "bar.buzz" -> 1234, 
      "bar.baz" -> true,
      "lst" -> List( true, true, false, false )
    )
    val config2 = FMT.fromText( FMT.toText( config ) )
    config[List[Boolean]]("lst") should be (config2[List[Boolean]]("lst"))
    config[String]("foo.gnats.gnits") should be (config2[String]("foo.gnats.gnits"))
    config[Int]("bar.buzz") should be (config2[Int]("bar.buzz"))
    config[Boolean]("bar.baz") should be (config2[Boolean]("bar.baz"))
  }

}
