package com.alniks.gfplugin;

import groovy.lang.Closure;
import org.gradle.api.*;
import org.gradle.api.plugins.*;
import org.gradle.testfixtures.ProjectBuilder;
import spock.lang.Specification;

/**
 *
 * @author Alex Saluk
 */
class GlassFishPluginTest extends Specification {
    
    static final RUN_APP_TASK_NAME = 'runApplication'
    static final STOP_APP_TASK_NAME = 'stopApplication'

    Project project
    def setup() {
        project = ProjectBuilder.builder().build();
    }

    def "Applies plugin and extension values"() {
        expect: 
            project.tasks.findByName(RUN_APP_TASK_NAME) == null
            project.tasks.findByName(STOP_APP_TASK_NAME) == null
            project.tasks.findByName(REDEPLOY_APP_TASK_NAME) == null
        when: 
            project.apply plugin: 'gf'
            project.gf {
                listenPort = 12345
                stopKey = 'sKey'
                redeployKey = 'rKey'
                port = 321
            }
        then:
            project.plugins.hasPlugin(WarPlugin)
            project.tasks.findByName(RUN_APP_TASK_NAME) != null
            
            Task run = project.tasks.findByName(RUN_APP_TASK_NAME)
            run != null
            run.description == 'Deploys your war to an embedded gf'
            run.group == WarPlugin.WEB_APP_GROUP
            run.runApplication()
            run.listenPort == 12345
            run.stopKey == 'sKey'
            run.port == 321
            
            Task stop = project.tasks.findByName(STOP_APP_TASK_NAME)
            stop != null
            stop.description == 'Stops embedded gf'
            stop.group == WarPlugin.WEB_APP_GROUP
            stop.stop()
            stop.listenPort == 12345
            stop.stopKey == 'sKey'
            stop.port == 321
            
    }
    
}
