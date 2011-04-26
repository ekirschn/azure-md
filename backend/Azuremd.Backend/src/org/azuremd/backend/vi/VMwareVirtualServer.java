package org.azuremd.backend.vi;

import java.util.*;

import com.spinn3r.log5j.Logger;
import com.vmware.vix.*;

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
            log.error(e);
        }

        return null;
    }
}
