package com.alniks.gfplugin;

import org.gradle.api.tasks.TaskAction;

/**
 *
 * @author Alniks
 */
public class PrintInfoTask extends GlassFishTask {
    
    @TaskAction
    public void pringInfo() {
        System.out.println("runnig task");
    }
    
}
