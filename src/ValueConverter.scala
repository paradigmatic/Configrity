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

/**
 * Parse and convert an Option[String] into an Option[A].
 */

trait ValueConverter[A] {
  /**
   * Defines how to parse the string.
   */
  def parse( s: String ): A

  /**
   * Parse the string if defined or return None
   */
  def apply( s: Option[String] ) = s map parse
}

/**
 * Ease the creation of a value converter. See the ValueConverter.scala source
 * file for examples.
 */
object ValueConverter {
  def apply[A]( f: String => A ) = new ValueConverter[A] {
    def parse( s: String ) = f(s)
  }
}

/**
 * Several predefined value converters for basic types: Int, Double, Boolean, etc.
 */
trait DefaultConverters {

  /**
   * Converts a string, to itself... Well just an identity converter.
   */
  implicit val stringConverter = ValueConverter[String]( s => s )

  /**
   * Convert strings to bytes.
   */
  implicit val byteConverter = ValueConverter[Byte]( 
    s => java.lang.Byte.parseByte(s)
  )

  /**
   * Convert strings to shorts.
   */
  implicit val shortConverter = ValueConverter[Short]( 
    s => java.lang.Short.parseShort(s)
  )

  /**
   * Convert strings to ints.
   */
  implicit val intConverter = ValueConverter[Int]( 
    s => java.lang.Integer.parseInt(s)
  )

  /**
   * Convert strings to longs.
   */
  implicit val longConverter = ValueConverter[Long]( 
    s => java.lang.Long.parseLong(s)
  )

  /**
   * Convert strings to floats.
   */
  implicit val floatConverter = ValueConverter[Float]( 
    s => java.lang.Float.parseFloat(s)
  )

  /**
   * Convert strings to doubles.
   */
  implicit val doubleConverter = ValueConverter[Double]( 
    s => java.lang.Double.parseDouble(s)
  )

  /**
   * Convert strings to Booleans. The strings values: "T", "true" and "on"
   * will be converted to true and the strings: "F", "false" and "off" will
   * be converted to false.
   */
  implicit val booleanConverter = BooleanConverter
  

  object BooleanConverter extends ValueConverter[Boolean] {
    val trues = Set("T","true","on")
    val falses = Set("F","false","off" )
    def parse( s: String ) = {
      if( trues contains s ) true
      else if ( falses contains s ) false
      else throw new IllegalArgumentException( 
        s + " could not be converted in to a Boolean" 
      )
    }
  }

}
