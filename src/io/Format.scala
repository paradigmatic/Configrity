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

/**
 * Format for converting a String into a Configuration
 */
trait ImportFormat {

  /**
   * Converts a string into a configuration
   */
  def fromText( s: String ): Configuration

}

/**
 * Format for converting a Configuration into a String.
 */
trait ExportFormat {

  /**
   * Converts a configuration into a string.
   */
  def toText( configuration: Configuration ): String

}

/**
 * Format able to convert Configuration to String and
 * String to Configuration
 */
trait Format extends ImportFormat with ExportFormat

