package org.azuremd.backend.vi;

/**
 * SystemStatus
 * 
 * Einfache Implementation für den Status des Clienten.
 * 
 * @author dako
 * 
 */
public enum SystemStatus
{
    NONE, 
    /**
     * Beziehe Image vom Azure Blob-Storage
     */
    BLOBBING, 
    UPGRADING, 
    STARTING, 
    READY, 
    BUSY
}
