package org.streum.configrity.opt


import scopt.immutable.OptionParser
import org.streum.configrity.Configuration

trait OptParser extends Function[Seq[String],Configuration] {

  private var itemList = List[Item]()

  private def ??? = throw new Exception( "Not implemented yet" )
  
  private def parser = new OptionParser[Configuration]("scopt", "2.x") { 
    def options = itemList.collect {
      case Opt( name, Some(shortName), _ , description ) => {
	opt( shortName, name, description ){ 
	  (value:String,config:Configuration) => config.set( name, value )
	}
      }
      case Argument( name, description ) => {
	arg( name, description ){ 
	  (value:String,config:Configuration) => config.set( name, value )
	}
      }
    }
  }

  def apply( args: Seq[String] ): Configuration =
    parser.parse(args, Configuration()).get

  def showUsage = parser.showUsage

  private def optStr( str: String ) = if( str == "" ) None else Some(str)

  protected def flag( 
    name: String, 
    shortName: String = "",
    description: String = ""
  ) {
    itemList ::= Flag( name, optStr(shortName), description )
  }

  protected def opt( 
    name: String, 
    shortName: String = "",
    argumentName: String = "",
    description: String = ""
  ) {
    itemList ::= Opt( 
      name, 
      optStr(shortName), 
      optStr(argumentName), 
      description 
    )
  }

  protected def argument( name: String, description: String = "" ) {
    itemList ::= Argument( name, description )
  }


}
