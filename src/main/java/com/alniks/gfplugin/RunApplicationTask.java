package com.alniks.gfplugin;

import groovy.lang.MissingPropertyException;
import java.io.File;
import org.glassfish.embeddable.GlassFishException;
import org.gradle.api.GradleException;
import org.gradle.api.UnknownTaskException;
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
        ProgressLogger progressLogger = getServices().get(ProgressLoggerFactory.class).newOperation(StopApplicationTask.class);
        progressLogger.setDescription("Start GlassFish server");
        progressLogger.setShortDescription("Starting gf");
        progressLogger.started();
        GlassFishExtension extension = (GlassFishExtension) getProject().getExtensions().findByName(GlassFishPlugin.EXTENSION_NAME);
        getLogger().info("extention in task " + extension);
        initFromExtension(extension);
        GlassFishRunner runner = new GlassFishRunner(port);
        getLogger().info("starting GlassFish server");
        try {
            runner.start();
            getLogger().info("server started");
            if (files == null) {
                deploy(runner, (File) getProject().getTasks().getByName(WarPlugin.WAR_TASK_NAME).property("archivePath"));
            } else {
                for (File app : files) {
                    deploy(runner, app);
                }
            }
            StopMonitor monitor = new StopMonitor(stopPort, runner, stopKey);
            monitor.start();
        } catch (GlassFishException | UnknownTaskException | MissingPropertyException ex) {
            getLogger().error("Error running glassfish", ex);
            try {
                runner.stop();
            } catch (GlassFishException ex1) {
                getLogger().error("Problen stopping glassfish", ex1);
            }
            throw new GradleException(ex.getMessage(), ex);
        } finally {
            progressLogger.completed();
        }
    }
    
    private void deploy(GlassFishRunner runner, File app) throws GlassFishException {
        getLogger().info("deploying application " + app);
        runner.deploy(app);
        getLogger().info("application deployed");
    }
    
}
