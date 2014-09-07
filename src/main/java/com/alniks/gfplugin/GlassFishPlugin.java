package com.alniks.gfplugin;

import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.plugins.WarPlugin;

/**
 *
 * @author Alniks
 */
public class GlassFishPlugin implements Plugin<Project>{

    @Override
    public void apply(Project p) {
        p.getPlugins().apply(WarPlugin.class);
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
