package org.azuremd.backend.vi;

import java.util.*;


/**
 * VirtServerInterface
 * 
 * Interface, das alle Backends implementieren müssen, damit sie
 * in diesem Programm genutzt werden können. Bisher wird
 * nur VMware-Server unterstützt.
 * 
 * @author dako
 *
 */
public interface VirtServerInterface extends VirtInterface 
{
	public ArrayList<String> getVMS();
	
	public void disconnect();
}
