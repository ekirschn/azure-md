
package org.azuremd.backend.test;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Logger;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceFeature;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.1.6 in JDK 6
 * Generated source version: 2.1
 * 
 */
@WebServiceClient(name = "AzureService", targetNamespace = "http://webservice.backend.azuremd.org/", wsdlLocation = "https://localhost:8080/azure?wsdl")
public class AzureService
    extends Service
{

    private final static URL AZURESERVICE_WSDL_LOCATION;
    private final static Logger logger = Logger.getLogger(org.azuremd.backend.test.AzureService.class.getName());

    static {
        URL url = null;
        try {
            URL baseUrl;
            baseUrl = org.azuremd.backend.test.AzureService.class.getResource(".");
            url = new URL(baseUrl, "https://localhost:8080/azure?wsdl");
        } catch (MalformedURLException e) {
            logger.warning("Failed to create URL for the wsdl Location: 'https://localhost:8080/azure?wsdl', retrying as a local file");
            logger.warning(e.getMessage());
        }
        AZURESERVICE_WSDL_LOCATION = url;
    }

    public AzureService(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public AzureService() {
        super(AZURESERVICE_WSDL_LOCATION, new QName("http://webservice.backend.azuremd.org/", "AzureService"));
    }

    /**
     * 
     * @return
     *     returns Azure
     */
    @WebEndpoint(name = "AzurePort")
    public Azure getAzurePort() {
        return super.getPort(new QName("http://webservice.backend.azuremd.org/", "AzurePort"), Azure.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns Azure
     */
    @WebEndpoint(name = "AzurePort")
    public Azure getAzurePort(WebServiceFeature... features) {
        return super.getPort(new QName("http://webservice.backend.azuremd.org/", "AzurePort"), Azure.class, features);
    }

}
