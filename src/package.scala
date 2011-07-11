package configrity


/** Package object containing the default converters + default formats */
object `package` extends DefaultConverters {

  val BlockFormat = io.BlockFormat

  val FlatFormat = io.FlatFormat
  
  def read[A: ValueConverter]( key: String ) = 
     ConfigurationReader[A](key, None)

  def read[A: ValueConverter]( key: String, default: A ) = 
     ConfigurationReader[A](key, Some(default) )
}

