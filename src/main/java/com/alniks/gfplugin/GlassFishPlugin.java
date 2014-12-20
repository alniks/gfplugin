package com.alniks.gfplugin;

import com.alniks.gfplugin.tasks.RunApplicationTask;
import com.alniks.gfplugin.tasks.StopApplicationTask;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.plugins.WarPlugin;

/**
 *
 * @author Alex Saluk
 */
public class GlassFishPlugin implements Plugin<Project> {
    
    public static final String EXTENSION_NAME = "gf";

    @Override
    public void apply(Project p) {
        p.getPlugins().apply(WarPlugin.class);
        p.getExtensions().create(EXTENSION_NAME, GlassFishExtension.class);
        addTasks(p);
    }

    private void addTasks(Project p) {
        RunApplicationTask run = p.getTasks().create("runApplication", RunApplicationTask.class);
        run.setDescription("Deploys your war to an embedded gf");
        run.setGroup(WarPlugin.WEB_APP_GROUP);
        run.dependsOn(WarPlugin.WAR_TASK_NAME);
        StopApplicationTask stop = p.getTasks().create("stopApplication", StopApplicationTask.class);
        stop.setDescription("Stops embedded gf");
        stop.setGroup(WarPlugin.WEB_APP_GROUP);
    }
    
}
