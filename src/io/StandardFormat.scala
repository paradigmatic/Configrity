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
 * Encodes the common behavior of standard formats
 */
trait StandardFormat extends Format {
  
  def parser: Parser

  def fromText( s: String ) = parser.parse( s )

  trait Parser extends RegexParsers {

    def reduce( lst: List[Configuration] ) =
      lst.foldLeft( Configuration() )( _ ++ _ )

    def unquote( s: String ) = s.substring( 1, s.size - 1 )

    def protect( s: String ) =  word.findFirstIn(s)  match {
      case Some(z) if s == z => s
      case _ => "\"" + s + "\""
    }

    override val whiteSpace = """(\s+|#[^\n]*\n)+""".r
    def key = """([^=\s])+""".r 
    val lineSep = "\n"
    def word = """([^=\s\n#\{\}\"\[\],])+""".r 
    def quoted = """"([^"]*)"""".r /*"*/ ^^ { unquote }
    val equals  = "="

    def item = word | quoted

    def items = repsep( item, "," )
    def list = "[" ~ items ~ "]" ^^ {
      case _ ~ lst ~ _ => lst.map( protect ).mkString("[ ", ", ", " ]")
    }

    def value = item | list

    def entry = key ~ equals ~ value ^^ {
      case k ~ _ ~ v  => Configuration( k -> v )
    }

    def content: Parser[Configuration]
    
    def parse( in: String )  = {
      parseAll(content, in) match {
        case Success( config , _ ) => config
        case x: NoSuccess => throw StandardFormat.ParserException(x.toString)
      }
    }

  }


} 

object StandardFormat{ 

  /** Parser exceptions */
  case class ParserException(s: String) extends Exception(s)

}
