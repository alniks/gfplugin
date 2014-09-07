package com.alniks.gfplugin;

import org.gradle.api.tasks.TaskAction;

/**
 *
 * @author Alex Saluk
 */
public class PrintInfoTask extends GlassFishTask {
    
    @TaskAction
    public void pringInfo() {
        getLogger().quiet("printing from plugin");
    }
    
}
