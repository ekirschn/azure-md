package org.azuremd.backend.server;

import java.util.*;

import org.azuremd.backend.vi.*;

/**
 * VirtualHost
 * 
 * Baut mit dem Hypervisor eine Verbindung auf und hält sie in der gesamten
 * Lebenszeit des Prozesses.
 * 
 * @author dako
 * 
 */
public class VirtualHost
{
    private static VirtualHost host;
    private ArrayList<VirtualMachine> machines = new ArrayList<VirtualMachine>();
    private VirtInterface vInterface;

    public static VirtualHost connect(String _host, String _username,
            String _password, int _port)
    {
        if (host == null)
            host = new VirtualHost();

        host.vInterface = new VMwareVirtualServer();

        return host;
    }

    public static VirtualHost getHost()
    {
        return host;
    }

    private VirtualHost()
    {
        // Connecting
        // Filling up virtual machines
        for (int i = 0; i < 10; i++)
            machines.add(new VirtualMachine());
    }
}
