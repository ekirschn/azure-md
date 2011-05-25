package org.azuremd.backend.azure;

import java.io.*;
import java.net.*;

import javax.net.ssl.*;

import com.spinn3r.log5j.Logger;

import org.azuremd.backend.Application;
import org.azuremd.backend.server.JythonAdapter;
import org.azuremd.backend.server.SystemStatus;

/**
 * BlobLoader
 * 
 * Lädt die Datei aus einem Blob in einem eigenem Thread und entpackt diese über Python.
 * 
 * @author dako
 * 
 */
public class BlobLoader extends Thread
{
    private static Logger log = Logger.getLogger();
    private String url;
    private boolean isSsl;
    private File file;
    private IEventComplete event;

    // TODO: Callback für den Vorgang
    private BlobLoader(String _url, String fileName, IEventComplete _event)
    {
        url = _url;
        isSsl = url.startsWith("https://");
        file = new File(fileName);
        event = _event;
    }
    
    private static BlobLoader instance;
    
    public static BlobLoader load(String _url, String fileName, IEventComplete event) 
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
            InputStream reader;

            // SSL-Support (even self-signed)
            if (isSsl)
            {
                HttpsURLConnection https = (HttpsURLConnection) new URL(url).openConnection();
                https.setHostnameVerifier(new HostnameVerifier()
                {
                    @Override
                    public boolean verify(String arg0, SSLSession arg1)
                    {
                        return true;
                    }
                });

                reader = https.getInputStream();
            }
            else
            {
                HttpURLConnection http = (HttpURLConnection) new URL(url).openConnection();
                reader = http.getInputStream();
            }

            String newFile = file.getParent() + "/data.7z";
            log.debug("Downloading data into %s", newFile);
            
            BufferedOutputStream writer = new BufferedOutputStream(new FileOutputStream(newFile));

            // Buffersize
            byte[] buffer = new byte[1024];
            int len;

            while ((len = reader.read(buffer)) > 0)
            {
                if (buffer != null)
                    writer.write(buffer, 0, len);
            }

            // order matters - wtf.
            writer.close();
            reader.close();
            
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
