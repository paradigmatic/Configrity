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

package configrity

import java.util.Properties

/**
 * Provides method for converting a Configuration in a java.util.Properties
 * instance and the opposite. Both methods are defined as implicit conversions.
 */
object JProperties {

  /**
   * java.util.Properties plain text format.
   */
  val format = io.PropertiesFormat

  /**
   * Converts a java.util.Properties instance into a Configuration.
   */
  implicit def propertiesToConfiguration( props: Properties ) = {
    var values = Map[String,String]()
    val it = props.keySet.iterator
    while( it.hasNext ) {
      val key = it.next.asInstanceOf[String]
      values += key -> props.get(key).asInstanceOf[String]
    }
    Configuration( values )
  }

  /**
   * Converts a Configuration into a java.util.Properties instance.
   */
  implicit def configurationToProperties( config: Configuration ) = {
    val props = new Properties
    for( (k,v) <- config.data ) {
      props.put(k,v)
    }
    props
  }

}
