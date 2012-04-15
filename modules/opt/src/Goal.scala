package org.streum.configrity.opt

import org.streum.configrity._

object Goal {

  object MyOptParser extends OptParser {
    switch( "verbose", "v" )
    option( "name", "n" )
  }

  val arguments = Array( "-v", "--name", "marc", "antoine" )

  val conf: Configuration = MyOptParser( arguments )

  println( conf[Boolean]( "verbose" ) )


}
