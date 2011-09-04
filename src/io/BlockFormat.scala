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

package org.streum.configrity.io

import org.streum.configrity.Configuration
import scala.collection.mutable.StringBuilder
import scala.util.parsing.combinator._

/**
 * Encodes the default block format
 */
object BlockFormat extends StandardFormat {

  def toText( configuration: Configuration ) = write( configuration.data )

  private def splitKey( s: String ) = s.split("""\.""").toList
  private def joinKey( ss: List[String] ) = ss.mkString(".")
  private def writeEntry(k:String,v:String,ind:Int,out:StringBuffer) {
    out.append( "  " * ind )
    .append( k ).append(" = ").append(v).append("\n")
  }

  private def write( 
    map: Map[String,String], 
    indents: Int = 0
  ): String = {
    val out = new StringBuffer
    var blocks = Map[String,Map[String,String]]()
    for( (k,v) <- map ) {
      splitKey(k) match {
        case el :: Nil => {
          writeEntry(k,v,indents,out)
        }
        case first :: rest => {
          val subKey = joinKey(rest)
          blocks +=  first -> 
          ( blocks.getOrElse( first, Map() ) + ( subKey -> v) )
        }
        case _ =>
      }
    }
    for( (k,block) <- blocks ) {
      out.append("  "*indents).append(k).append(" {").append("\n")
      out.append( write( block, indents + 1 ) )
      out.append("  "*indents).append("}").append("\n")
    }
    out.toString
  }

  def parser = new BlockParser

  class BlockParser extends Parser {

    private var blocks = List[String]()
   
    private def addPrefix( config: Configuration ) = 
      blocks match {
        case Nil => config
        case head :: _ => Configuration().attach( head, config )
      }
    
    val dot = "."
    val openBrace = "{"
    val closeBrace = "}"

    def blockStart: Parser[Unit] = key ~ openBrace ^^ {
      case k ~ _ => blocks ::= k
    }

    def block: Parser[Configuration] = 
      blockStart ~ ( block | entry ) ~ content ~ closeBrace ^^ {
        case _ ~ single ~ rest ~ _ => {
          val newLst = addPrefix( single ++ rest )
          blocks = blocks.tail
          newLst
        }
      }
   
    def content = rep( includeDirective | block | entry ) ^^ { reduce }
  }


} 
