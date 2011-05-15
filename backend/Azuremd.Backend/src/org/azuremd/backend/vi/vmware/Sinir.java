package org.azuremd.backend.vi.vmware;

import java.io.*;

import com.spinn3r.log5j.Logger;

/**
 * Sinir (StupidIniReader, yeahhh)
 * 
 * Einer muss ja die kaputten Inis von VMware lesen können.
 * 
 * @author dako
 * 
 */
public class Sinir
{
    private static Logger log = Logger.getLogger();

    private static boolean contains(String[] array, String needle)
    {
        for (String item : array)
            if (item.equals(needle))
                return true;

        return false;
    }

    /**
     * Liest aus der VMX-Konfiguration von VMware die entsprechenden Werte aus.
     * 
     * @param fileName Pfad der VMX-Konfigurationsdatei
     * @param keys Array mit Werte, die gelesen werden sollen
     * @return Array mit Werten, die gelesen wurde in derselben Reihenfolge
     */
    public static String[] gimme(String fileName, String[] keys)
    {
        String[] result = new String[keys.length];
        int i = 0;

        try
        {
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(fileName)));

            String line;

            while ((line = br.readLine()) != null)
            {
                String[] arr = line.split("=");

                if (contains(keys, arr[0].trim()))
                    result[i++] = arr[1].trim().replace("\"", "");
            }

            br.close();
        }
        catch (IOException e)
        {
            log.error(e);
        }

        return result;
    }
}
