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

package configrity

import configrity.io._

/**
 * A Configuration class stores and allow access to configuration data. Although
 * immutable, several methods allow to easily change configuration data, returning
 * a new Configuration instance.
 */
case class Configuration( data: Map[String,String] ) {

  /**
   * Retrieve and convert configuration data in the wanted type. Returns None
   * if the data is not defined and Some(x) else. The conversion is done by
   * a ValueConverter instance which should be provided or implicitly defined
   * elsewhere.
   */
  def apply[A]( key: String )( implicit converter: ValueConverter[A] ) =
    converter( data get key )

  /**
   * Retrieve and convert configuration data in the wanted type. The user
   * must supply a default value, returned is the key is not found.
   */
  def apply[A]( key: String, default: A )( implicit converter: ValueConverter[A] ) =
    converter( data get key ) getOrElse default

  /**
   * Sets a new configuration value. If the key already exists, previous value
   * is replaced.
   */
  def set[A]( key: String, a: A ) =
    Configuration( data + ( key -> a.toString ) )

  /**
   * Removes a configuration value. No effect if the value was not previously
   * defined.
   */
  def clear[A]( key: String ) = 
    if( data contains key ) 
      Configuration( data - key )
    else
      this

  /**
   * Convert the map in a string using a provided export format.
   * By default, FlatFormat is used.
   */
  def format( fmt: ExportFormat ) = fmt.toText( this )

}


/** Configuration companion object */

object Configuration {

  /** Returns the environement variables as a Configuration */
  def environment = Configuration( sys.env )

  /** Returns the system properties as a Configuration */
  def systemProperties = Configuration( sys.props.toMap ) 

  /** Instanciates a configuration file from a string using
   *  eventually a specified format. By default, the FlatFormat
   *  will be used.
   */
  def from( s: String, fmt: ImportFormat = FlatFormat ) = 
    fmt.fromText( s )

}
