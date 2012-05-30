package org.streum.configrity.conf4j

import org.streum.configrity.{Configuration=> SConfig}

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

}

object Configuration {

  def empty():Configuration = new Configuration( SConfig() )

  def fromProperties( props: java.util.Properties ) = {
    import org.streum.configrity.JProperties._
    val conf = propertiesToConfiguration( props )
    new Configuration( conf )
  }

}
