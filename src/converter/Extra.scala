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

package org.streum.configrity.converter

import java.io.File
import java.awt.Color
import java.net.URL
import java.net.URI

/**
 * Extra value converters.
 */
trait Extra {
  
  /**
   * Converts a string to a java.io.File object.
   */
  implicit val fileConverter = ValueConverter[File]( new File(_) )

  /**
   * Converts a string to a java.awt.Color object. The string must be an hexadecimal
   * representation of the color (for instance #ffa0a0). The initial hash ('#') is
   * optional. The hexdigits must be exactly 6 and may be in upper or lower case.
   */
  implicit object ColorConverter extends ValueConverter[Color] {

    val HexColor = """#?([a-fA-F0-9]{6})""".r

    def parse( s: String ) = s match {
      case HexColor(c) => {
	val rgb = Integer.parseInt( c.toLowerCase, 16 )
	new Color( rgb )
      }
    }

  }

  /**
   * Converts a string to an URI.
   */
  implicit val URIConverter = ValueConverter[URI]( new URI(_) )

  /**
   * Converts a string to an URL.
   */
  implicit val URLConverter = URIConverter map ( _.toURL )

}


/*
 * Extra value converters.
 */
object Extra extends Extra
