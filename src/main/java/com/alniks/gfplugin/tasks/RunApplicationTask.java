package com.alniks.gfplugin.tasks;

import com.alniks.gfplugin.GFTasks;
import com.alniks.gfplugin.GlassFishExtension;
import com.alniks.gfplugin.GlassFishPlugin;
import com.alniks.gfplugin.GlassFishRunner;
import com.alniks.gfplugin.Monitor;
import com.alniks.gfplugin.RunnerException;
import groovy.lang.MissingPropertyException;
import org.gradle.api.GradleException;
import org.gradle.api.UnknownTaskException;
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
        getLogger().info("extention in task {}", extension);
        initFromExtension(extension);
        GlassFishRunner runner = new GlassFishRunner(port);
        getLogger().info("starting GlassFish server");
        try {
            runner.start();
            getLogger().info("server started");
            getFiles().forEach(f -> runner.deploy(f));
            Monitor monitor = new Monitor(listenPort, runner, configureTasks());
            monitor.start();
        } catch (RunnerException | UnknownTaskException | MissingPropertyException ex) {
            getLogger().error("Error running glassfish", ex);
            runner.stop();
            throw new GradleException(ex.getMessage(), ex);
        } finally {
            progressLogger.completed();
        }
    }
    
    private GFTasks configureTasks() {
        GFTasks tasks = new GFTasks();
        tasks.addTask(stopKey, r -> r.stop());
        tasks.addTask(redeployKey, r -> r.redeploy(getFiles())); 
        return tasks;
    }
        
}
