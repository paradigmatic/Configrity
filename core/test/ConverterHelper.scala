package org.streum.configrity.test

import org.streum.configrity.converter._


object ConverterHelper {
  
  def convert[A]( opt: Option[String] )(implicit converter: ValueConverter[A] )  =
    converter(opt)

  def convert[A]( opt: String )(implicit converter: ValueConverter[A] )  =
    converter.parse(opt)

}
