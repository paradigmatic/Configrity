/*
 This file is part of Configrity.
 
 Configrity is free software: you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.
 
 Configrity is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.
 
 You should have received a copy of the GNU General Public License
 along with Configrity.  If not, see <http://www.gnu.org/licenses/>.
*/

package configrity.io

import configrity.Configuration
import configrity.ValueConverters._

import scala.collection.mutable.StringBuilder
import scala.util.parsing.combinator._

/**
 * Encodes the default block format
 */
object BlockFormat extends ImportFormat {
  

  /*def toText( configuration: Configuration ) = {
    val out = new StringBuilder
    val data = configuration.data
    for( k <- data.keySet.toList.sorted ) {
      out.append(k).append(" = ").append( data(k) ).append(sep)
    }
    out.toString
  }*/

  def fromText( s: String ) = new Parser().parse( s )

  /** Parser exceptions */
  case class ParserException(s: String) extends Exception(s)

  /** Parser for FlatFormat */
  class Parser extends RegexParsers {

    private var blocks = List[String]()

    def addPrefix( lst: List[(String,String)] ) = {
      val prefix = blocks.head + dot
      for( (k,v) <- lst ) yield {
        ( prefix + k, v ) 
      }
    }

    override val whiteSpace = """(\s+|#[^\n]*\n)+""".r
    def key: Parser[String] = """([^=\s])+""".r
    val lineSep = "\n"
    val dot = "."
    def value: Parser[String] = """([^=\n#])*""".r ^^ { _.trim }
    def equals: Parser[String]  = "="
    val openBrace = "{"
    val closeBrace = "}"

    def entry = key ~ equals ~ value ^^ {
      case k ~ _ ~ v  => List( (k,v) )
    }

    def blocOrEntry:Parser[List[(String,String)]] = block | entry
    
    def content = rep( blocOrEntry ) ^^ {  _.flatten } 
    
    def blockStart: Parser[Unit] = key ~ openBrace ^^ {
      case k ~ _ => blocks ::= k
    }

    def block = blockStart ~ content ~ closeBrace ^^ {
      case _ ~ lst ~ _ => {
        val newLst = addPrefix( lst )
        blocks = blocks.tail
        newLst
      }
    }
   
    def parse( in: String )  = {
      parseAll(content, in) match {
        case Success( lst , _ ) => Configuration( lst.toMap )
        case x: NoSuccess => throw ParserException(x.toString)
      }
    }

  }


} 
