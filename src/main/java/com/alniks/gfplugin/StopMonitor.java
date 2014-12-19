package com.alniks.gfplugin;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.glassfish.embeddable.GlassFishException;

/**
 *
 * @author Alex Saluk
 */
public class StopMonitor extends Thread {
    
    private static final Logger LOGGER = Logger.getLogger(StopMonitor.class.getName());

    private final GlassFishRunner runner;
    private final String key;
    private final int port;

    public StopMonitor(int port, GlassFishRunner runner, String key) {
        this.runner = runner;
        if (port <= 0) {
            throw new IllegalArgumentException("Incorrect stop port");
        }
        if (key == null) {
            throw new IllegalArgumentException("Incorrect stop key");
        }
        this.key = key;
        this.port = port;
        setDaemon(true);
        setName("GlassFishStopMonitor");
    }

    @Override
    public void run() {
        LOGGER.log(Level.INFO, "started monitor on port:{0}", port);
        try (ServerSocket serverSocket = new ServerSocket(port, 1, InetAddress.getByName("127.0.0.1"))) {
            serverSocket.setReuseAddress(true);
            while (true) { //TODO executor?
                try (Socket socket = serverSocket.accept()) {
                    socket.setSoLinger(false, 0);
                    LineNumberReader lin = new LineNumberReader(new InputStreamReader(socket.getInputStream()));
                    String line = lin.readLine();
                    if (!this.key.equals(line)) {
                        continue;
                    }
                    try {
                        LOGGER.log(Level.INFO, "Stopping server due to received '{}' command...", line);
                        runner.stop();
                    } catch (GlassFishException e) {
                        LOGGER.log(Level.SEVERE, "Exception when stopping server", e);
                    }
                    return;
                } catch (IOException e) {
                    LOGGER.log(Level.SEVERE, "Exception during monitoring Server", e);
                }
            }
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, "Exception when opening server socket", ex);
        }
        
        
    }

}
