package org.azuremd.backend.vi;

import org.azuremd.backend.Configuration;

import com.spinn3r.log5j.Logger;

/**
 * Token
 * 
 * Beinhaltet alle Methoden, um den Token zu überprüfen
 * und zu speichern.
 * 
 * @author dako
 *
 */
public class Token
{
    private static Logger log = Logger.getLogger();
    
    public static boolean isValid(String token) 
    {
        return !Configuration.getInstance().AuthToken.equals("NONE") && Configuration.getInstance().AuthToken.equals(token);
    }
    
    public static boolean gotToken()
    {
        return !Configuration.getInstance().AuthToken.equals("NONE");
    }
    
    public static void save(String token, String computerId)
    {
        Configuration.getInstance().AuthToken = token;
        Configuration.getInstance().ComputerId = computerId;
        Configuration.getInstance().save();
        
        log.debug("Writing initial computerId und token ...");
    }
}
