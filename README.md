# Configrity README file #

Configrity is a scala configuration library. Project aims at
simplicity, immutability, type safety, flexibility and humility.

## Features ##

### Implemented ###

  - Automatic values conversions via type classes.
  - Loading configurations for a file or a source (InputStream, URL, etc.)
  - Loading configurations from system properties and environment variables.
  - Saving configurations to files.
  - Interoperability with java properties.

### Planned ###

  - Hierarchical configuration blocks.
  - Configuration chaining (such as to provide default values).
  - Export/Import formats: plists, JSON, XML, apache, etc.

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

## Example ##

    import configrity.Configuration._
    import configrity.ValueConverters._
    
    val config = Configuration.load( "server.conf" )

    val hostname = config[String]("host")
    val port = config[Int]("port")

    val updatedConfig = config.set("port",80)
    upddatedConfig.save( "local.conf" )	

## Documentation ##

See the wiki: <https://github.com/paradigmatic/Configrity/wiki>

A scaladoc reference will be generated when following the install procedure described above.


## License ##

GPL 3. See `LICENSE.txt`.

## License and ownership ##

Configrity is an free open source library released under GNU GPLv3 license (see `LICENSE.txt`).

Copyright Paradigmatic, 2011. All rights reserved.