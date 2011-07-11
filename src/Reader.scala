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

trait Reader[A] {

  def apply( c: Configuration ): A
  def map[B]( f: A => B ) = {
    val parent = this
    new Reader[B] {
      def apply( c: Configuration ) = f( parent(c) )
    }
  }
  def flatMap[B]( f: A => Reader[B] ) = {
    val parent = this
    new Reader[B] {
      def apply( c: Configuration ) = f( parent(c) )(c)
    }
  }

}

case class ConfigurationReader[A: ValueConverter](
  key: String, 
  default: Option[A]
) extends Reader[A] {
  def apply( c: Configuration ) = if( default.isDefined ) {
    c.get[A](key).getOrElse( default.get )
  } else {
    c[A](key)
  }
}
