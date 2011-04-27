package org.azuremd.backend.cli;

import com.beust.jcommander.*;

/**
 * ProgramArguments
 * 
 * Enthält Kommandozeilenparameter für die Verwendung mit JCommander.
 * 
 * @author dako
 *
 */
public class ProgramArguments
{
    @Parameter(names = "-cc", description="Erstellt neue Standardkonfiguration in $HOME/.azuremd")
    public boolean createConfig;
    
    @Parameter(names = "-c", description="Verwendet einen alternativen Pfad für das Konfigurationsverzeichnis (muss config enthalten)")
    public String configPath;
    
    @Parameter(names = "-nc", description="Stellt keine Verbindung zum eingetragnen Server her (debug)")
    public boolean noConnection;
    
    @Parameter(names = "-h", description="Zeigt diese Hilfe an")
    public boolean showHelp;
}
