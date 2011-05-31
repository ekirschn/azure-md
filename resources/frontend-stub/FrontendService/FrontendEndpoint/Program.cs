using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.ServiceModel;
using FrontendService;
using System.ServiceModel.Description;

namespace FrontendEndpoint
{
    class Program
    {
        static void Main(string[] args)
        {
            using (var host = new ServiceHost(typeof(Frontend), new Uri("http://192.168.0.62:8000/services")))
            {
                host.AddServiceEndpoint(typeof(IFrontend), new BasicHttpBinding(), "frontend");
                host.Description.Behaviors.Add(new ServiceMetadataBehavior() { HttpGetEnabled = true });
                host.Open();

                Console.ReadLine();
            }
        }
    }
}
