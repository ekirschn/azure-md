package org.azuremd.backend.test;

public class ServiceTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) 
	{
		AzureService service = new AzureService();
		Azure inter = service.getAzurePort();
		System.out.println(inter.getStatus());
	}
}
