package configrity

/**
 * A Configuration class stores and allow access to configuration data. Although
 * immutable, several methods allow to easily change configuration data, returning
 * a new Configuration instance.
 */
case class Configuration( data: Map[String,String] ) {

  /**
   * Retrieve and convert configuration data in the wanted type. Returns None
   * if the data is not defined and Some(x) else. The conversion is done by
   * a ValueConverter instance which should be provided or implicitly defined
   * elsewhere.
   */
  def apply[A]( key: String )( implicit converter: ValueConverter[A] ) =
    converter( data get key )

  /**
   * Retrieve and convert configuration data in the wanted type. The user
   * must supply a default value, returned is the key is not found.
   */
  def apply[A]( key: String, default: A )( implicit converter: ValueConverter[A] ) =
    converter( data get key ) getOrElse default

  /**
   * Sets a new configuration value. If the key already exists, previous value
   * is replaced.
   */
  def set[A]( key: String, a: A ) =
    Configuration( data + ( key -> a.toString ) )

  /**
   * Removes a configuration value. No effect if the value was not previously
   * defined.
   */
  def clear[A]( key: String ) = 
    if( data contains key ) 
      Configuration( data - key )
    else
      this

}


/** Configuration companion object */

object Configuration {

  /** Returns the environement variables as a Configuration */
  def environement = Configuration( sys.env )

  /** Returns the system properties as a Configuration */
  def systemProperties = Configuration( sys.props.toMap ) 


}
