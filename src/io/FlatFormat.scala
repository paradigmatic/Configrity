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
 * Encodes a simple flat text format. Composed of:
 * 
 * key1 = value1<br/>
 * key2 = value2<br/>
 * ...
 * 
 * where keys and values cannot contain an '=' character. Keys
 * cannot contain whitespaces, while values can.
 * Whitespaces elsewhere are ignored.
 * Comment should start with '#' and are ignored.
 */
object FlatFormat extends StandardFormat {
  
  val sep = "\n"

  def toText( configuration: Configuration ) = {
    val out = new StringBuilder
    val data = configuration.data
    for( k <- data.keySet.toList.sorted ) {
      out.append(k).append(" = ").append( data(k) ).append(sep)
    }
    out.toString
  }

  def parser = FlatParser

  /** Parser for FlatFormat */
  object FlatParser extends Parser {
    def content = rep( entry ) ^^ {  _.flatten }
  }

} 
