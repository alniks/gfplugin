package com.alniks.gfplugin;

import java.io.File;
import java.util.concurrent.atomic.AtomicBoolean;
import org.glassfish.embeddable.BootstrapProperties;
import org.glassfish.embeddable.GlassFish;
import org.glassfish.embeddable.GlassFishException;
import org.glassfish.embeddable.GlassFishProperties;
import org.glassfish.embeddable.GlassFishRuntime;
import org.gradle.api.logging.Logger;
import org.gradle.api.logging.Logging;

/**
 *
 * @author Alex Saluk
 */
public class GlassFishRunner {
    
    private GlassFish gf;
    private final int port;
    private final AtomicBoolean started = new AtomicBoolean();
    private static final Logger LOGGER = Logging.getLogger(GlassFishRunner.class);

    public GlassFishRunner(int port) {
        this.port = port;
    }
    
    public void start() throws GlassFishException {
        if (started.get())
            throw new IllegalStateException("the server has been already started");
        
        GlassFishProperties gfp = new GlassFishProperties();
        gfp.setPort("http-listener", port);
        
        GlassFishRuntime glassfishRuntime = GlassFishRuntime.bootstrap(new BootstrapProperties());
        gf = glassfishRuntime.newGlassFish(gfp);
        LOGGER.info("starting gf");
        gf.start();
        LOGGER.info("gf started");
        started.set(true);
    }
    
    public void stop() throws GlassFishException {
        if (!started.get())
            throw new IllegalStateException("the server has not been started");
        LOGGER.info("stopping gf");
        gf.stop();
        started.set(false);
    }
    
    //TODO hot deployment
    public void deploy(File file) throws GlassFishException {
        if (!started.get())
            throw new IllegalStateException("the server has not been started");
        LOGGER.info("deploying application: " + file);
        gf.getDeployer().deploy(file, "--name="+file.getName(), "--contextroot="+file.getName());
    }
    
    public void undeploy(File file) throws GlassFishException {
        if (!started.get())
            throw new IllegalStateException("the server has not been started");
        LOGGER.info("undeploying application: " + file);
        gf.getDeployer().undeploy(file.getName());
    }
}
