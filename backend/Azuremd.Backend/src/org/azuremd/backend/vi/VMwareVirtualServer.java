package org.azuremd.backend.vi;

import java.util.*;

import com.spinn3r.log5j.Logger;
import com.vmware.vix.*;

import org.azuremd.backend.Application;
import org.azuremd.backend.server.SystemStatus;

/**
 * VMwareVirtualServer
 * 
 * VMware-Server spezielle Implementierung des Interfaces.
 * 
 * @author dako
 * 
 */
public class VMwareVirtualServer implements VirtServerInterface
{
    private VixHostHandle server;
    private static Logger log = Logger.getLogger();

    public VMwareVirtualServer(String hostname, String username,
            String password, int port) throws VixException
    {
        String url = String.format("https://%s:%s/sdk", hostname, port);
        log.debug("Connecting to virtual server host using %s@%s", username, url);

        server = new VixHostHandle(VixConstants.VIX_API_VERSION, VixServiceProvider.VIX_SERVICEPROVIDER_VMWARE_VI_SERVER, url, (port != 8333) ? port
                : 0, username, password);
    }
    
    public void disconnect()
    {
        server.disconnect();
    }

    @Override
    public SystemStatus RegisterVm(String vmId, String source)
    {
        Application.setStatus(SystemStatus.BUSY);
        
        return Application.getStatus();
    }

    @Override
    public SystemStatus StartVm(String vmId)
    {
        // TODO Auto-generated method stub
        return Application.getStatus();
    }

    @Override
    public SystemStatus RestartVm(String vmId)
    {
        // TODO Auto-generated method stub
        return Application.getStatus();
    }

    @Override
    public SystemStatus StopVm(String vmId)
    {
        // TODO Auto-generated method stub
        return Application.getStatus();
    }

    @Override
    public SystemStatus SuspendVm(String vmId)
    {
        // TODO Auto-generated method stub
        return Application.getStatus();
    }

    @Override
    public SystemStatus ResumeVm(String vmId)
    {
        // TODO Auto-generated method stub
        return Application.getStatus();
    }

    @Override
    public SystemStatus ResizeComponents(String vmId, int ramSize, long hdSize, int cpuCores)
    {
        // TODO Auto-generated method stub
        return Application.getStatus();
    }

    @Override
    public Dictionary<String, VmInfo> GetVmStatus()
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String GetVmIp(String vmId)
    {
        // TODO Auto-generated method stub
        return null;
    }
    
    public void SetInitialParams(String authToken, String computerId) {
        // Wird nie in dieser Klasse aufgerufen.
    }

    public ArrayList<String> getVMS()
    {
        try
        {
            return server.getRegisteredVms();
        }
        catch (VixException e)
        {
            log.error(e);
        }

        return null;
    }
}
