package org.azuremd.backend.azure;

import java.io.*;
import java.net.*;

import javax.net.ssl.*;

import com.spinn3r.log5j.Logger;

/**
 * BlobLoader
 * 
 * Lädt die Datei aus einem Blob in einem eigenem Thread.
 * 
 * @author dako
 * 
 */
public class BlobLoader implements Runnable
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
        th.run();
        
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

            BufferedOutputStream writer = new BufferedOutputStream(new FileOutputStream(file));

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
            
            event.done();
        }
        catch (MalformedURLException e)
        {
            log.error(e);
        }
        catch (IOException e)
        {
            log.error(e);
        }
    }
}
