using System;
using System.Collections.Generic;
using System.Linq;
using System.Runtime.Serialization;
using System.ServiceModel;
using System.Text;

namespace FrontendService
{
    public enum SystemStatus 
    {
        NONE = 0,
        BLOBBING = 1,
        UPGRADING = 2,
        STARTING = 3,
        READY = 4,
        BUSY = 5,
    };

    [ServiceContract]
    public interface IFrontend
    {
        [OperationContract(IsOneWay=true)]
        void TellSystemStatus(string computerId, SystemStatus status);

        [OperationContract(IsOneWay = true)]
        void GetInitialParams();
    }
}
