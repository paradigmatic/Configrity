package org.streum.configrity.test.io

import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers
import org.streum.configrity._
import org.streum.configrity.io.StandardFormat.ParserException

class Standard extends FlatSpec with ShouldMatchers{

  "a flat format comment line" must "be ignored" in {
    val config = Configuration.loadResource("/test-config-comments.conf")
		
		val comment = config("comment", "")
		
		assert(comment == "")
  }

}
