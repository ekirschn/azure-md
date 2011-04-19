package org.azuremd.backend;

import java.io.File;
import java.lang.management.ManagementFactory;

import javax.xml.ws.*;
import com.spinn3r.log5j.*;
import com.vmware.vix.*;

import org.azuremd.backend.server.*;
import org.azuremd.backend.vi.*;
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
    private static final Logger log = Logger.getLogger();
    private static SystemStatus status = SystemStatus.STARTING;
    private static Endpoint webservice;
    private static Heartbeat heart;

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
    	String pid = ManagementFactory.getRuntimeMXBean().getName();
    	log.debug("Backend version %s starting up ... (pid: %s)", "0.1", pid.substring(0, pid.indexOf('@')));
    	
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

        VirtServerInterface host = null;
        
        try
        {
            // blocks, wtf?
            host = new VMwareVirtualServer(Configuration.getInstance().Hostname, Configuration.getInstance().Username, Configuration.getInstance().Password, Configuration.getInstance().Port);
        }
        catch (VixException e)
        {
            e.printStackTrace();
        }

        log.debug(host.getVMS());
        
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
        heart = new Heartbeat(Configuration.getInstance().HeartbeatUrl);
        
        // Adding control+c/exit handler
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run()
            {
                heart.stop();
                webservice.stop();
                log.debug("Exiting client");
            }
        });
        
        SetStatus(SystemStatus.READY);

        while (true)
        {}
    }
}
