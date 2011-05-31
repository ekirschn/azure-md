using System;
using System.Collections.Generic;
using System.Linq;
using System.Runtime.Serialization;
using System.ServiceModel;
using System.Text;

namespace FrontendService
{
    public class Frontend : IFrontend
    {
        public void TellSystemStatus(string computerId, SystemStatus status)
        {
            Console.WriteLine(computerId);
            Console.WriteLine(status);
        }

        public void GetInitialParams()
        {

        }
    }
}
