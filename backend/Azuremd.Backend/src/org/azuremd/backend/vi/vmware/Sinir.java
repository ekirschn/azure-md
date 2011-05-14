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
        for(String item : array)
            if (item.equals(needle))
                return true;
                
        return false;
    }
    
    public static String[] fastRead(String fileName, String[] keys) 
    {
        String[] result = new String[keys.length];
        int i = 0;
        
        try 
        {
            FileInputStream fstream = new FileInputStream(new File(fileName));
            DataInputStream in = new DataInputStream(fstream);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            
            String line;
            
            while ((line = br.readLine()) != null)   
            {
                String[] arr = line.split("=");
                
                if (contains(keys, arr[0].trim()))
                    result[i++] = arr[1].trim().replace("\"", "");
            }
            
            in.close();
        }
        catch(IOException e)
        {
            log.error(e);
        }
        
        return result;
    }
}
