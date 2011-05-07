
package org.azuremd.backend.test;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.bind.annotation.XmlSeeAlso;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.1.6 in JDK 6
 * Generated source version: 2.1
 * 
 */
@WebService(name = "Azure", targetNamespace = "http://webservice.backend.azuremd.org/")
@SOAPBinding(style = SOAPBinding.Style.RPC)
@XmlSeeAlso({
    ObjectFactory.class
})
public interface Azure {


    /**
     * 
     * @param arg1
     * @param arg0
     * @return
     *     returns org.azuremd.backend.webservice.SystemStatus
     */
    @WebMethod(operationName = "RegisterVm")
    @WebResult(partName = "return")
    public SystemStatus registerVm(
        @WebParam(name = "arg0", partName = "arg0")
        String arg0,
        @WebParam(name = "arg1", partName = "arg1")
        String arg1);

    /**
     * 
     * @param arg0
     * @return
     *     returns org.azuremd.backend.webservice.SystemStatus
     */
    @WebMethod(operationName = "StartVm")
    @WebResult(partName = "return")
    public SystemStatus startVm(
        @WebParam(name = "arg0", partName = "arg0")
        String arg0);

    /**
     * 
     * @param arg0
     * @return
     *     returns org.azuremd.backend.webservice.SystemStatus
     */
    @WebMethod(operationName = "RestartVm")
    @WebResult(partName = "return")
    public SystemStatus restartVm(
        @WebParam(name = "arg0", partName = "arg0")
        String arg0);

    /**
     * 
     * @param arg0
     * @return
     *     returns org.azuremd.backend.webservice.SystemStatus
     */
    @WebMethod(operationName = "StopVm")
    @WebResult(partName = "return")
    public SystemStatus stopVm(
        @WebParam(name = "arg0", partName = "arg0")
        String arg0);

    /**
     * 
     * @param arg0
     * @return
     *     returns org.azuremd.backend.webservice.SystemStatus
     */
    @WebMethod(operationName = "SuspendVm")
    @WebResult(partName = "return")
    public SystemStatus suspendVm(
        @WebParam(name = "arg0", partName = "arg0")
        String arg0);

    /**
     * 
     * @param arg0
     * @return
     *     returns org.azuremd.backend.webservice.SystemStatus
     */
    @WebMethod(operationName = "ResumeVm")
    @WebResult(partName = "return")
    public SystemStatus resumeVm(
        @WebParam(name = "arg0", partName = "arg0")
        String arg0);

    /**
     * 
     * @param arg3
     * @param arg2
     * @param arg1
     * @param arg0
     * @return
     *     returns org.azuremd.backend.webservice.SystemStatus
     */
    @WebMethod(operationName = "ResizeComponents")
    @WebResult(partName = "return")
    public SystemStatus resizeComponents(
        @WebParam(name = "arg0", partName = "arg0")
        String arg0,
        @WebParam(name = "arg1", partName = "arg1")
        int arg1,
        @WebParam(name = "arg2", partName = "arg2")
        long arg2,
        @WebParam(name = "arg3", partName = "arg3")
        int arg3);

    /**
     * 
     * @return
     *     returns org.azuremd.backend.webservice.Dictionary
     */
    @WebMethod(operationName = "GetVmStatus")
    @WebResult(partName = "return")
    public String getVmStatus();

    /**
     * 
     * @param arg0
     * @return
     *     returns java.lang.String
     */
    @WebMethod(operationName = "GetVmIp")
    @WebResult(partName = "return")
    public String getVmIp(
        @WebParam(name = "arg0", partName = "arg0")
        String arg0);

    /**
     * 
     * @param arg1
     * @param arg0
     */
    @WebMethod(operationName = "SetInitialParams")
    public void setInitialParams(
        @WebParam(name = "arg0", partName = "arg0")
        String arg0,
        @WebParam(name = "arg1", partName = "arg1")
        String arg1);

}
