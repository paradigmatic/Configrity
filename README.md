# Configrity README file #

Configrity is a scala configuration library. Project aims at
simplicity, immutability, type safety, flexibility and humility.

## Features ##

### Implemented ###

  - Automatic values conversions via type classes.
  - Loading configurations from any source (File, InputStream, URL, etc.)
  - Loading configurations from system properties and environment variables.
  - Saving configurations to files.
  - Interoperability with java properties.
  - Hierarchical configuration blocks.
  - Configuration chaining (such as to provide default values).
  - Single import: `import configrity._` imports all you need.
  - Reader monad


### Planned ###
  
  - Loading from resources.
  - Implicit converters for: File, URL, URI and Color.
  - Export/Import formats: INI, JSON, XML, apache, plists, etc.

## Example ##

    import configrity._
    
    val config = Configuration.load( "server.conf" )

    val hostname = config[String]("host")
    val port = config[Int]("port")

    val updatedConfig = config.set("port",80)
    upddatedConfig.save( "local.conf" )	

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

## Documentation ##

See the wiki: <https://github.com/paradigmatic/Configrity/wiki>

A scaladoc reference will be generated when following the install procedure described above.

## License and ownership ##

Configrity is a free and open source library released under the
permissive GNU LesserGPLv3 license (see `LICENSE.txt`).

Copyright (C) 2011. Paradigmatic. All rights reserved.