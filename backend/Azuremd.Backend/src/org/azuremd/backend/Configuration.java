package org.azuremd.backend;

import java.io.File;

import org.simpleframework.xml.*;
import org.simpleframework.xml.core.Persister;

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
    String Hostname = "localhost";

    @Element
    String Username = "root";

    @Element
    String Password = "root";

    @Element
    int Port = 8333;

    @Element
    String FrontEndUrl = "http://localhost:8080/";

    @Element
    String HeartbeatUrl = "Http://localhost.heartbeat.com";

    private static Configuration instance = null;

    public static Configuration load(String fileName)
    {
        Serializer ser = new Persister();
        Configuration cfg = null;

        try
        {
            cfg = ser.read(Configuration.class, new File(fileName));
        }
        catch (Exception e)
        {
            Application.log.error("Could not read configuration", e);
        }

        instance = (cfg != null) ? cfg : new Configuration();

        return instance;
    }

    public static void save(String fileName)
    {
        Serializer ser = new Persister();

        try
        {
            ser.write(Configuration.getInstance(), new File(fileName));
        }
        catch (Exception e)
        {
            Application.log.error("Could not write configuration", e);
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
