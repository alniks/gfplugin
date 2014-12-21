package com.alniks.gfplugin;

import java.io.File;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.glassfish.embeddable.BootstrapProperties;
import org.glassfish.embeddable.GlassFish;
import org.glassfish.embeddable.GlassFishException;
import org.glassfish.embeddable.GlassFishProperties;
import org.glassfish.embeddable.GlassFishRuntime;

/**
 *
 * @author Alex Saluk
 */
public class GlassFishRunner {
    
    private static final Logger LOGGER = Logger.getLogger(GlassFishRunner.class.getName());
    
    private GlassFish gf;
    private final int port;
    private final AtomicBoolean started = new AtomicBoolean();

    public GlassFishRunner(int port) {
        this.port = port;
    }
    
    public void start() {
        if (started.get())
            throw new IllegalStateException("the server has been already started");
        
        GlassFishProperties gfp = new GlassFishProperties();
        gfp.setPort("http-listener", port);
        
        try {
            GlassFishRuntime glassfishRuntime = GlassFishRuntime.bootstrap(new BootstrapProperties());
            gf = glassfishRuntime.newGlassFish(gfp);
            gf.start();
        } catch (GlassFishException e) {
            throw new RunnerException(e);
        }
        started.set(true);
    }
    
    public void stop() {
        if (!started.get())
            throw new IllegalStateException("the server has not been started");
        try {
            gf.stop();
        } catch (GlassFishException ex) {
            throw new RunnerException(ex);
        }
        started.set(false);
    }
        
    //TODO hot deployment
    public void deploy(File file) {
        LOGGER.log(Level.INFO, "deploying application {0}", file);
        if (!started.get())
            throw new IllegalStateException("the server has not been started");
        String appName = getAppName(file);
        try {
            gf.getDeployer().deploy(file, "--name="+appName, "--contextroot="+appName, "--force=true");
        } catch (GlassFishException ex) {
            throw new RunnerException(ex);
        }
    }
    
    public void undeploy(File file) {
        LOGGER.log(Level.INFO, "undeploying application {0}", file);
        if (!started.get())
            throw new IllegalStateException("the server has not been started");
        String appName = getAppName(file);
        try {
            gf.getDeployer().undeploy(appName);
        } catch (GlassFishException ex) {
            throw new RunnerException(ex);
        }
    }
    
    private String getAppName(File file) {
        //return file.getName().substring(0, file.getName().lastIndexOf("."));//TODO receiving WEB9100: No WebSecurityManager found for context
        return file.getName();
    }
}
