package com.alniks.gfplugin;

import java.io.File;
import java.util.List;
import org.gradle.api.DefaultTask;

/**
 *
 * @author Alniks
 */
public abstract class GlassFishTask extends DefaultTask {
    
    protected int stopPort = 5555;
    protected String stopKey = "stopKey";
    protected int port = 8080;
    protected List<File> files;
    
    public void initFromExtension(GlassFishExtension extension) {
        stopPort = extension.getStopPort();
        stopKey = extension.getStopKey();
        port = extension.getPort();
        files = extension.getFiles();
        getLogger().info("{} received following extention {}", getClass(), extension);
    }
    
}
