package org.azuremd.backend;

/**
 * 
 * BackendVersion
 * 
 * Abstraktion der Versionsdarstellung für das Backend
 * 
 * @author dako
 *
 */
public class BackendVersion
{
    private int[] version = new int[4];
    
    public BackendVersion(String _version) 
    {
        int i = 0;
        
        for(String item : _version.split("\\.", 4))
            version[i++] = Integer.parseInt(item);
                    
    }
    
    public int[] underlyingValue()
    {
        return version;
    }
    
    /**
     * Testet, ob die übergebene Version neuer ist als die aktuelle Instanz.
     * @param _version
     * @return True, wenn übergebene Version neuer ist
     */
    public boolean isNewer(BackendVersion _version)
    {
        int[] newVision = _version.underlyingValue();
        
        for(int i = 0; i < version.length; i++)
            if (newVision[i] > version[i])
                return true;
        
        return false;
    }
    
    public String toString()
    {
        return String.format("%s.%s.%s.%s", version[0], version[1], version[2], version[3]);
    }
}
