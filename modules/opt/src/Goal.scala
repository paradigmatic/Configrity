package org.streum.configrity.opt

import org.streum.configrity._

object Goal extends App{

  object MyOptParser extends OptParser {
    //flag( "verbose", "v" )
    opt( "name", "n", description="Guy's name" )
    opt( "age", "a", description="Guy's age (in years)" )
    argument( "input", "inputfile" )
  }

  //val arguments = Array( "-v", "--name", "marc", "antoine" )
  val arguments = Array( "--name", "marc," "data.txt" )

  val conf: Configuration = MyOptParser( arguments )

  println( conf )

}
