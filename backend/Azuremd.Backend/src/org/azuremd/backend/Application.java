package org.azuremd.backend;

import java.io.*;
import java.lang.management.ManagementFactory;
import java.net.*;
import java.security.*;

import com.sun.net.httpserver.*;

import javax.net.ssl.*;
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
    private static VirtServerInterface host;

    public static SystemStatus getStatus()
    {
        return status;
    }

    public static void setStatus(SystemStatus _status)
    {
        status = _status;
    }

    public VirtServerInterface Host()
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

        store.load(new FileInputStream("bin/server.keystore"), Configuration.getInstance().KeyStorePass.toCharArray());

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

    public static void main(String[] args)
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

        try
        {
            host = new VMwareVirtualServer(Configuration.getInstance().Hostname, Configuration.getInstance().Username, Configuration.getInstance().Password, Configuration.getInstance().Port);
        }
        catch (VixException e)
        {
            log.error(e);
            System.exit(1);
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
