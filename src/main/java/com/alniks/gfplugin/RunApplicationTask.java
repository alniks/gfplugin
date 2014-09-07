package com.alniks.gfplugin;

import java.io.File;
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
        GlassFishRunner runner = new GlassFishRunner(8080);//TODO configuration
        try {
            runner.start();
            File app = (File) getProject().getTasks().getByName(WarPlugin.WAR_TASK_NAME).property("archivePath");
            runner.deploy(app);
        } catch (GlassFishException ex) {
//            runner.stop();
            getLogger().error("Error running glassfish", ex);
            throw new GradleException(ex.getMessage());
        }
    }
    
}
