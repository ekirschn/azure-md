package org.azuremd.backend.webservice;

import java.io.*;
import java.util.*;
import java.util.Map.Entry;

import javax.jws.*;
import javax.jws.soap.SOAPBinding;
import javax.xml.stream.*;

import org.azuremd.backend.*;
import org.azuremd.backend.azure.*;
import org.azuremd.backend.server.*;
import org.azuremd.backend.vi.*;

import com.spinn3r.log5j.Logger;

/**
 * Azure
 * 
 * WebService für die Kommunikation mit dem FrontEnd.
 * 
 * @author dako
 * 
 */
@WebService(name = "Azure")
@SOAPBinding(style = SOAPBinding.Style.RPC)
public class Azure
{    
    private static final Logger log = Logger.getLogger();
    private static VirtServerInterface server = Application.getHost();
    
    @WebMethod
    public SystemStatus RegisterVm(String token, final String vmId, final String source)
    {
        if (!Token.isValid(token))
            return SystemStatus.NONE;
        
        if (Application.isLocked())
            return Application.getStatus();
        
        Application.setStatus(SystemStatus.BLOBBING);
        
        log.debug("Downloading image from %s (id: %s)", source, vmId);
        
        BlobLoader.load(source, Configuration.getInstance().vmwareDirectory + vmId, new IEventComplete() 
        {
            @Override
            public void done()
            {
                // Initiate Vm register
                Application.setStatus(SystemStatus.READY);
                // Sets it to busy again ...
                // TODO: vmId (noch) egal
                Application.getHost().RegisterVm(vmId, source);
            }
        });
        
        return Application.getStatus();
    }

    @WebMethod
    public SystemStatus StartVm(String token, String vmId)
    {
        if (!Token.isValid(token))
            return SystemStatus.NONE;
        
        if (Application.isLocked())
            return Application.getStatus();
        
        server.StartVm(vmId);
        
        return Application.getStatus();
    }

    @WebMethod
    public SystemStatus RestartVm(String token, String vmId)
    {
        if (!Token.isValid(token))
            return SystemStatus.NONE;
        
        if (Application.isLocked())
            return Application.getStatus();
        
        server.RestartVm(vmId);
        
        return Application.getStatus();
    }

    @WebMethod
    public SystemStatus StopVm(String token, String vmId)
    {
        if (!Token.isValid(token))
            return SystemStatus.NONE;
        
        if (Application.isLocked())
            return Application.getStatus();
        
        server.StopVm(vmId);
        
        return Application.getStatus();
    }

    @WebMethod
    public SystemStatus SuspendVm(String token, String vmId)
    {
        if (!Token.isValid(token))
            return SystemStatus.NONE;
        
        if (Application.isLocked())
            return Application.getStatus();
        
        server.SuspendVm(vmId);
        
        return Application.getStatus();
    }

    @WebMethod
    public SystemStatus ResumeVm(String token, String vmId)
    {
        if (!Token.isValid(token))
            return SystemStatus.NONE;
        
        if (Application.isLocked())
            return Application.getStatus();
        
        server.ResumeVm(vmId);
        
        return Application.getStatus();
    }

    @WebMethod
    public SystemStatus ResizeComponents(String token, String vmId, int ramSize, long hdSize, int cpuCores)
    {
        if (!Token.isValid(token))
            return SystemStatus.NONE;
        
        if (Application.isLocked())
            return Application.getStatus();
        
        server.ResizeComponents(vmId, ramSize, hdSize, cpuCores);
        
        return Application.getStatus();
    }

    @WebMethod
    public String GetVmStatus(String token)
    {
        if (!Token.isValid(token))
            return SystemStatus.NONE.toString();
        
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        String result = "";
        
        try
        {
            XMLStreamWriter writer = XMLOutputFactory.newInstance().createXMLStreamWriter(os);
            writer.writeStartElement("vms");
            
            for(Entry<String, Hashtable<String, String>> item : server.GetVmStatus().entrySet())
            {
                writer.writeStartElement("vm");
                writer.writeAttribute("name", item.getKey());
                
                // Alle Key/Value als eigene Tags schreiben
                for(Entry<String, String> attr : item.getValue().entrySet())
                {
                    writer.writeStartElement(attr.getKey());
                    writer.writeCharacters(attr.getValue());
                    writer.writeEndElement();
                }
                
                writer.writeEndElement();
            }
            
            writer.writeEndElement();
            
            writer.flush();
            writer.close();
            
            result = os.toString("utf-8");
            
        }
        catch (Exception e)
        {
            log.error(e);
        }
        
        return result;
    }

    @WebMethod
    public String GetVmIp(String token, String vmId)
    {
        if (!Token.isValid(token))
            return SystemStatus.NONE.toString();
        
        return server.GetVmIp(vmId);
    }
    
    @WebMethod
    public String GetBackendVersion(String token) 
    {
        if (!Token.isValid(token))
            return SystemStatus.NONE.toString();
        
        return Application.getVersion().toString();
    }
    
    @WebMethod
    public SystemStatus UpgradeBackend(String token, String version, String source)
    {
        if (!Token.isValid(token))
            return SystemStatus.NONE;
        
        SystemStatus result = SystemStatus.READY;
        
        if(Application.getVersion().isNewer(new BackendVersion(version)))
        {
            result = SystemStatus.UPGRADING;
            // TODO: Async updating über OSGI
        }
        
        return result;
    }
    
    @WebMethod
    public SystemStatus SetInitialParams(String token, String computerId) 
    {
        if (Token.gotToken())
        {
            log.debug("Invalid request: token already set");
            return SystemStatus.NONE;
        }
        
        Token.save(token, computerId);
        
        return SystemStatus.READY;
    }
}
