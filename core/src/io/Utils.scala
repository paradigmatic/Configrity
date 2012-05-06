/*
 Copyright (C) 2012, Paradigmatic <paradigmatic@streum.org>

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

package org.streum.configrity.io

object Utils {

  val Alphanum = """\w+""".r

  def sanitize( in: String ): String = in match {
    case Alphanum(out) => out
    case out => "\"" + out + "\""
  }

  def sanitize[A]( as: List[A] ): List[String] = 
    as.map( a => sanitize( a.toString ) )


}
