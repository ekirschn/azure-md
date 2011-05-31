package org.azuremd.backend.vi;

import org.python.Version;
import org.python.util.PythonInterpreter;

import com.spinn3r.log5j.Logger;

import org.azuremd.backend.*;

/**
 * JythonAdapter
 * 
 * Schnittstelle zu Jython und um Python-Skripte damit kontrolliert auszuführen.
 * 
 * @author dako
 * 
 */
public class JythonAdapter
{
    static PythonInterpreter py = null;
    static boolean blocked = false;

    private static void setup()
    {
        Logger.getLogger().debug("Starting up jython runtime (version: %s)", Version.getVersion().replace("\n", ""));
        py = new PythonInterpreter();
        
        // Zugriffe auf die API erlauben
        py.set("app", Application.class);
    }
    
    /**
     * Setzt im Pythonskript Objekte, die genutzt werden können (Args).
     * 
     * @param name - Name
     * @param value - Wert (z.B. ein String)
     */
    public static void set(String name, String value)
    {
        if (py == null)
            setup();
        
        py.set(name, value);
    }

    /**
     * Führt ein Skript aus dem Jar aus und blockiert dabei den laufenden
     * Thread.
     * 
     * @param _file
     *            - Nur Dateiname ohne Endung,
     */
    public static void execInternFile(String file)
    {
        if (py == null)
            setup();
        
        py.execfile(Application.class.getClassLoader().getResourceAsStream(String.format("scripts/%s.py", file)));
    }
}
