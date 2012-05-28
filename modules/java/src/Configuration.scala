package org.streum.configrity.conf4j;

import org.streum.configrity.{Configuration=> SConfig}

class Configuration private ( sconfig: SConfig ) {

  def set( key: String, value: Object ): Configuration = 
    new Configuration( sconfig.set(key,value.toString) )
  
  def get( key: String ): String = sconfig[String]( key )

  def contains( key: String ): Boolean = sconfig.contains(key)
  

}

object Configuration {

  def empty():Configuration = new Configuration( SConfig() )


}
