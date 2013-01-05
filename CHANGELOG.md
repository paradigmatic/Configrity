Changelog
=========

Next Version
------------

### Compatibility

  - Compatible with Scala 2.9.2 and 2.10.0 (thanks to Aki Saarinen).
  - **Dropping support for Scala 2.8.2**

### New features

  - Allow Configuration to carry its context name (issue #14 and #15) -- Pablo Lalloni.
  - Add `Configuration#detachAll` (issue #19) -- Jussi Virtanen.
  - Allow to parse "yes/no" into a Boolean (issue #20) -- Yuri Molchan

Version 0.10.2 - 2012-05-21
---------------------------

### Improved compatibility

  - Compatible with Scala 2.8.1+

### Bug fix

  - Formats can write (and read) empty string values (issue #13).


Version 0.10.1 - 2012-05-06
---------------------------

### Bug fix

  - List values are sanitized when added to a configuration (issue #12).

### Better exceptions

  - Loading a non-existing file from claspath produces `FileNotFoundException` 
    (issue #8) -- Martin Konicek
  - Include key in `NoSuchElementException` message (issue #11) -- Jussi Virtanen

Version 0.10.0 - 2012-02-12
---------------------------

Configrity v0.10.0 was refactored to allow a modular project layout.

### New features

 - YAML module, allowing to use YAML format for loading/saving configurations (issue #2).


Version 0.9.0 - 2011-12-08
--------------------------

### New features

  - Configurations can be loaded directly from the classpath using
    `Configuration.loadResource`.
  
### Minor feature

  - Better handling of lists when the configuration is built with
    key/values (issue #4)

### Code quality

  - Test pass on Windows (issue #3) -- Gerolf Seitz
    

Version 0.8.0 - 2011-09-04
--------------------------

### New feature

  - Flat and Block formats accept an include directive.

### Minor feature

  - `Configuration#set` formats lists directly to a convertible representation.
  - `BlockFormat` ignores empty blocks  

Version 0.7.0 - 2011-08-20
--------------------------

### New features

  - Parser accepts lists
  - List value converters
  - Extra converters: Color, File, URI, URL

Version 0.6.1 - 2011-08-18
--------------------------

### Build process

  - Moved to SBT 0.10.x


Version 0.6.0 - 2011-07-11
--------------------------

First stable version.

### Major Change

  - Changed package `configrity` to `org.streum.configrity`.

Version 0.5.0 - 2011-07-11
--------------------------

### Major Change

  - Adoption of Map semantic.

### New features

  - Value converters fo bytes, shots, longs and floats.
  - Reader monads
  - A configuration can include another one
  - Better Configuration factory methods

### Bugfix

  - Empty blocks parsing throw an exception in BlockFormat

Version 0.4.0 - 2011-07-04
--------------------------

### Licensing 

  Configrity is now licensed with the LGPL

### New features 

  - Hierarchical BlockFormat
  - Block manipulations (attach and detach)

### Code quality 

  - Block and Flat formats have common behavior in common Trait

Version 0.3.0 - 2011-07-02 
--------------------------

### New features 
    
  - Interconversions with java.util.Properties
  - java.util.Properties Format

Version 0.2.0 - 2011-07-01 
--------------------------

### New features 
    
  - FlatFormat
  - From and to string conversion
  . Load and save to files and scala.io.Source
  - Pretty text formatting

### Code quality 

  - 100% test coverage with SCCT

Version 0.1.0 
-------------

  - Most basic features.
