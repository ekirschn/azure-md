package org.azuremd.backend.vi;

import java.util.*;
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
    
    public VMwareVirtualServer(String hostname, String username,
            String password, int port) throws VixException
    {
        server = new VixHostHandle(VixConstants.VIX_API_VERSION, VixServiceProvider.VIX_SERVICEPROVIDER_VMWARE_SERVER, hostname, port, username, password);
    }

    @Override
    public boolean LoadImage(String imageUrl)
    {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean UnloadImage()
    {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean Reboot(String virtualMachine)
    {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean RebootHost()
    {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean Suspend(String virtualMachine)
    {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean Resume(String virutalMachine)
    {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean GetVmStatus(String virtualMachine)
    {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public SystemStatus GetStatus()
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean GetSystemStatus()
    {
        // TODO Auto-generated method stub
        return false;
    }

    public ArrayList<String> getVMS()
    {
        try
        {
            return server.getRegisteredVms();
        }
        catch (VixException e)
        {
            Application.log.error(e);
        }
        
        return null;
    }
}
