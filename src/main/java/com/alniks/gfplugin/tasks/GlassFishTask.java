package com.alniks.gfplugin.tasks;

import com.alniks.gfplugin.GlassFishExtension;
import java.io.File;
import java.util.List;
import java.util.stream.Stream;
import org.gradle.api.DefaultTask;
import org.gradle.api.plugins.WarPlugin;

/**
 *
 * @author Alniks
 */
public abstract class GlassFishTask extends DefaultTask {
    
    protected int listenPort = 5555;
    protected String stopKey = "stopKey";
    protected String redeployKey = "redeployKey";
    protected int port = 8080;
    private List<File> files;
    
    public void initFromExtension(GlassFishExtension extension) {
        listenPort = extension.getListenPort();
        stopKey = extension.getStopKey();
        redeployKey = extension.getRedeployKey();
        port = extension.getPort();
        files = extension.getFiles();
        getLogger().info("{} received following extention {}", getClass(), extension);
    }
    
    protected Stream<File> getFiles() {
        if (files == null)
            return Stream.of((File) getProject().getTasks().getByName(WarPlugin.WAR_TASK_NAME).property("archivePath"));
        return files.stream();
    }
    
}
