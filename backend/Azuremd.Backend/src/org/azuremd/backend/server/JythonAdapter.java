package org.azuremd.backend.server;

import org.python.Version;
import org.python.util.PythonInterpreter;

import com.spinn3r.log5j.Logger;

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
        Logger.getLogger().debug("Starting up jython runtime (version: %s)", Version.getVersion());
        py = new PythonInterpreter();
        // TODO: Extra Variablen/Methoden für Python zur Verfügung stellen
        // py.set(name, value);
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

        py.execfile(String.format("bin/scripts/%s.py", file));
    }
}
