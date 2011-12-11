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

}
