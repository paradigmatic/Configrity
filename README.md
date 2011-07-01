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

The values can be retrieved with the `apply` method:

    import configrity.ValueConverters._
    val name = config[String]( "name" )         // name == Some("Bob")
    val age = config[Int]( "age" )              // age == Some(42)
    val isMarried = config[Boolean]( "married" ) // isMarried == Some(true)
    val hasChildren = config[Boolean]( "children" ) // hasChildren == None

An option is returned, defined only if the key exists. The underlying
value can also be directly retrived when a default value is passed as
a second argument:

    val isMarried = config( "married", false ) // isMarried == true
    val hasChildren = config( "children", false ) // hasChildren == false

The type will be inferred from the default value.

When accessing a value, the conversion is realized by an implicit
`ValueConverter`. They are already defined for most basic types in
`ValueConverters` (hence the import in the first example). See below
to learn how to write converters for pther types.

### Setting values ###

A Configuration can be modified using the `set` method. It either creates 
a new value or replace silently an existing one. Since Configuration is
immutable, a new instance will be returned:

    val config2 = config.set("name","Robert").set("children","true")
    val name = config2[String]( "name" )         // name == Some("Robert")
    val hasChildren = config2[Boolean]( "children" ) // hasChildren == Some(true)

### Removing values ###

The method `clear` allows to remove an existing key. If the key does not exist,
nothing will happen:

    val config2 = config.clear( "name" ).clear( "address" )
    val name = config2[String]( "name" )         // name == None

### System properties and environment variables ###

Configurations can be created from the current system properties or the environement
variables:

    val sysProp = Configuration.systemProperties
    val sep = sysProp[String]("line.separator") // sep == Some("\n")
    val env = Configuration.environment
    val homeDir = env("HOME"  "/tmp") // home == "/home/paradigmatic"

### Defining a ValueConverter ###

Value converters can be defined easily by users for specific types. For example, you
can define a converter which retrieves a `java.io.File` with:

    implicit val fileConverter = 
      ValueConverter[File]( s => new File(s) )	       

In your code, you can then directly get files from configurations:

   val env = Configuration.environment
   val homeDir = env[File]( "HOME" ) 
       // homeDir == Some(new File("/home/paradigmatic"))
   
or:

    val homeDir = env( "HOME", new File( "/tmp" ) )
       // homeDir == Some(new File("/home/paradigmatic"))

See `src/ValueConverter.scala` for other examples.

== License ==

GPL 3. See `LICENSE.txt`.