user-id maven plugin
====================

This  plugin provides the user-id (UID) and user-gid (GID) of the user executing the build.
Due  to  the java system itself not providing this information but  only
the  username,  the plugin gets the property `user.name` of the  systems
properties.  This  unfortunately  break  the possibility  to  use  the
plugin for non-standard unix-environments.

WARNING
-------

Using  this plugin could couple your build tightly to your  environment.  
Please use it only if you really need it.

Example
-------

To enable the plugin add this to your build-plugins in your `pom.xml`.

``` xml
<plugin>
  <!-- Plugin only works with unix-like environments, please use it wisely! -->
  <groupId>org.jrichard.util</groupId>
  <artifactId>user-id-maven-plugin</artifactId>
  <version>1.0</version>
  <!-- (If you have an idea how I can omit the executions, please contact the author of this plugin) -->
  <executions>
    <execution>
      <goals>
        <goal>user-id</goal>
      </goals>
    </execution>
  </executions>
</plugin>
```

Now you can use `${user.id}` within your `pom.xml`.  

Configuration
-------------

You can adapt some different values to your need:

``` xml
<plugin>
  <!-- Plugin only works with unix-like environments, please use it wisely! -->
  <!-- This contains the default configuration for standard unix systems. -->
  <groupId>org.rjung.util</groupId>
  <artifactId>user-id-maven-plugin</artifactId>
  <version>1.0</version>
  <configuration>
    <uidPropertyName>user.uid</uidPropertyName>
    <gidPropertyName>user.gid</gidPropertyName>
  </configuration>
  <!-- (If you have an idea how I can omit the executions, please contact the author of this plugin) -->
  <executions>
    <execution>
      <goals>
        <goal>user-id</goal>
      </goals>
    </execution>
  </executions>
</plugin>
```

Links
-----

 - [Info](https://github.com/jrichard8/uid-maven-plugin)
 - [Github](https://github.com/jrichard8/uid-maven-plugin)
 - [Bugs](https://github.com/jrichard8/uid-maven-plugin/issues)
 - [![Build Status](https://github.com/jrichard8/uid-maven-plugin.svg?branch=master)](https://travis-ci.org/rynr/user-id-maven-plugin) [![Javadocs](https://www.javadoc.io/badge/org.rjung.util/user-id-maven-plugin.svg)](https://www.javadoc.io/doc/org.rjung.util/user-id-maven-plugin)