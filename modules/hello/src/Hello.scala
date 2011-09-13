package org.streum.configrity.hello

import org.streum.configrity._

object Hello {

  def greetings = {
    val c = new Configuration( Map( "greetings" -> "Hello World" ) )
    c[String]( "greetings" )
  }

}

object HelloMain extends App {

  println( Hello.greetings )
  sys.exit(0)

}
