package org.azuremd.backend;

import java.io.File;
import javax.xml.ws.*;
import com.spinn3r.log5j.*;

import org.azuremd.backend.server.Heartbeat;
import org.azuremd.backend.server.SystemStatus;
import org.azuremd.backend.server.VirtualHost;
import org.azuremd.backend.webservice.Azure;

/**
 * Application
 * 
 * Entry-Point für die Applikation, richtet alles ein (WebService, Heartbeat)
 * und lauscht dann mit dem WebService für Aufgaben vom Frontend.
 * 
 * @author dako
 * 
 */
public class Application
{
    // Global logging object
    public static final Logger log = Logger.getLogger();

    private static SystemStatus status = SystemStatus.STARTING;

    public static SystemStatus GetStatus()
    {
        return status;
    }

    public static void SetStatus(SystemStatus _status)
    {
        status = _status;
    }

    public static void main(String[] args)
    {
        // Loading configuration
        String cfgFile = new File(String.format("%s/%s", System.getProperty("user.home"), ".azuremd")).getAbsolutePath();

        Configuration.load(cfgFile);

        // Generating default config
        if (args.length > 0)
        {
            if (args[0].equals("-cc"))
            {
                Configuration.save(cfgFile);
            }
        }

        log.debug("Connecting to virtual server host using %s@%s:%s", Configuration.getInstance().Username, Configuration.getInstance().Hostname, Configuration.getInstance().Port);

        VirtualHost.connect(Configuration.getInstance().Hostname, Configuration.getInstance().Username, Configuration.getInstance().Password, Configuration.getInstance().Port);

        // Starting up webservice
        try
        {
            Endpoint.publish(Configuration.getInstance().FrontEndUrl, new Azure());
            log.debug("Starting webservice at %s", Configuration.getInstance().FrontEndUrl);
        }
        catch (Exception bind)
        {
            log.error("Could not set up webservice; exiting ... ", bind);
            System.exit(1);

        }

        // Entering normal working modus
        new Heartbeat(Configuration.getInstance().HeartbeatUrl);
        SetStatus(SystemStatus.READY);

        while (true)
        {}
    }
}
