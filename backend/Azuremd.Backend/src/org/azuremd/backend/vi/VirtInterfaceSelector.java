package org.azuremd.backend.vi;

/**
 * VirtInterfaceSelector
 * 
 * Bestimmt und wählt das notwendige Interface für den virtuellen Server im Backend.
 * Jede Anbindung zum Hypervisor muss über dieses Interface initialisiert werden.
 * 
 * @author dako
 * 
 */
public class VirtInterfaceSelector
{
    @SuppressWarnings("unchecked")
    public static VirtServerInterface get(String _type) throws ClassNotFoundException, InstantiationException, IllegalAccessException 
    {
        String className = String.format("org.azuremd.backend.vi.%s.%s%sVirtualServer", _type, _type.substring(0, 1).toUpperCase(), _type.substring(1));
        return ((Class<VirtServerInterface>) Class.forName(className)).newInstance();
    }
}
