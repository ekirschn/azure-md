package org.azuremd.backend.vi.vmware;

import org.azuremd.backend.Application;
import org.azuremd.backend.server.SystemStatus;

import com.spinn3r.log5j.Logger;
import com.sun.jna.Pointer;
import com.vmware.vix.*;

/**
 * VmHelper
 * 
 * Hilfsmethoden für Vmware, damit weniger geschrieben werden muss.
 * 
 * @author dako
 * 
 */
public class VmwareHelper
{
    private static final Logger log = Logger.getLogger();
    
    public static VixEventProc stdCallback(final String msg) 
    {
        return new VixEventProc()
        {
            public void callbackProc(int handle, int eventType,
                    int moreEventInfo, Pointer clientData)
            {
                log.debug(msg);
                Application.setStatus(SystemStatus.READY);
                // TODO: Frontend-Anrufen, dass wir fertig sind
            }
        };
    }
}
