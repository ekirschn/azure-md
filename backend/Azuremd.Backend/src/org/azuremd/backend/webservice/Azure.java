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
    public boolean LoadImage(String imageUrl)
    {
        // Do locking
        Application.setStatus(SystemStatus.BUSY);

        return true;
    }

    @WebMethod
    public boolean UnloadImage()
    {
        return true;
    }

    @WebMethod
    public boolean Reboot(String virtualMachine)
    {
        return true;
    }

    @WebMethod
    public boolean RebootHost()
    {
        return true;
    }

    @WebMethod
    public boolean Suspend(String virtualMachine)
    {
        return true;
    }

    @WebMethod
    public boolean Resume(String virutalMachine)
    {
        return true;
    }

    @WebMethod
    public boolean GetVmStatus(String virtualMachine)
    {
        return true;
    }

    @WebMethod
    public SystemStatus GetStatus()
    {
        return Application.getStatus();
    }

    @WebMethod
    public boolean GetSystemStatus()
    {
        return true;
    }
}
