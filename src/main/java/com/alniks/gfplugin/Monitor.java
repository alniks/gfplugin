package com.alniks.gfplugin;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Alex Saluk
 */
public class Monitor extends Thread {
    
    private static final Logger LOGGER = Logger.getLogger(Monitor.class.getName());

    private final GlassFishRunner runner;
    private final GFTasks tasks;
    private final int port;

    public Monitor(int port, GlassFishRunner runner, GFTasks tasks) {
        this.runner = runner;
        if (port <= 0) {
            throw new IllegalArgumentException("Incorrect stop port");
        }
        if (tasks == null) {
            throw new IllegalArgumentException("Incorrect stop key");
        }
        this.tasks = tasks;
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
                    if (!tasks.contais(line)) {
                        continue;
                    }
                    try {
                        LOGGER.log(Level.INFO, "Exequting task '{0}'", line);
                        tasks.execute(line, runner);
                    } catch (RunnerException e) {
                        LOGGER.log(Level.SEVERE, "Exception when exequting task", e);
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
