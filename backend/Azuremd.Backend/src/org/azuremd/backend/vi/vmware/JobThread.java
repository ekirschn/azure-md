package org.azuremd.backend.vi.vmware;

import com.vmware.vix.*;

public class JobThread implements Runnable 
{
    private VixHandle job;
    private VixVmHandle handle;
    private VixHostHandle server;
    
    public JobThread(VixHandle _job, VixVmHandle _handle, VixHostHandle _server)
    {
        job = _job;
        handle = _handle;
        server = _server;
    }
    
    public void run()
    {
        server.getVix().VixJob_Wait(job, VixPropertyID.VIX_PROPERTY_NONE, VixPropertyID.VIX_PROPERTY_NONE);
        handle.release();
        job.release();
    }
}
