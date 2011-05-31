package org.azuremd.backend.vi.vmware;

import org.azuremd.backend.Application;
import org.azuremd.backend.vi.SystemStatus;

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
     * @param jobHandle
     *            - Job-Handle
     * @return VixError (VixError.VIX_OK für alles ok).
     */
    public static VixError getError(VixLibrary vix, int jobHandle)
    {
        VixError err = vix.VixJob_GetError(new VixHandle(jobHandle));

        if (!err.equals(VixError.VIX_OK))
            log.error("Vmware whines: %s", vix.Vix_GetErrorText(err, null));

        return err;
    }

    /**
     * Standard-Callback für asynchrone Jobs der VIX-Library.
     * 
     * @param vix
     *            - Handle auf die zu nutzende VixLibrary.
     * @param logMessage
     *            - Nachricht, die ins Log geschrieben werden soll nach
     *            erfolgreicher Abarbeitung.
     * @return Callback-Objekt
     */
    public static VixEventProc stdCallback(final VixLibrary vix,
            final String logMessage)
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
                if (getError(vix, handle).equals(VixError.VIX_OK))
                    log.debug(logMessage);

                Application.setStatus(SystemStatus.READY);
                // TODO: Frontend-Anrufen, dass wir fertig sind
            }
        };
    }
}
