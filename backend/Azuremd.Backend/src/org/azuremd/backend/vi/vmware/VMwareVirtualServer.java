package org.azuremd.backend.vi.vmware;

import java.util.*;

import com.spinn3r.log5j.Logger;
import com.sun.jna.Pointer;
import com.vmware.vix.*;

import org.azuremd.backend.Application;
import org.azuremd.backend.server.SystemStatus;
import org.azuremd.backend.vi.VirtServerInterface;
import org.azuremd.backend.vi.VmBucket;

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

    public void Disconnect()
    {
        server.disconnect();
    }

    @Override
    public SystemStatus RegisterVm(String vmId, String source)
    {
        // Blocking call
        if (Application.isLocked())
            return SystemStatus.BUSY;

        Application.setStatus(SystemStatus.BUSY);

        // async
        // TODO: fix param source
        server.getVix().VixHost_RegisterVM(server, source, VmwareHelper.stdCallback("New vm created"), null);

        return Application.getStatus();
    }

    @Override
    public SystemStatus StartVm(String vmId)
    {
        VixVmHandle handle = null;

        try
        {
            handle = server.findVmByName(vmId);
            server.getVix().VixVM_PowerOn(handle, VixVMPowerOpOptions.VIX_VMPOWEROP_NORMAL, VixVmHandle.VIX_INVALID_HANDLE, VmwareHelper.stdCallback("VM powered on"), null);
        }
        catch (VixException e)
        {
            log.error(e);
        }
        finally
        {
            if (handle != null)
                handle.release();
        }

        return Application.getStatus();
    }

    private void StartVm(VixVmHandle vixVmHandle)
    {
        server.getVix().VixVM_PowerOn(vixVmHandle, VixVMPowerOpOptions.VIX_VMPOWEROP_NORMAL, VixVmHandle.VIX_INVALID_HANDLE, VmwareHelper.stdCallback("VM powered on"), null);
        vixVmHandle.release();
    }

    @Override
    public SystemStatus RestartVm(String vmId)
    {
        VixVmHandle handle = null;

        try
        {
            handle = server.findVmByName(vmId);
            server.getVix().VixVM_PowerOff(handle, VixVMPowerOpOptions.VIX_VMPOWEROP_FROM_GUEST, new VixEventProc()
            {
                public void callbackProc(int handle, int eventType,
                        int moreEventInfo, Pointer clientData)
                {
                    log.debug("VM (gracefully) powered off");
                    StartVm(new VixVmHandle(handle));
                }
            }, null);
        }
        catch (VixException e)
        {
            log.error(e);
        }
        finally
        {
            if (handle != null)
                handle.release();
        }

        return Application.getStatus();
    }

    @Override
    public SystemStatus StopVm(String vmId)
    {
        VixVmHandle handle = null;

        try
        {
            handle = server.findVmByName(vmId);
            // VIX_VMPOWEROP_FROM_GUEST = graceful shutdown
            // VIX_VMPOWEROP_NORMAL = kill
            server.getVix().VixVM_PowerOff(handle, VixVMPowerOpOptions.VIX_VMPOWEROP_FROM_GUEST, VmwareHelper.stdCallback("VM (gracefully) turned off"), null);
        }
        catch (VixException e)
        {
            log.error(e);
        }
        finally
        {
            if (handle != null)
                handle.release();
        }

        return Application.getStatus();
    }

    @Override
    public SystemStatus SuspendVm(String vmId)
    {
        VixVmHandle handle = null;

        try
        {
            handle = server.findVmByName(vmId);
            // VIX_VMPOWEROP_NORMAL = 0 (as api defines)
            server.getVix().VixVM_Suspend(handle, VixVMPowerOpOptions.VIX_VMPOWEROP_NORMAL, VmwareHelper.stdCallback("VM suspended"), null);
        }
        catch (VixException e)
        {
            log.error(e);
        }
        finally
        {
            if (handle != null)
                handle.release();
        }

        return Application.getStatus();
    }

    @Override
    public SystemStatus ResumeVm(String vmId)
    {
        // TODO Auto-generated method stub
        return Application.getStatus();
    }

    @Override
    public SystemStatus ResizeComponents(String vmId, int ramSize, long hdSize,
            int cpuCores)
    {
        // TODO Auto-generated method stub
        return Application.getStatus();
    }

    @SuppressWarnings("serial")
    public VmBucket GetVmStatus()
    {
        VmBucket table = new VmBucket();

        try
        {
            final List<String> runningVms = server.getRunningVms();

            for (final String item : server.getRegisteredVms())
            {
                table.put(item, new Hashtable<String, String>()
                {
                    {
                        put("status", (runningVms.contains(item)) ? "running"
                                : "stopped");
                    }
                });
            }
        }
        catch (VixException e)
        {
            log.error(e);
        }

        return table;
    }

    @Override
    public String GetVmIp(String vmId)
    {
        VixVmHandle handle = null;
        String result = null;

        try
        {
            // get handle
            handle = server.findVmByName(vmId);
            result = handle.getIpAddress();
        }
        catch (VixException e)
        {
            log.error(e);
        }
        finally
        {
            // releasse handle
            if (handle != null)
                handle.release();
        }

        return result;
    }
}
