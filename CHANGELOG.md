Changelog
=========

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
