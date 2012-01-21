/*
 Copyright (C) 2011, Paradigmatic <paradigmatic@streum.org>

 This file is part of Configrity.
 
 Configrity is free software: you can redistribute it and/or modify
 it under the terms of the GNU Lesser General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.
 
 Configrity is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU Lesser General Public License for more details.
 
 You should have received a copy of the GNU Lesser General Public License
 along with Configrity.  If not, see <http://www.gnu.org/licenses/>.
*/

package org.streum.configrity.yaml

import org.yaml.snakeyaml.Yaml
import org.streum.configrity._
import org.streum.configrity.io.{Format, HierarchyUtils}
import java.util.{Map => JMap}
import java.util.{List => JList}
import collection.JavaConversions._

class YAMLFormatException(msg:String) extends RuntimeException(msg)

object YAMLFormat extends Format with HierarchyUtils {

  def fromText( s: String ) ={
    val map = yaml2map( s )
    new Configuration( map )
  }
  
  def toText( configuration: Configuration ) = writeHierarchy( configuration.data )

  def writeEntry(k:String,v:String,ind:Int,out:StringBuffer) {
    out.append( "    " * ind ).append( k ).append( ": " )
    .append( v ).append( "\n" )
  }

  def writeBlockStart( k:String, ind:Int, out:StringBuffer ) =
      out.append("    "*ind).append(k).append( ": " ).append("\n")

  def writeBlockEnd( k:String, ind:Int, out:StringBuffer ) { } 

  private def yaml2map(s: String): Map[String,String] = {
    (new Yaml).loadAll(s).head match {
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

