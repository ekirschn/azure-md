package org.azuremd.backend.vi.vmware;

import java.util.*;

import com.spinn3r.log5j.Logger;
import com.vmware.vix.*;

import org.azuremd.backend.*;
import org.azuremd.backend.server.*;
import org.azuremd.backend.vi.*;

/**
 * VMwareVirtualServer
 * 
 * VMware-Server spezielle Implementierung des Interfaces.
 * 
 * @author dako
 * 
 */
public class VmwareVirtualServer implements VirtServerInterface
{
    private static Logger log = Logger.getLogger();

    /**
     * Letzter Job, der blockierte.
     */
    private Thread job;
    private VixHostHandle server;
    private VixLibrary vix;
    private final String vmDir = Configuration.getInstance().vmwareDirectory
            + "Virtual Machines/";
    
    public VirtServerInterface Create(String hostname, String username,
            String password, int port) throws Exception
    {
        String url = String.format("https://%s:%s/sdk", hostname, port);
        log.debug("Connecting to virtual server host using %s@%s", username, url);

        try
        {
            server = new VixHostHandle(VixConstants.VIX_API_VERSION, VixServiceProvider.VIX_SERVICEPROVIDER_VMWARE_VI_SERVER, url, (port != 8333) ? port
                    : 0, username, password);
            vix = server.getVix();
        }
        catch (VixException e)
        {
            log.error(e);
            throw new Exception(e);
        }

        return this;
    }

    public void Disconnect()
    {
        server.disconnect();
    }

    @Override
    public SystemStatus RegisterVm(String vmId, String source)
    {
        Application.setStatus(SystemStatus.BUSY);

        // async
        // TODO: fix param source
        vix.VixHost_RegisterVM(server, source, VmwareHelper.stdCallback("New vm created"), null);

        return Application.getStatus();
    }

    @Override
    public SystemStatus StartVm(String vmId)
    {
        Application.setStatus(SystemStatus.BUSY);
        
        VixVmHandle handle = null;

        try
        {
            handle = server.openVm(vmId);
            VixHandle _job = vix.VixVM_PowerOn(handle, VixVMPowerOpOptions.VIX_VMPOWEROP_NORMAL, VixVmHandle.VIX_INVALID_HANDLE, VmwareHelper.stdCallback("VM powered on"), null);
            deleteLater(_job, handle);
        }
        catch (VixException e)
        {
            log.error(e);
        }

        return Application.getStatus();
    }
    
    @Override
    public SystemStatus RestartVm(String vmId)
    {
        /** TODO: Broken
        Application.setStatus(SystemStatus.BUSY);
        
        VixVmHandle handle = null;

        try
        {
            handle = server.openVm(vmId);
            vix.VixVM_PowerOff(handle, VixVMPowerOpOptions.VIX_VMPOWEROP_FROM_GUEST, new VixEventProc()
            {
                public void callbackProc(int handle, int eventType,
                        int moreEventInfo, Pointer clientData)
                {
                    new VixHandle(handle).release();
                    log.debug("VM (gracefully) powered off");
                    vix.VixVM_PowerOn(vixVmHandle, VixVMPowerOpOptions.VIX_VMPOWEROP_NORMAL, VixVmHandle.VIX_INVALID_HANDLE, VmwareHelper.stdCallback("VM powered on"), null);
                    
                    StartVm(new VixVmHandle(handle));
                }
            }, null);
        }
        catch (VixException e)
        {
            log.error(e);
        }
        */
        return Application.getStatus();
    }

    @Override
    public SystemStatus StopVm(String vmId)
    {
        Application.setStatus(SystemStatus.BUSY);
        
        VixVmHandle handle = null;

        try
        {
            handle = server.openVm(vmId);
            // VIX_VMPOWEROP_FROM_GUEST = graceful shutdown
            // VIX_VMPOWEROP_NORMAL = kill
            VixHandle job = vix.VixVM_PowerOff(handle, VixVMPowerOpOptions.VIX_VMPOWEROP_NORMAL, VmwareHelper.stdCallback("VM (gracefully) turned off"), null);
            deleteLater(job, handle);
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
        Application.setStatus(SystemStatus.BUSY);
        
        VixVmHandle handle = null;

        try
        {
            handle = server.openVm(vmId);
            // VIX_VMPOWEROP_NORMAL = 0 (as api defines)
            VixHandle job = vix.VixVM_Suspend(handle, VixVMPowerOpOptions.VIX_VMPOWEROP_NORMAL, VmwareHelper.stdCallback("VM suspended"), null);
            deleteLater(job, handle);
        }
        catch (VixException e)
        {
            log.error(e);
        }

        return Application.getStatus();
    }

    @Override
    public SystemStatus ResumeVm(String vmId)
    {
        Application.setStatus(SystemStatus.BUSY);
        
        return StartVm(vmId);
    }

    @Override
    public SystemStatus ResizeComponents(String vmId, int ramSize, long hdSize,
            int cpuCores)
    {
        Application.setStatus(SystemStatus.BUSY);
        
        StopVm(vmId);

        String[] result = Sinir.gimme(vmDir + vmId.replace("[standard]", "").trim(), new String[] { "memsize" });
        
        if (Integer.parseInt(result[0]) != ramSize)
        {
            log.debug("Ram size changing! %s -> %i", result[0], ramSize);
        }
        
        // TODO: Neue Ramgröße schreiben und starten
        // Bei Festplatte mal sehen ... async?

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
                final String[] bucket = Sinir.gimme(vmDir
                        + item.replace("[standard]", "").trim(), new String[] {
                        "displayName", "guestOS" });

                VixVmHandle handle = server.openVm(item);
                table.put(bucket[0], new Hashtable<String, String>()
                {
                    {
                        put("status", (runningVms.contains(item)) ? "running"
                                : "stopped");
                        put("path", item);
                        put("guestOS", bucket[1]);
                    }
                });

                handle.release();
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
            handle = server.openVm(vmId);
            result = handle.getIpAddress();
        }
        catch (VixException e)
        {
            log.error(e);
        }
        finally
        {
            // release handle
            if (handle != null)
                handle.release();
        }

        return result;
    }
    
    /**
     * Wird bei Blocking-Calls genutzt, um den Speicher zu leeren.
     * 
     * @param _job
     * @param _handle
     */
    private void deleteLater(VixHandle _job, VixVmHandle _handle)
    {
        job = new Thread(new JobThread(_job, _handle, server));
        job.setName("JobCompletion");
        job.start();
    }
}
