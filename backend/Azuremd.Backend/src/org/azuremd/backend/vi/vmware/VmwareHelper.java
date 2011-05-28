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

    public static boolean isComplete(int eventType)
    {
        return new VixEventType(eventType).equals(VixEventType.VIX_EVENTTYPE_JOB_COMPLETED);
    }

    /**
     * Liefert den Fehlercode zurück für einen Job.
     * 
     * @param vix
     *            - Handle auf die zu nutzende VixLibrary.
     * @param handle
     *            - Job-Handle
     * @return VixError (VixError.VIX_OK für alles ok).
     */
    public static VixError check(VixLibrary vix, int handle)
    {
        VixError err = vix.VixJob_GetError(new VixHandle(handle));

        if (err != VixError.VIX_OK)
            log.error("Vmware whines: %s", vix.Vix_GetErrorText(err, null));

        return err;
    }

    public static VixEventProc stdCallback(final VixLibrary vix,
            final String msg)
    {
        return new VixEventProc()
        {
            public void callbackProc(int handle, int eventType,
                    int moreEventInfo, Pointer clientData)
            {
                // Uns interessiert nur abgeschlossen.
                if (!isComplete(eventType))
                    return;

                // Das Error-Handling für die Hausfrau
                if (check(vix, handle) == VixError.VIX_OK)
                    log.debug(msg);

                Application.setStatus(SystemStatus.READY);
                // TODO: Frontend-Anrufen, dass wir fertig sind
            }
        };
    }
}
