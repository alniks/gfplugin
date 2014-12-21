GlassFish plugin
========
A gradle plugin for easy deployment of applications to the embedded GlassFish application server.

Usage
=====
This plugin provides two tasks "runApplication" and "stopApplication".  Executing runApplication 
will deploy a file produced by gradle WarPlugin in an embedded GlassFish instance running
on localhost, port 8080 on default. "stopApplication" shuts down running instance of GlassFish.

The easiest way to apply this plugin to Gradle projects is to use the published plugin jars in the artifact 
repository at https://raw.githubusercontent.com/alniks/gfplugin/master/repo.  
To do so, add the following lines to a Gradle build file:

    buildscript {
      repositories {
        maven {
         url uri('https://raw.githubusercontent.com/alniks/gfplugin/master/repo')
        }
      }

      dependencies {
        classpath group: 'com.alniks',
                   name: 'gf-plugin',
                version: '1.0'
      }
    }

    apply plugin: 'gf'
    
  You can configure listen port for stopApplication task (default is 5555), stop key (default "stopKey"), 
  port the GlasFish will be listening to (default is 8080), and files to deploy by adding configuration:
  
    gf {
      listenPort = 12345
      stopKey = "stopKey"
      port = 8080
      files = [war.archivePath]
  }
  
Copyright and License
=====

Copyright 2014 (c) Alex Saluk

All versions, present and past, of GlassFish plugin are licensed under GNU General Public License (http://www.gnu.org/licenses/licenses.en.html).
