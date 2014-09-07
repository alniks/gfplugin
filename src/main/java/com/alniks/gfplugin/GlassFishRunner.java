package com.alniks.gfplugin;

import java.io.File;
import java.util.concurrent.atomic.AtomicBoolean;
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
    
    private GlassFish gf;
    private final int port;
    private final AtomicBoolean started = new AtomicBoolean();

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
        gf.start();
        started.set(true);
    }
    
    public void stop() throws GlassFishException {
        if (!started.get())
            throw new IllegalStateException("the server has not been started");
        gf.stop();
        started.set(false);
    }
    
    //TODO hot deployment
    public void deploy(File file) throws GlassFishException {
        if (!started.get())
            throw new IllegalStateException("the server has not been started");
        String appName = file.getName().substring(0, file.getName().lastIndexOf("."));//TODO receiving WEB9100: No WebSecurityManager found for context
        gf.getDeployer().deploy(file, "--name="+file.getName(), "--contextroot="+file.getName(), "--force=true");
    }
    
    public void undeploy(File file) throws GlassFishException {
        if (!started.get())
            throw new IllegalStateException("the server has not been started");
        String appName = file.getName().substring(0, file.getName().lastIndexOf("."));
        gf.getDeployer().undeploy(appName);
    }
}
