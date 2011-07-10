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

package configrity

import configrity.io._
import scala.io.Source

/**
 * A Configuration class stores and allow access to configuration data. Although
 * immutable, several methods allow to easily change configuration data, returning
 * a new Configuration instance.
 */
case class Configuration( data: Map[String,String] ) {

  /**
   * Returns true if some value is associated with the
   * given key, else false.
   */
  def contains( key: String ) = data contains key

  /**
   * Retrieve and convert configuration data in the wanted type. Throws
   * an NoSuchElementException if the data is not defined. The conversion
   * is done by a ValueConverter instance which should be provided or
   * implicitly defined elsewhere.
   */
  def apply[A]( key: String )( implicit converter: ValueConverter[A] ) =
    get[A](key).get

  /**
   * Retrieve and convert configuration data in the wanted type. Returns None
   * if the data is not defined and Some(x) else. The conversion is done by
   * a ValueConverter instance which should be provided or implicitly defined
   * elsewhere.
   */
  def get[A]( key: String )( implicit converter: ValueConverter[A] ) =
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
  def format( fmt: ExportFormat = Configuration.defaultFormat ) = 
    fmt.toText( this )

  /** Saves the configuration to a file */
  def save( 
    file: java.io.File, 
    fmt: ExportFormat = Configuration.defaultFormat
  ) {
    val out = new java.io.PrintWriter( file )
    out.println( format(fmt) )
    out.close
  }

  /** Saves the configuration to a file */
  def save( filename: String, fmt: ExportFormat ): Unit =
    save( new java.io.File( filename ), fmt )

  /** Saves the configuration to a file */
  def save( filename: String ): Unit =
    save( new java.io.File( filename ) )

  /** Attach a configuration as a sub block. Existing entries with
   *  same keys will be replaced. Prefix should not end with a 'dot'.
   */
  def attach( prefix: String, block: Configuration ) = {
    require( 
      prefix.substring( prefix.length-1) != ".", 
      "Prefix should not end with a dot"
    )
    var nextData = Map[String,String]()
    for( (k,v) <- block.data ) {
      val nextKey = prefix + "." + k
      nextData += nextKey -> v
    }
    Configuration( data ++ nextData )
  }

  /** Detach all values whose keys have the given prefix as a new configuration.
   *  The initial configuration is not modified. The prefix is removed in the
   *  resulting configuration. An important property:
   *
   *  <pre>val c2 = c1.attach(prefix, c1.dettach( prefix )</pre>
   *
   *  The resulting configuration c2 should be equal to c1.
   */
  def dettach( prefix: String ) = {
    require( 
      prefix.substring( prefix.length-1) != ".", 
      "Prefix should not end with a dot"
    )
    val regexp = (prefix + """\.(.+)""" ).r
    var nextData = Map[String,String]()
    for( (k,v) <- data ) k match {
      case regexp( subkey ) =>  nextData += subkey -> v
      case _ => {}
    }
    Configuration( nextData )
  }

  /**
   * Adds another configuration values providing entries
   * which are not present in the present one. Useful
   * for defaulting to another Configuration
   */
  def include( config: Configuration ) = Configuration( config.data ++ data )

}


/** Configuration companion object */

object Configuration {

  /** By default, all conversions are done with BlockFormat */
  val defaultFormat = BlockFormat

  /** Creates a configuration from tuples of key,value */
  def apply( entries:(String,Any)* ):Configuration =
    Configuration( entries.map( t => (t._1,t._2.toString) ).toMap  )


  /** Returns the environement variables as a Configuration */
  def environment = Configuration( sys.env )

  /** Returns the system properties as a Configuration */
  def systemProperties = Configuration( sys.props.toMap ) 

  /** Instanciates a configuration file from a string using
   *  eventually a specified format. By default, the FlatFormat
   *  will be used.
   */
  def parse( s: String, fmt: ImportFormat = defaultFormat ) = 
    fmt.fromText( s )

  /**
   * Load a configuration from a scala.io.Source. An optional
   * format can be passed.
   */
  def load( source: Source,
           fmt: ImportFormat = defaultFormat ): Configuration = 
    fmt.fromText( source.mkString )

  /**
   * Load a configuration from a file specified by a filename.
   */
  def load( fileName: String )
  (implicit codec: scala.io.Codec): Configuration =
    load( Source.fromFile( fileName ) )

  /**
   * Load a configuration from a file specified by a filename and
   * using a given format.
   */
  def load( fileName: String, fmt: ImportFormat )
  (implicit codec: scala.io.Codec): Configuration =
    load( Source.fromFile( fileName ), fmt )


}
