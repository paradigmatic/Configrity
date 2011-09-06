package org.streum.configrity.test.io

import java.io.File
import java.io.PrintWriter

trait IOHelper {

  def autoFile[A]( filename: String, content: String = "" )
  ( body: File => A ) = {
    val f = new File( filename )
    try {
      if( content != "" ) {
        val out = new PrintWriter( f )
        out println content 
        out close
      }
      body( f )
    } finally {
      f delete
    }
  }


}

object IOHelper extends IOHelper
