package org.streum.configrity.test.io

import org.scalatest.FlatSpec
import org.scalatest.Matchers._
import org.streum.configrity._
import org.streum.configrity.io.StandardFormat.ParserException

class Standard extends FlatSpec {

  "a flat format comment line" must "be ignored" in {
    val config = Configuration.loadResource("/test-config-comments.conf")
    val comment = config("comment", "")
    assert(comment == "")
  }

}
