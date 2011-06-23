package org.azuremd.backend.azure;

import java.io.*;

import com.spinn3r.log5j.Logger;

import org.azuremd.backend.Application;
import org.azuremd.backend.util.IoHelper;
import org.azuremd.backend.vi.JythonAdapter;
import org.azuremd.backend.vi.SystemStatus;

/**
 * BlobLoader
 * 
 * Lädt die Datei aus einem Blob in einem eigenem Thread und entpackt diese über
 * Python.
 * 
 * @author dako
 * 
 */
public class BlobLoader extends Thread
{
    private static Logger log = Logger.getLogger();
    private String url;
    private File file;
    private IBlobCompleteEvent event;

    private BlobLoader(String _url, String fileName, IBlobCompleteEvent _event)
    {
        url = _url;
        file = new File(fileName);
        event = _event;
    }

    private static BlobLoader instance;

    public static BlobLoader load(String _url, String fileName,
            IBlobCompleteEvent event)
    {
        instance = new BlobLoader(_url, fileName, event);

        Thread th = new Thread(instance);
        th.setDaemon(true);
        th.setName("BlobberThread");
        th.start();

        return instance;
    }

    @Override
    public void run()
    {
        try
        {
            String newFile = file.getParent() + "/data.7z";
            
            log.debug("Downloading data into %s", newFile);
            IoHelper.downloadFile(url, newFile);
            
            log.debug("Extracting %s", newFile);
            JythonAdapter.set("arg0", new File(newFile).getAbsolutePath());
            JythonAdapter.execInternFile("extract");

            event.done();
        }
        catch (Exception e)
        {
            log.error(e);
            Application.setStatus(SystemStatus.READY);
        }
    }
}
