package com.alniks.gfplugin;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import org.gradle.api.GradleException;
import org.gradle.api.tasks.TaskAction;
import org.gradle.logging.ProgressLogger;
import org.gradle.logging.ProgressLoggerFactory;

/**
 *
 * @author Alex Saluk
 */
public class StopApplicationTask extends GlassFishTask {
    
    @TaskAction
    public void stop() {
        ProgressLogger progressLogger = getServices().get(ProgressLoggerFactory.class).newOperation(StopApplicationTask.class);
        progressLogger.setDescription("Stop GlassFish server");
        progressLogger.setShortDescription("Stopping gf");
        progressLogger.started();
        GlassFishExtension extension = (GlassFishExtension) getProject().getExtensions().findByName(GlassFishPlugin.EXTENSION_NAME);
        getLogger().info("extention in task " + extension);
        initFromExtension(extension);
        try (Socket s = new Socket(InetAddress.getByName("127.0.0.1"), stopPort)) {
            s.setSoLinger(false, 0);
            OutputStream out = s.getOutputStream();
            out.write((stopKey + "\r\nstop\r\n").getBytes());
            out.flush();
        } catch (IOException e) {
            getLogger().error("Problems with stopping gf");
            throw new GradleException(e.getMessage(), e);
        } finally {
            progressLogger.completed();
        }
    }

}
