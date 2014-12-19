package com.alniks.gfplugin;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author Alex Saluk
 */
public class GFTasks {
    
    private Map<String, GFTask> tasks  = new ConcurrentHashMap<>();
    
    public GFTask addTask(String name, GFTask task) {
        return tasks.put(name, task);
    }
    
    public boolean contais(String name) {
        return tasks.containsKey(name);
    }
    
    public void execute(String what, GlassFishRunner runner) {
        tasks.get(what).execute(runner);
    }
    
}
