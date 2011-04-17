package org.azuremd.backend.server;

import java.util.Timer;
import java.util.TimerTask;

import org.azuremd.backend.Application;

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
        Application.log.debug("Heart starts beating");

        timer = new Timer(true);
        timer.scheduleAtFixedRate(new TimerTask()
        {
            public void run()
            {
                // Beating ...
            }
        }, 0, 60000);
    }
}
