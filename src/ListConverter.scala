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

package org.streum.configrity

import scala.util.parsing.combinator._

/**
 * Parse a list of type A. The string representation must be surrounded
 * by square brackets: '[...]'. Elements are separed by comma: 'elem1, elem2'.
 * Elements are either a single word without reserved characters, or any characters
 * surrounded by double quotes '"'.
 */
class ListConverter[A]( implicit val valueConverter: ValueConverter[A] )
extends ValueConverter[List[A]] {

  def parse( s: String ) = {
    ListConverter.Parser parse s map valueConverter.parse
  }

}

object ListConverter {
  
  object Parser extends RegexParsers {

    private def unquote( s: String ) = s.substring( 1, s.size - 1 )

    override val whiteSpace = """(\s+|#[^\n]*\n)+""".r

    val lineSep = "\n"
    def word = """([^=\s\n#\{\}\"\[\],])+""".r 
    def quoted = """"([^"]*)"""".r /*"*/ ^^ { unquote }

    def item = word | quoted

    def items = repsep( item, "," )

    def list = "[" ~ items ~ "]" ^^ {
      case _ ~ lst ~ _ => lst
    }
    
    def parse( in: String ): List[String]  = {
      parseAll(list, in) match {
        case Success( lst , _ ) => lst
        case x: NoSuccess => throw new IllegalArgumentException( x.toString )
      }
    }

  }


}
