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

import org.azuremd.backend.cli.ProgramArguments;
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
    
    public static BackendVersion getVersion()
    {
        return new BackendVersion("0.2.2.3");
    }
    
    public static String getPid()
    {
        String pid = ManagementFactory.getRuntimeMXBean().getName();
        return pid.substring(0, pid.indexOf('@'));
    }

    public static boolean isLocked()
    {
        return (status == SystemStatus.BUSY || status == SystemStatus.BLOBBING);
    }

    public static SystemStatus getStatus()
    {
        return status;
    }

    public static void setStatus(SystemStatus _status)
    {
        log.debug("STATUS: %s (was %s)", _status, status);
        
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
        HttpsConfigurator httpsConf = new HttpsConfigurator(ssl);

        URI uri = new URI(Configuration.getInstance().WebServiceUrl);

        HttpsServer httpsServer = HttpsServer.create(new InetSocketAddress(uri.getHost(), uri.getPort()), 0);
        httpsServer.setHttpsConfigurator(httpsConf);

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
                    host.Disconnect();
            }
        });

        ProgramArguments args = new ProgramArguments();
        JCommander parser = new JCommander(args, _args);

        if (args.showHelp)
        {
            parser.usage();
            System.exit(0);
        }
        
        log.debug("Backend version %s starting up ... (pid: %s)", Application.getVersion(), getPid());

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

        if (!Token.gotToken())
            log.error("No Token found; webservice locked.");

        if (!args.noConnection)
        {
            try
            {
                host = VirtInterfaceSelector.get(Configuration.getInstance().Hypervisor).Create(Configuration.getInstance().Hostname, Configuration.getInstance().Username, Configuration.getInstance().Password, Configuration.getInstance().Port);
            }
            catch (ClassNotFoundException e)
            {
                log.error(String.format("Could not load hypervisor for %s", Configuration.getInstance().Hypervisor), e);
                System.exit(1);
            }
            catch (Exception e)
            {
                log.error(e);
                System.exit(1);
            }
        }

        try
        {
            if (args.noSsl)
            {
                Endpoint.publish(Configuration.getInstance().WebServiceUrl.replace("https", "http"), new Azure());
            }
            else
            {
                webservice = Endpoint.create(new Azure());
                webservice.publish(createSslContext());
            }

            String serviceUrl = Configuration.getInstance().WebServiceUrl;
            serviceUrl = ((args.noSsl) ? "http" : "https")
                    + serviceUrl.substring(serviceUrl.indexOf(':'));

            log.debug("Starting webservice at %s", serviceUrl);
        }
        catch (Exception bind)
        {
            log.error("Could not set up webservice; exiting ... ", bind);
            Runtime.getRuntime().exit(1);
        }

        heart = new Heartbeat(Configuration.getInstance().HeartbeatUrl);

        // Entering normal working modus
        setStatus(SystemStatus.READY);
    }
}
