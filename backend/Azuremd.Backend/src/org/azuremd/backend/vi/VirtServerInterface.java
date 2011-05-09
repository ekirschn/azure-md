package org.azuremd.backend.vi;

import org.azuremd.backend.server.SystemStatus;


/**
 * VirtServerInterface
 * 
 * Interface, das alle Backends implementieren müssen, damit sie
 * in diesem Programm genutzt werden können. Bisher wird
 * nur VMware-Server unterstützt.
 * 
 * @author dako
 *
 */
public interface VirtServerInterface 
{	
	public void Disconnect();
	
    public SystemStatus RegisterVm(String vmId, String source);

    public SystemStatus StartVm(String vmId);

    public SystemStatus RestartVm(String vmId);

    public SystemStatus StopVm(String vmId);

    public SystemStatus SuspendVm(String vmId);

    public SystemStatus ResumeVm(String vmId);

    public SystemStatus ResizeComponents(String vmId, int ramSize, long hdSize, int cpuCores);
    
    public VmBucket GetVmStatus();
    
    public String GetVmIp(String vmId);
}
