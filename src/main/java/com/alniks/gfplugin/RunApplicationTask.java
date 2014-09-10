package com.alniks.gfplugin;

import java.io.File;
import org.glassfish.embeddable.GlassFishException;
import org.gradle.api.GradleException;
import org.gradle.api.plugins.WarPlugin;
import org.gradle.api.tasks.TaskAction;
import org.gradle.logging.ProgressLogger;
import org.gradle.logging.ProgressLoggerFactory;

/**
 *
 * @author Alex Saluk
 */
public class RunApplicationTask extends GlassFishTask {
    
    
    @TaskAction
    public void runApplication() {
        initFromExtension();
        ProgressLogger progressLogger = getServices().get(ProgressLoggerFactory.class).newOperation(StopApplicationTask.class);
        progressLogger.setDescription("Start GlassFish server");
        progressLogger.setShortDescription("Starting gf");
        progressLogger.started();
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
        } catch (Exception ex) {
            getLogger().error("Error running glassfish", ex);
            try {
                runner.stop();
            } catch (GlassFishException ex1) {
                getLogger().error("Error running glassfish", ex1);
            }
            throw new GradleException(ex.getMessage(), ex);
        } finally {
            progressLogger.completed();
        }
    }
    
}
