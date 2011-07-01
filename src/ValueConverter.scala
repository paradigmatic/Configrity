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
object ValueConverters {

  /**
   * Converts a string, into itself... Well just an identity converter.
   */
  implicit val stringConverter = ValueConverter[String]( s => s )

  /**
   * Convert strings into ints.
   */
  implicit val intConverter = ValueConverter[Int]( 
    s => java.lang.Integer.parseInt(s)
  )

  /**
   * Convert strings into doubles.
   */
  implicit val doubleConverter = ValueConverter[Double]( 
    s => java.lang.Double.parseDouble(s)
  )

  /**
   * Convert strings into Booleans. The strings values: "T", "true" and "on"
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
        s + " could not be converted in into a Boolean" 
      )
    }
  }

}
