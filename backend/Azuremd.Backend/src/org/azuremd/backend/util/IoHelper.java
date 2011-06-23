package org.azuremd.backend.util;

import java.io.*;
import java.net.*;
import javax.net.ssl.*;

import org.azuremd.backend.Application;

/**
 * IoHelper
 * 
 * Helfermethoden für IO.
 * 
 * @author dako
 *
 */
public class IoHelper
{
    /**
     * Lädt eine Datei von der angebeben URL herunter und schreibt sie an den
     * bestimmten Ort.
     * 
     * @param url
     *            - Quellurl
     * @param dest
     *            - Zielpfad
     * @throws IOException
     */
    public static void downloadFile(String url, String dest) throws IOException
    {
        InputStream reader;

        // SSL-Support (even self-signed)
        if (url.startsWith("https://"))
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

        BufferedOutputStream writer = new BufferedOutputStream(new FileOutputStream(dest));

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
    
    public static void extractFromReource(String resourceName, String dest) throws IOException
    {
        // EOF
        byte[] buffer = new byte[0xFFFF];
        
        OutputStream out = new FileOutputStream(dest);
        InputStream in = Application.class.getClassLoader().getResourceAsStream(resourceName);

        for (int len; (len = in.read(buffer)) != -1;)
            out.write(buffer, 0, len);
        
        in.close();
        out.close();
    }
}
