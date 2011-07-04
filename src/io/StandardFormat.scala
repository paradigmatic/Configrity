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
 * Encodes the common behavior of standard formats
 */
trait StandardFormat extends Format {
  
  def parser: Parser

  def fromText( s: String ) = parser.parse( s )


  trait Parser extends RegexParsers {

    private var blocks = List[String]()

    private def unquote( s: String ) = s.substring( 1, s.size - 1 )

    override val whiteSpace = """(\s+|#[^\n]*\n)+""".r
    def key = """([^=\s])+""".r 
    val lineSep = "\n"
    def word = """([^=\s\n#\{\}\"])+""".r 
    def quoted = """"([^"]*)"""".r /*"*/ ^^ { unquote }
    val equals  = "="

    def value = word | quoted

    def entry = key ~ equals ~ value ^^ {
      case k ~ _ ~ v  => List( (k,v) )
    }

    def content: Parser[List[(String,String)]]
    
    def parse( in: String )  = {
      parseAll(content, in) match {
        case Success( lst , _ ) => Configuration( lst.toMap )
        case x: NoSuccess => throw StandardFormat.ParserException(x.toString)
      }
    }

  }


} 

object StandardFormat {

  /** Parser exceptions */
  case class ParserException(s: String) extends Exception(s)

}
