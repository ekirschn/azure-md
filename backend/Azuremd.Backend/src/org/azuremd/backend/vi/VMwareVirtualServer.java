package org.azuremd.backend.vi;

import org.azuremd.backend.server.SystemStatus;

/**
 * VMwareVirtualServer
 * 
 * Implementiert das Backend f�r VirtInterface f�r VMware-Server. Andere
 * Backends m�ssen nur das VirtInterface �bernehmen.
 * 
 * @author dako
 * 
 */
public class VMwareVirtualServer implements VirtInterface
{
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

}
