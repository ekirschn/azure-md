package org.azuremd.backend.azure;

/**
 * IBlobCompleteEvent
 * 
 * Wird als Callback dafür genutzt, wenn vom Blob fertig geladen wird.
 * 
 * @author dako
 *
 */
public interface IBlobCompleteEvent
{
    public void done();
}
