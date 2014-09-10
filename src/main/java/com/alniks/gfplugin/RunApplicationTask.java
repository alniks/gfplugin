package com.alniks.gfplugin;

import java.io.File;
import java.io.IOException;
import org.glassfish.embeddable.GlassFishException;
import org.gradle.api.GradleException;
import org.gradle.api.plugins.WarPlugin;
import org.gradle.api.tasks.TaskAction;

/**
 *
 * @author Alex Saluk
 */
public class RunApplicationTask extends GlassFishTask {
    
    
    @TaskAction
    public void runApplication() {
        //TODO add progress?
        initFromExtension();
        GlassFishRunner runner = new GlassFishRunner(port);
        getLogger().info("starting GlassFish server");
        try {
            runner.start();
            getLogger().info("server started");
            File app = (File) getProject().getTasks().getByName(WarPlugin.WAR_TASK_NAME).property("archivePath");
            getLogger().info("deploying application " + app);
            runner.deploy(app);
            getLogger().info("application deployed");
            StopMonitor monitor = new StopMonitor(stopPort, runner, stopKey);
            monitor.start();
        } catch (GlassFishException | IOException ex) {
            getLogger().error("Error running glassfish", ex);
            throw new GradleException(ex.getMessage(), ex);
        }
    }
    
}
