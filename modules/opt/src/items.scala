package org.streum.configrity.opt

trait Item {}

case class Flag( 
  name: String, 
  shortName: Option[String], 
  description: String
) extends Item

case class Opt( 
  name: String,
  shortName: Option[String], 
  argumentName: Option[String],
  description:  String
) extends Item

case class Argument( 
  name: String,
  description:  String
) extends Item
