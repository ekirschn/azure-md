package org.azuremd.backend.webservice;

import java.io.*;
import java.util.*;
import java.util.Map.Entry;

import javax.jws.*;
import javax.jws.soap.SOAPBinding;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamWriter;

import org.azuremd.backend.*;
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
    
    @WebMethod
    public SystemStatus RegisterVm(String token, String vmId, String source)
    {
        if (!TokenHandler.isValid(token))
            return SystemStatus.NONE;
        
        Application.getHost().RegisterVm(vmId, source);
        
        return Application.getStatus();
    }

    @WebMethod
    public SystemStatus StartVm(String token, String vmId)
    {
        if (!TokenHandler.isValid(token))
            return SystemStatus.NONE;
        
        Application.getHost().StartVm(vmId);
        
        return Application.getStatus();
    }

    @WebMethod
    public SystemStatus RestartVm(String token, String vmId)
    {
        if (!TokenHandler.isValid(token))
            return SystemStatus.NONE;
        
        Application.getHost().RestartVm(vmId);
        
        return Application.getStatus();
    }

    @WebMethod
    public SystemStatus StopVm(String token, String vmId)
    {
        if (!TokenHandler.isValid(token))
            return SystemStatus.NONE;
        
        Application.getHost().StopVm(vmId);
        
        return Application.getStatus();
    }

    @WebMethod
    public SystemStatus SuspendVm(String token, String vmId)
    {
        if (!TokenHandler.isValid(token))
            return SystemStatus.NONE;
        
        Application.getHost().SuspendVm(vmId);
        
        return Application.getStatus();
    }

    @WebMethod
    public SystemStatus ResumeVm(String token, String vmId)
    {
        if (!TokenHandler.isValid(token))
            return SystemStatus.NONE;
        
        Application.getHost().ResumeVm(vmId);
        
        return Application.getStatus();
    }

    @WebMethod
    public SystemStatus ResizeComponents(String token, String vmId, int ramSize, long hdSize, int cpuCores)
    {
        if (!TokenHandler.isValid(token))
            return SystemStatus.NONE;
        
        Application.getHost().ResizeComponents(vmId, ramSize, hdSize, cpuCores);
        
        return Application.getStatus();
    }

    @WebMethod
    public String GetVmStatus(String token)
    {
        if (!TokenHandler.isValid(token))
            return SystemStatus.NONE.toString();
        
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        String result = "";
        
        try
        {
            XMLStreamWriter writer = XMLOutputFactory.newInstance().createXMLStreamWriter(os);
            writer.writeStartElement("vms");
            
            for(Entry<String, Hashtable<String, String>> item : Application.getHost().GetVmStatus().entrySet())
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
        if (!TokenHandler.isValid(token))
            return SystemStatus.NONE.toString();
        
        return null;
    }
    
    @WebMethod
    public SystemStatus SetInitialParams(String token, String computerId) 
    {
        if (TokenHandler.gotToken())
        {
            log.debug("Invalid request: token already set");
            return SystemStatus.NONE;
        }
        
        TokenHandler.save(token, computerId);
        
        return SystemStatus.READY;
    }
}
