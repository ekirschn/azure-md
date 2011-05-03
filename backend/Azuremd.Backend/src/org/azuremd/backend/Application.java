package org.azuremd.backend;

import java.io.*;
import java.lang.management.ManagementFactory;
import java.net.*;
import java.security.*;

import com.sun.net.httpserver.*;

import javax.net.ssl.*;
import javax.xml.ws.*;
import com.spinn3r.log5j.*;
import com.beust.jcommander.JCommander;
import com.vmware.vix.*;

import org.azuremd.backend.cli.ProgramArguments;
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
    private static VirtServerInterface host;

    public static SystemStatus getStatus()
    {
        return status;
    }

    public static void setStatus(SystemStatus _status)
    {
        status = _status;
    }

    public static VirtServerInterface getHost()
    {
        return host;
    }

    private static HttpContext createSslContext() throws Exception
    {
        // Ja, diesen GANZEN Kram brauch man, wenn man nur SSL haben will. Java
        // macht so Spaß.
        SSLContext ssl = SSLContext.getInstance("TLS");

        KeyManagerFactory keyFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        KeyStore store = KeyStore.getInstance("JKS");

        store.load(new FileInputStream(String.format("%s/server.keystore", Configuration.getConfigurationPath())), Configuration.getInstance().KeyStorePass.toCharArray());

        keyFactory.init(store, Configuration.getInstance().KeyStorePass.toCharArray());

        TrustManagerFactory trustFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());

        trustFactory.init(store);

        ssl.init(keyFactory.getKeyManagers(), trustFactory.getTrustManagers(), new SecureRandom());

        URI uri = new URI(Configuration.getInstance().WebServiceUrl);

        HttpsServer httpsServer = HttpsServer.create(new InetSocketAddress(uri.getHost(), uri.getPort()), 0);
        httpsServer.setHttpsConfigurator(new HttpsConfigurator(ssl));

        HttpContext httpContext = httpsServer.createContext("/");
        httpsServer.start();

        return httpContext;
    }

    public static void main(String[] _args)
    {
        // Adding control+c/exit handler
        Runtime.getRuntime().addShutdownHook(new Thread()
        {
            public void run()
            {
                if (heart != null)
                    heart.stop();
                if (webservice != null)
                    webservice.stop();
                if (host != null)
                    host.disconnect();
            }
        });

        ProgramArguments args = new ProgramArguments();
        JCommander parser = new JCommander(args, _args);
        
        if (args.showHelp)
        {
            parser.usage();
            System.exit(0);
        }
        
        String pid = ManagementFactory.getRuntimeMXBean().getName();
        log.debug("Backend version %s starting up ... (pid: %s)", "0.1", pid.substring(0, pid.indexOf('@')));
        
        if (args.configPath != null)
            Configuration.setConfigurationPath(args.configPath);

        if (args.createConfig)
        {
            log.debug("Writing default config to %s", Configuration.getConfigurationFile());
            Configuration.saveDefaultConfig(Configuration.getConfigurationFile());
            System.exit(0);
        }
        
        if (!new File(Configuration.getConfigurationFile()).exists())
        {
            log.error("Could not found configuration file. Use -cc to create a default configuraton.");
            System.exit(1);
        }
        
        Configuration.load(Configuration.getConfigurationFile());
        
        if (!args.noConnection)
        {
            try
            {
                host = new VMwareVirtualServer(Configuration.getInstance().Hostname, Configuration.getInstance().Username, Configuration.getInstance().Password, Configuration.getInstance().Port);
            }
            catch (VixException e)
            {
                log.error(e);
                System.exit(1);
            }
        }

        try
        {
            webservice = Endpoint.create(new Azure());
            webservice.publish(createSslContext());

            log.debug("Starting webservice at %s", Configuration.getInstance().WebServiceUrl);
        }
        catch (Exception bind)
        {
            log.error("Could not set up webservice; exiting ... ", bind);
            Runtime.getRuntime().exit(1);
        }

        heart = new Heartbeat(Configuration.getInstance().HeartbeatUrl);

        // Entering normal working modus
        setStatus(SystemStatus.READY);

        while (true)
        {}
    }
}
