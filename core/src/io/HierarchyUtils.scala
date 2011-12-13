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

package org.streum.configrity.io

/**
 * Useful methods for implementing hierarchical output formats.
 */

trait HierarchyUtils {

  private def splitKey( s: String ) = s.split("""\.""").toList
  private def joinKey( ss: List[String] ) = ss.mkString(".")

  def writeEntry( k:String, v:String, ind:Int, out:StringBuffer ): Unit
  def writeBlockStart( k:String, ind:Int, out:StringBuffer ): Unit
  def writeBlockEnd( k:String, ind:Int, out:StringBuffer ): Unit


  def writeHierarchy( 
    map: Map[String,String], 
    indents: Int = 0
  ): String = {
    val out = new StringBuffer
    var blocks = Map[String,Map[String,String]]()
    for( (k,v) <- map ) {
      splitKey(k) match {
        case el :: Nil => {
          writeEntry(k,v,indents,out)
        }
        case first :: rest => {
          val subKey = joinKey(rest)
          blocks +=  first -> 
          ( blocks.getOrElse( first, Map() ) + ( subKey -> v) )
        }
        case _ =>
      }
    }
    for( (k,block) <- blocks ) {
      writeBlockStart( k, indents, out )
      out.append( writeHierarchy( block, indents + 1 ) )
      writeBlockEnd( k, indents, out )
    }
    out.toString
  }


}
