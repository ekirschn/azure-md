package org.azuremd.backend.vi;


/**
 * VirtServerInterface
 * 
 * Interface, das alle Backends implementieren müssen, damit sie in diesem
 * Programm genutzt werden können. Bisher wird nur VMware-Server unterstützt.
 * 
 * @author dako
 * 
 */
public interface VirtServerInterface
{
    /**
     * Das Interface wird über diese Methode initialisiert, nachdem es
     * intern vorher erstellt wurde.
     * 
     * @param hostname Hostname
     * @param username Benutzername
     * @param password Passwort
     * @param port  Port
     * @return Initialisiertes Serverinterface
     * @throws Exception
     */
    public VirtServerInterface Create(String hostname, String username,
            String password, int port) throws Exception;

    public void Disconnect();

    public SystemStatus RegisterVm(String vmId, String source);

    public SystemStatus StartVm(String vmId);

    public SystemStatus RestartVm(String vmId);

    public SystemStatus StopVm(String vmId);

    public SystemStatus SuspendVm(String vmId);

    public SystemStatus ResumeVm(String vmId);

    public SystemStatus ResizeComponents(String vmId, int ramSize, long hdSize,
            int cpuCores);

    public VmBucket GetVmStatus();

    public String GetVmIp(String vmId);
    
    /**
     * Standard-Datastore rausfiltern, falls nötig, damit mit dem Pfad gearbeitet werden
     * kann.
     * 
     * @param vmId
     *            - Pfad mit Standard-Datastore
     * @return Gefilterter Pfad
     */
    public String FilterVmPath(String vmId);
}
