/*
 This file is part of Configrity.
 
 Foobar is free software: you can redistribute it and/or modify
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

/**
 * Basic format:
 * key1 = value1
 * key2 = value2
 * etc.
 */
object FlatFormat extends ExportFormat {
  
  lazy val sep = Configuration.systemProperties("line.separator", "\n")

  def toText( configuration: Configuration ) = {
    val out = new StringBuilder
    val data = configuration.data
    for( k <- data.keySet.toList.sorted ) {
      out.append(k).append(" = ").append( data(k) ).append(sep)
    }
    out.toString
  }

}
