using System;
using System.IO;
using System.Threading.Tasks;
using MateZone.common.dto;

namespace MateZone.metier.interfaces
{
    /*-------------------------------*/
    /* Interface IEnvoyeur           */
    /*-------------------------------*/

    public interface IEnvoyeur
    {
        Task ConnexionServeur  (String uri);
        Task DeconnexionServeur();
        Task EnvoyerMessageAsync(ChatEventDTO eventDTO);
        bool EstConnectee();
    }
}
