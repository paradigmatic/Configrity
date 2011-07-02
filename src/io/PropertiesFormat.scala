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
import configrity.JProperties._
import java.io.StringWriter
import java.io.StringReader
import java.util.Properties

/**
 * Text format described by java.util.Properties javadoc
 */
object PropertiesFormat extends Format {

  def toText( config: Configuration ) = {
    val out = new StringWriter
    config.store( out, "")
    out.close
    out.toString
  }

  def fromText( s: String ) = {
    val in = new StringReader( s )
    val props = new Properties
    props.load( in )
    in.close
    props: Configuration
  }

}
