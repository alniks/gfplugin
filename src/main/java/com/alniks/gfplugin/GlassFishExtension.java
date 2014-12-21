package com.alniks.gfplugin;

import java.io.File;
import java.util.List;

/**
 *
 * @author Alex Saluk
 */
public class GlassFishExtension {
    
    private int listenPort = 5555;
    private String stopKey = "stopKey";
    private int port = 8080;
    private List<File> files;

    public int getListenPort() {
        return listenPort;
    }

    public void setListenPort(int listenPort) {
        this.listenPort = listenPort;
    }

    public String getStopKey() {
        return stopKey;
    }

    public void setStopKey(String stopKey) {
        this.stopKey = stopKey;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public List<File> getFiles() {
        return files;
    }

    public void setFiles(List<File> files) {
        this.files = files;
    }

    @Override
    public String toString() {
        return "GlassFishExtension{" + "listenPort=" + listenPort + ", stopKey=" + stopKey + ", port=" + port + ", files=" + files + '}';
    }
    
}
