package org.streum.configrity.conf4j

import org.streum.configrity.{Configuration=> SConfig}
import org.streum.configrity.io.ImportFormat

class Configuration private ( sconfig: SConfig ) {

  def set( key: String, value: Object ): Configuration = 
    new Configuration( sconfig.set(key,value.toString) )
  
  def get( key: String ): String = sconfig[String]( key )

  def get( key: String, default: String ): String = sconfig( key, default )

  def getInt( key: String ): Int = sconfig[Int]( key )

  def getInt( key: String, default: Int ): Int = sconfig( key, default )

  def getDouble( key: String ): Double = sconfig[Double]( key )

  def getDouble( key: String, default: Double ): Double = sconfig( key, default )

  def getBoolean( key: String ): Boolean = sconfig[Boolean]( key )

  def getBoolean( key: String, default: Boolean ): Boolean = sconfig( key, default )
  
  def contains( key: String ): Boolean = sconfig.contains(key)
  
  def toProperties: java.util.Properties = {
    import org.streum.configrity.JProperties._
    configurationToProperties( sconfig )
  }

  def toScala: SConfig = sconfig

  override def equals( that: Any ): Boolean = that match {
    case conf: Configuration => sconfig == conf.toScala
    case _ => false
  }

  override def hashCode: Int = sconfig.hashCode

}

object Formats {
  import org.streum.configrity.io._
  val blockFormat = BlockFormat
  val flatFormat = FlatFormat
}

object Configuration {

  def empty():Configuration = new Configuration( SConfig() )

  def fromProperties( props: java.util.Properties ) = {
    import org.streum.configrity.JProperties._
    val conf = propertiesToConfiguration( props )
    new Configuration( conf )
  }

  def load( filename: String,  format: ImportFormat ): Configuration = 
    load( new java.io.File(filename), format )

  def load( file: java.io.File, format: ImportFormat ): Configuration = {
    val conf = SConfig.load( io.Source.fromFile( file ), format ) 
    new Configuration(conf)
  }

}
