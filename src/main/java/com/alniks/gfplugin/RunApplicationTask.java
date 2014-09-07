package com.alniks.gfplugin;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import org.glassfish.embeddable.GlassFishException;
import org.gradle.api.GradleException;
import org.gradle.api.plugins.WarPlugin;
import org.gradle.api.tasks.TaskAction;

/**
 *
 * @author Alex Saluk
 */
public class RunApplicationTask extends GlassFishTask {
    
    private final static String STOP_KEY = "x";
    
    @TaskAction
    public void runApplication() {
        GlassFishRunner runner = new GlassFishRunner(8080);//TODO configuration
        getLogger().info("starting GlassFish server");
        try {
            runner.start();
            getLogger().info("server started");
            File app = (File) getProject().getTasks().getByName(WarPlugin.WAR_TASK_NAME).property("archivePath");
            //TODO redeploy
            getLogger().info("deploying application " + app);
            runner.deploy(app);
            getLogger().info("application deployed");
            System.out.println("press " + STOP_KEY + " to exit" );
            while (true) { //TODO
                String key;
                try {
                    key = new BufferedReader(new InputStreamReader(System.in)).readLine();
                }
                catch (IOException ex) {
                    throw new GradleException(ex.getMessage(), ex);
                }
                getLogger().info("received command: " + key);
                if (STOP_KEY.equals(key)) {
                    getLogger().info("stopping GlassFish server");
                    runner.stop();
                    getLogger().info("server stopped");
                    break;
                }
            }
        } catch (GlassFishException ex) {
            getLogger().error("Error running glassfish", ex);
            throw new GradleException(ex.getMessage(), ex);
        }
    }
    
}
