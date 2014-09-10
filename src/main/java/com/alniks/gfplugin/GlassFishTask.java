package com.alniks.gfplugin;

import org.gradle.api.DefaultTask;

/**
 *
 * @author Alniks
 */
public abstract class GlassFishTask extends DefaultTask {
    
    protected int stopPort = 5555;
    protected String stopKey = "stopKey";
    protected int port = 8080;
    
    protected void initFromExtension() {
        GlassFishExtension extension = (GlassFishExtension) getProject().getExtensions().getByName(GlassFishPlugin.EXTENSION_NAME);
        stopPort = extension.getStopPort();
        stopKey = extension.getStopKey();
        port = extension.getPort();
    }
    
}
