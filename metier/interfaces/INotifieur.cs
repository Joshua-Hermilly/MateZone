using System;
using MateZone.common.dto;

namespace MateZone.metier.interfaces
{
    /*-------------------------------*/
    /* Interface INotifieur          */
    /*-------------------------------*/
    public interface INotifieur
    {
        void NotifierMessage(ChatEventDTO eventDTO);
        void SuccesLogin(string pseudo);
        void AfficherListMessage(ChatEventDTO eventDTO);
        void AfficherNvMessage(ChatEventDTO eventDTO);
        void AfficherListChannel(ChatEventDTO eventDTO);
        void AfficherNvChannel(ChatEventDTO eventDTO);
        void NotifierErreur(string erreur);
    }
}
