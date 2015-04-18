Bonjour, Jean-Luc,


Désolé de vous laisser croire qu'il peut y avoir un futur update sur votre projet Configrity...

Et donc de vous écrire par ce biais...

Je découvre depuis ne quinzaine de jours ces outils révolutionnaire de partage de code.

C'est géant, et ça peut vraiement aider beaucoup de monde...

... Anglophone !

Pour les autres, c'est là qu'il y a à faire d'après moi...

Donc, je recherche toute personne qui serait à même de collaborer dans le cadre de la vulgariasationd e l'utillisation de ces outils pour les 'non-anglophones'

J'ai installé:

https://git.c57.fr/explore

Si vous acceptéez de colalborer, n'hésitez pas ! <a href="http://board.concrete5.fr/viewtopic.php?p=4655#4655">"Plus on est de fous, plus il y de riz"</a> !


@ très bientôt, j'espère,


Lionel Adel.
<i>P.S.: Et vvous avez raison ! Cet outil devienbt de + en + un concurrent de Fb ;-) !</i>


----


# Configrity README file #

Configrity is a configuration library for Scala 2.10 or
+. Configuration instances are immutable, thread-safe and allow
functional design patterns, such as:

  - Getting options to values
  - Converting implicitly values via type-classes
  - Reader monad

The API is stable and covered by tests.

[![Build Status](https://secure.travis-ci.org/paradigmatic/Configrity.png)](http://travis-ci.org/paradigmatic/Configrity)

## Example ##

    import org.streum.configrity._
    
    val config = Configuration.load( "server.conf" )

    val hostname = config[String]("host")
    val port = config[Int]("port")

    val updatedConfig = config.set ("port",8080 )
    upddatedConfig.save( "local.conf" )	

## Features ##

### Implemented ###

  - Automatic values conversions via type classes.
  - Loading configurations from any source (File, InputStream, URL, etc.)
  - Loading configurations from system properties and environment variables.
  - Saving configurations to files.
  - Interoperability with java properties.
  - Hierarchical configuration blocks.
  - Configuration chaining (such as to provide default values).
  - Single import: `import org.streum.configrity._` imports all you need.
  - Access through reader monads
  - Values can be lists (v0.7.0)
  - Extra value converters: File, Color, URL, URI (v0.7.0)
  - `include` directive is supported, adding compatibility with Akka and
     Configgy configurations files.
  - Loading configurations from classpath/resources (v0.9.0)
  - YAML format support (v0.10.0)

### Planned ###
  
  - More export/Import formats: INI, JSON, XML, apache, etc.
  - Build configurations from command line arguments

## Requirements ##

Configrity depends on Scala 2.10 or newer.

While core features do not depend on external libraries, some modules
may. In that case, the approriate dependencies will be automatically
installed by sbt/maven.

## Installation ##

### From SBT ###

The dependency line is:

    "org.streum" %% "configrity-core" % "1.0.0"

Additional modules require addition dependency lines. Check the wiki
for more information:
<https://github.com/paradigmatic/Configrity/wiki/Modules>

### From Maven, Buildr, Ivy, Grape and Grails ###

Follow the [repository instructions](http://search.maven.org/#artifactdetails|org.streum|configrity-core_2.10|1.0.0|jar).
    

### From source (master branch) ###

To install Configrity you just need a working java installation (tested with
JDK 1.6 and 1.7, but may work with 1.5) and SBT **0.13.5**:

    $ git clone git://github.com/paradigmatic/Configrity.git
    $ cd Configrity
    $ sbt
    > test
    > doc
    > package


## Documentation ##

See the wiki: <https://github.com/paradigmatic/Configrity/wiki>

A scaladoc reference can be found online: 
<http://paradigmatic.github.com/Configrity/>

## Collaboration

### Reporting bugs and asking for features

You can github issues tracker, to report bugs or to ask for new features:

https://github.com/paradigmatic/Configrity/issues

### Submitting patches

Patches are gladly accepted from their original author. Along with any
patches, please state that the patch is your original work and that
you license the work to the Configrity project under the LGPLv3 or
a compatible license.

To propose a patch, fork the project and send a pull request via
github. Tests are appreciated.

## License and ownership ##

Configrity is a free and open source library released under the
permissive GNU LesserGPLv3 license (see `LICENSE.txt`).

Copyright (C) 2011-2014. Paradigmatic. All rights reserved.
