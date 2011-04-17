package org.azuremd.backend.vi;

import org.azuremd.backend.server.*;

/**
 * VirtInterface
 * 
 * Beinhalt alle Funktionen, die ein Backend für die Nutzug mit diesem Client
 * implementieren muss und der WebService bereitstellt.
 * 
 * @author dako
 * 
 */
public interface VirtInterface
{
    public boolean LoadImage(String imageUrl);

    public boolean UnloadImage();

    public boolean Reboot(String virtualMachine);

    public boolean RebootHost();

    public boolean Suspend(String virtualMachine);

    public boolean Resume(String virutalMachine);

    public boolean GetVmStatus(String virtualMachine);

    public SystemStatus GetStatus();

    public boolean GetSystemStatus();
}
