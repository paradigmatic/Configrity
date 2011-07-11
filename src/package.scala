package configrity


/** Package object containing the default converters + default formats */
object `package` extends DefaultConverters {

  val BlockFormat = io.BlockFormat

  val FlatFormat = io.FlatFormat
  
  /** Returns a reader which retrieves and converts a configuration
   * value. If the Configuration does not contain the value, an
   * Exception will be thrown.
   */
  def read[A: ValueConverter]( key: String ) = 
     ConfigurationReader[A](key, None)

  /* Returns a reader which retrieves and converts a configuration
   * value. If the Configuration does not contain the value, the
   * provided default value will be returned.
   */ 
  def read[A: ValueConverter]( key: String, default: A ) = 
     ConfigurationReader[A](key, Some(default) )
}

