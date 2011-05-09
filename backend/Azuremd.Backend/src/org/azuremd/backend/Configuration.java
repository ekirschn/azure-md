package org.azuremd.backend;

import java.io.*;

import org.simpleframework.xml.*;
import org.simpleframework.xml.core.Persister;

import com.spinn3r.log5j.Logger;

/**
 * Configuration
 * 
 * 'Nuff said.
 * 
 * @author dako
 * 
 */
@Root
@Namespace(prefix = "cfg", reference = "https://github.com/ekirschn/azure-md")
public class Configuration
{
    @Element
    public String Hostname = "localhost";

    @Element
    public String Username = "root";

    @Element
    public String Password = "root";
    
    @Element 
    public String Hypervisor = "vmware";

    @Element
    int Port = 8333;

    @Element
    public String WebServiceUrl = "https://localhost:8080/";

    @Element
    public String HeartbeatUrl = "http://localhost.heartbeat.com";

    @Element
    public String KeyStorePass = "changeit";
    
    @Element
    public String AuthToken = "NONE";
    
    @Element
    public String ComputerId = "NONE";

    private static final Logger log = Logger.getLogger();
    private static Configuration instance = null;
    private static String configurationPath = String.format("%s/%s", System.getProperty("user.home"), ".azuremd");

    public static String getConfigurationPath()
    {
        return configurationPath;
    }

    public static String getConfigurationFile()
    {
        return String.format("%s/%s", configurationPath, "config");
    }

    public static void setConfigurationPath(String path)
    {
        configurationPath = path;
    }

    public static void load(String fileName)
    {
        Serializer ser = new Persister();
        Configuration cfg = new Configuration();
        File cfgFile = new File(fileName);

        try
        {
            cfg = ser.read(Configuration.class, cfgFile);
            log.debug("Loading configuration from %s", cfgFile);
        }
        catch (Exception e)
        {
            log.error("Could not read configuration", e);
        }

        instance = cfg;
    }
    
    /**
     * Speichert die aktuelle Konfiguration im Pfad.
     */
    public void save()
    {
        Serializer ser = new Persister();

        try
        {
            ser.write(this, new File(getConfigurationFile()));
        }
        catch (Exception e)
        {
            log.error(e);
        }
    }

    /**
     * Schreibt die Standardkonfigurtion mit Dummy-Zertifikat in einen Pfad.
     * Falls dieser Pfad nicht vorhanden ist, wird er erstellt.
     * 
     * @param fileName
     *            Pfad der zur schreibenden Konfigurationsdatei
     */
    public static void saveDefaultConfig(String fileName)
    {
        File file = new File(fileName);
        File path = new File(file.getParent());

        if (!path.exists())
        {
            log.debug("Creating new path configuration path (%s)", path);
            path.mkdir();
        }

        try
        {
            // EOF
            byte[] buffer = new byte[0xFFFF];
            OutputStream out = new FileOutputStream(String.format("%s/%s", path, "server.keystore"));
            InputStream in = Application.class.getClassLoader().getResourceAsStream("server.keystore");

            for (int len; (len = in.read(buffer)) != -1;)
                out.write(buffer, 0, len);
            
            in.close();
            out.close();

            Serializer ser = new Persister();

            ser.write(new Configuration(), file);
        }
        catch (IOException e)
        {
            log.error("Could not write keystore", e);
        }
        catch (Exception e)
        {
            log.error("Could not write configuration", e);
        }
    }

    public static Configuration getInstance()
    {
        return instance;
    }

    private Configuration()
    {
        // Erstellen außerhalb dieser Klasse verhindern
    }
}
