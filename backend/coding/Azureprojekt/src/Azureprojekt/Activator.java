/*
 * Created on Mon May 09 17:55:51 CEST 2011
 *
 * TODO: 
 * - Methoden (restore, backup, ...) umsetzen
 * - Kommunikation mit geladener Jar (muss bescheid sagen, wann Upgrade ansteht und welche Daten upgegraded werden sollen)
 * 
 */
package Azureprojekt;

import java.lang.Thread.State;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class Activator implements BundleActivator
{
    String appname;             // << ist eigentlich ueber
    String path2upgrade;
    String path2run;
    Float actversion;
    static BundleContext context;

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext
     * )
     */
    public void start(BundleContext context) throws Exception
    {

        Activator.context = context;

        Cloudprocess clproc = new Cloudprocess();
        clproc.start();

        clproc.setAppname("CloudVM");
        clproc.setRunningpath(path2run); // hier die Methode des Clientsaufrufen
        clproc.setVersion(actversion); // hier die Methode des Clientsaufrufen

        // registerService
        // context.registerService(appname, null);
        // context.getServiceReference(appname);

        // ServiceTracker st = new ServiceTracker(context, appname, null);

    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
     */
    public void stop(BundleContext context) throws Exception
    {
        // beendet die Applikation (OSGI) und setzt den Pointer auf Null (wird vom Garbage Collector aberaeumt)
        Activator.context = null;
    }

    public void upgrade(Cloudprocess clproc)
    {
        // do upgrade
        // ueberpruefen ob Thread noch laeuft
        if (clproc.getState() != State.TERMINATED)
        {
            clproc.setStopState(true);
        }

        // backup durchfuehren
        int result = this.backup();
        if (result == 0)
        {
            // Status setzen Upgrade Success
        }
        else
        {
            result = this.restore();
        }
    }

    public int backup()
    {
        // cp actual jar to backup folder
        int retcode = 0;
        return retcode;
    }

    public int restore()
    {
        // discard new jar and cp old one from backup to workspace folder
        int retcode = 0;
        return retcode;
    }
}
