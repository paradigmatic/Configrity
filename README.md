# Configrity README file #

Configrity is a scala configuration library. Project aims at
simplicity, immutability, type safety and flexibility.

## Features ##

### Implemented ###

  - Automatic values conversions via type classes.
  - Configuration from system properties and environment variables.

### Planned ###

  - Interoperability with java properties.
  - Hierarchical configuration blocks.
  - Loading configuration via scala io sources.
  - Configuration chaining (such as to provide default values).

## Installation ##

To install Configrity you just need a worling java installation (tested with
JDK 1.6, but should work with 1.5) and SBT.

    $ git clone git://github.com/paradigmatic/Configrity.git
    $ cd Configrity
    $ sbt
    > update
    > test
    > doc
    > package

## Usage ##

### Accessing a configuration value ###

If the configuration instance `config` represents the configuration:

    name    = Bob
    age     = 42
    married = true

The values can be retrieved with:

    val name = config[String]( "name" )         // name == Some("Bob")
    val age = config[Int]( "age" )              // age == Some(42)
    val isMarried = config[Boolean]( "married" ) // isMarried == Some(true)
    val hasChildren = config[Boolean]( "children" ) // hasChildren == None

An option is returned, defined only if the value if the key
exists. The underlying value can also be directly retrived when
a default value is passed as a second argument:

    val isMarried = config[Boolean]( "married", false ) // isMarried == true
    val hasChildren = config[Boolean]( "children", false ) // hasChildren == false



