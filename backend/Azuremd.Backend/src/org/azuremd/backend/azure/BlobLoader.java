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

    public BlobLoader(String _url, String fileName)
    {
        url = _url;
        isSsl = url.startsWith("https://");
        file = new File(fileName);
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

    public static void main(String[] args)
    {
        BlobLoader loader = new BlobLoader("https://testdek.blob.core.windows.net/ass/NAVIGON_Fresh_setup.exe", "temp.bin");
        loader.run();
    }
}
