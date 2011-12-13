package org.streum.configrity.yaml

import org.yaml.snakeyaml.Yaml
import org.streum.configrity._
import org.streum.configrity.io.Format
import java.util.{Map => JMap}
import java.util.{List => JList}
import collection.JavaConversions._

class YAMLFormatException(msg:String) extends RuntimeException(msg)

object YAMLFormat extends Format {

  def fromText( s: String ) ={
    val map = yaml2map( s )
    new Configuration( map )
  }
  
  def toText( configuration: Configuration ) = ""

  private def yaml2map(s: String): Map[String,String] = {
    new Yaml load s match {
      case jmap: JMap[_,_] => readMap( "", jmap.toMap )
      case other => {
        val klass = other.getClass
	except( "Top level should be a map. Received: " + klass )
      }
    }
  }

  private def readMap( prefix: String, map: Map[_,_] ):Map[String,String] =
    map.foldLeft( Map[String,String]() ){
      case (map,(k,v)) => map ++ readValue( prefix + k.toString, v )
    }
  

  private def readValue(key: String, value: Any ):Map[String,String] = 
    value match {
      case jl: JList[_] => readList( key, jl )
      case jm: JMap[_,_] => readMap( key+".", jm.toMap )
      case _ => Map( key -> value.toString )
    }

  private def readList( key: String, lst: JList[_] ):Map[String,String] = {
    val value = lst.toList.map {
      case jlist: JList[_] => except("Lists cannot contain nested lists")
      case jMap: JMap[_,_] => except("Lists cannot contain nested maps")
      case s => "\"" + s.toString + "\""
    }.mkString( "[", ",", "]" )
    Map( key -> value )
  }

  private def except( msg: String ) =
    throw new YAMLFormatException( msg )

}

