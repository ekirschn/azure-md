package org.azuremd.backend.server;

import java.util.*;

import com.spinn3r.log5j.Logger;

/**
 * Heartbeat
 * 
 * Implementiert einen Heartbeat, der zurück an den Server gesendet werden soll.
 * 
 * @author dako
 * 
 */
public class Heartbeat
{
    private Timer timer;

    public Heartbeat(String serverUrl)
    {
        Logger.getLogger().debug("Heart starts beating");

        timer = new Timer(true);
        timer.schedule(new TimerTask()
        {
            public void run()
            {
                // Beating ...
            }
        }, 0, 60000);
    }
    
    public void stop()
    {
        Logger.getLogger().debug("Heart stops beating");   
    }
}
