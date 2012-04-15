package org.streum.configrity.opt

import org.streum.configrity.Configuration

trait OptParser extends Function[Seq[String],Configuration] {

  private def ??? = throw new Exception( "Not implemented yet" )
  
  def apply( args: Seq[String] ): Configuration = ???

  protected def switch( name: String, short: String = "" ): Unit = ???

  protected def option( name: String, short: String = "" ): Unit = ???

}
