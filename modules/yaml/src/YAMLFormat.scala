package org.streum.configrity.yaml

import org.yaml.snakeyaml.Yaml
import org.streum.configrity._
import org.streum.configrity.io.Format
import java.util.{Map => JMap}
import collection.JavaConversions._

class YAMLFormatException(msg:String) extends RuntimeException(msg)

object YAMLFormat extends Format {

  def fromText( s: String ) = {
    new Configuration( yaml2map( s ) )
  }
  
  def toText( configuration: Configuration ) = ""

  private def yaml2map(s: String): Map[String,String] = {
    new Yaml load s match {
      case jmap: JMap[_,_] => {
        jmap.toMap.map {
          case (k,v) => (k.toString,v.toString)
        }
      }
      case other => {
        val klass = other.getClass
        throw new YAMLFormatException(
          "Top level should be a map. Received: " + klass 
        )
      }
    }
  }

}

