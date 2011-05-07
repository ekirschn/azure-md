package org.azuremd.backend.webservice;

import javax.jws.*;
import javax.jws.soap.SOAPBinding;

import org.azuremd.backend.Application;
import org.azuremd.backend.server.SystemStatus;
import org.azuremd.backend.vi.VirtInterface;

/**
 * Azure
 * 
 * WebService für die Kommunikation mit dem FrontEnd.
 * 
 * @author dako
 * 
 */
@WebService(name = "Azure")
@SOAPBinding(style = SOAPBinding.Style.RPC)
public class Azure implements VirtInterface
{
    @WebMethod
    public SystemStatus RegisterVm(String vmId, String source)
    {
        // host.vmware.register (...)
        return Application.getStatus();
    }

    @WebMethod
    public SystemStatus StartVm(String vmId)
    {
        return Application.getStatus();
    }

    @WebMethod
    public SystemStatus RestartVm(String vmId)
    {
        Application.getHost().RestartVm(vmId);
        return Application.getStatus();
    }

    @WebMethod
    public SystemStatus StopVm(String vmId)
    {
        return Application.getStatus();
    }

    @WebMethod
    public SystemStatus SuspendVm(String vmId)
    {
        return Application.getStatus();
    }

    @WebMethod
    public SystemStatus ResumeVm(String vmId)
    {
        return Application.getStatus();
    }

    @WebMethod
    public SystemStatus ResizeComponents(String vmId, int ramSize, long hdSize, int cpuCores)
    {
        return Application.getStatus();
    }

    @WebMethod
    public String GetVmStatus()
    {  
        return Application.getHost().GetVmStatus();
    }

    @WebMethod
    public String GetVmIp(String vmId)
    {
        return null;
    }
    
    @WebMethod
    public void SetInitialParams(String authToken, String computerId) 
    {
        
    }
}
