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
object BlockFormat extends StandardFormat with HierarchyUtils {

  def toText( configuration: Configuration ) = writeHierarchy( configuration.data )

  def writeEntry(k:String,v:String,ind:Int,out:StringBuffer) {
    out.append( "  " * ind )
    .append( k ).append(" = ").append(
      sanitizeEmpty( v )
    ).append("\n")
  }
  def writeBlockStart( k:String, ind:Int, out:StringBuffer ) =
      out.append("  "*ind).append(k).append(" {").append("\n")

  def writeBlockEnd( k:String, ind:Int, out:StringBuffer ) =
    out.append("  "*ind).append("}").append("\n")

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
      case k ~ _ =>  blocks ::= k
    }
    
    def emptyBlock = blockStart <~ closeBrace ^^ {
      case _ => {
        blocks = blocks.tail.tail
        Configuration()
      }
    }
    


    def block: Parser[Configuration] = 
      blockStart ~ ( block | emptyBlock | entry ) ~ content ~ closeBrace ^^ {
        case _ ~ single ~ rest ~ _ => {
          val newLst = addPrefix( single ++ rest )
          blocks = blocks.tail
          newLst
        }
      }
   
    def content = rep( includeDirective | block | emptyBlock | entry ) ^^ { reduce }
  }


} 
