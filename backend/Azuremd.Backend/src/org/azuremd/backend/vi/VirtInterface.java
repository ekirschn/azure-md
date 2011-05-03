package org.azuremd.backend.vi;

import java.util.*;

import org.azuremd.backend.server.*;

/**
 * VirtInterface
 * 
 * Beinhalt alle Funktionen, die ein Backend für die Nutzug mit diesem Client
 * implementieren muss und der WebService bereitstellt.
 * 
 * @author dako
 * 
 */
public interface VirtInterface
{
    public SystemStatus RegisterVm(String vmId, String source);

    public SystemStatus StartVm(String vmId);

    public SystemStatus RestartVm(String vmId);

    public SystemStatus StopVm(String vmId);

    public SystemStatus SuspendVm(String vmId);

    public SystemStatus ResumeVm(String vmId);

    public SystemStatus ResizeComponents(String vmId, int ramSize, long hdSize, int cpuCores);
    
    public Dictionary<String, VmInfo> GetVmStatus();
    
    public String GetVmIp(String vmId);

    public void SetInitialParams(String authToken, String computerId);
}
