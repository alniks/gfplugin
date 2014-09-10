package com.alniks.gfplugin;

/**
 *
 * @author Alex Saluk
 */
public class GlassFishExtension {
    
    private int stopPort = 5555;
    private String stopKey = "stopKey";
    private int port = 8080;

    public int getStopPort() {
        return stopPort;
    }

    public void setStopPort(int stopPort) {
        this.stopPort = stopPort;
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
    
}
