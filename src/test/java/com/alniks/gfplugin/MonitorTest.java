package com.alniks.gfplugin;

import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import org.mockito.runners.MockitoJUnitRunner;

/**
 *
 * @author Alex Saluk
 */
@RunWith(MockitoJUnitRunner.class)
public class MonitorTest {
    
    @Mock GlassFishRunner runner;
    
    @Test
    public void stop() throws Exception {
        Monitor m = new Monitor(7777, runner, configureTasks());
        m.start();
        Socket s = new Socket(InetAddress.getByName("127.0.0.1"), 7777);
        s.setSoLinger(false, 0);
        OutputStream out = s.getOutputStream();
        out.write(("skey\r\nstop\r\n").getBytes());
        out.flush();
        verify(runner).stop();
        m.interrupt();
    }
    
    @Test
    public void name() {
        Monitor m = new Monitor(1111, runner, configureTasks());
        assertEquals("EmbeddedGlassFishMonitor", m.getName());
    }
    
    private GFTasks configureTasks() {
        GFTasks tasks = new GFTasks();
        tasks.addTask("skey", r -> r.stop());
        return tasks;
    }
    
}
