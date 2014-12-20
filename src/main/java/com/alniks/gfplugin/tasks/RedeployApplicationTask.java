package com.alniks.gfplugin.tasks;

import com.alniks.gfplugin.GlassFishExtension;
import com.alniks.gfplugin.GlassFishPlugin;
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
public class RedeployApplicationTask extends GlassFishTask {
    
    @TaskAction
    public void redeploy() {
        ProgressLogger progressLogger = getServices().get(ProgressLoggerFactory.class).newOperation(StopApplicationTask.class);
        progressLogger.setDescription("Redeploying applications");
        progressLogger.setShortDescription("Redeploying app");
        progressLogger.started();
        GlassFishExtension extension = (GlassFishExtension) getProject().getExtensions().findByName(GlassFishPlugin.EXTENSION_NAME);
        getLogger().info("extention in task " + extension);
        initFromExtension(extension);
        
        try (Socket s = new Socket(InetAddress.getByName("127.0.0.1"), listenPort)) {
            s.setSoLinger(false, 0);
            OutputStream out = s.getOutputStream();
            out.write((redeployKey + "\r\nstop\r\n").getBytes());
            out.flush();
        } catch (IOException e) {
            getLogger().error("Problems with redeploying applications");
            throw new GradleException(e.getMessage(), e);
        } finally {
            progressLogger.completed();
        }
        
    }
    
}
