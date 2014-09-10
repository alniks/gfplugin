package com.alniks.gfplugin;

import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.plugins.WarPlugin;

/**
 *
 * @author Alex Saluk
 */
public class GlassFishPlugin implements Plugin<Project> {
    
    public static final String EXTENSION_NAME = "gfExtension";

    @Override
    public void apply(Project p) {
        p.getPlugins().apply(WarPlugin.class);
        p.getExtensions().create(EXTENSION_NAME, GlassFishExtension.class);
        addTasks(p);
    }

    private void addTasks(Project p) {
        p.getTasks().create("runApplication", RunApplicationTask.class);
        p.getTasks().create("stopApplication", StopApplicationTask.class);
    }
    
}
