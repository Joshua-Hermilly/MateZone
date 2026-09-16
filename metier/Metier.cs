using System;
using System.Threading.Tasks;
using System.Windows;
using MateZone.common.dto;
using MateZone.common.protocol;
using MateZone.metier.interfaces;

namespace MateZone.metier
{
    /*-------------------------------*/
    /* Classe Metier                 */
    /*-------------------------------*/
    public class Metier
    {
        /*--------------------------*/
        /* Attributs                */
        /*--------------------------*/
        private readonly IEnvoyeur  _iEnvoyeur;
        private readonly INotifieur _iNotifieur;
        private int    _idChannel;
        private int    _idClient;
        private string _pseudoClient;

        /*--------------------------*/
        /* Constructeur             */
        /*--------------------------*/
        public Metier(IEnvoyeur iEnvoyeur, INotifieur iNotifieur)
        {
            this._iEnvoyeur  = iEnvoyeur;
            this._iNotifieur = iNotifieur;
        }

        /*-----------------------------------*/
        /* METIER                            */
        /*-----------------------------------*/
        /*--------------------------*/
        /* Client                   */
        /*--------------------------*/
        public void SetClient(int idClient, string pseudoClient)
        {
            this._idClient = idClient;
            this._pseudoClient = pseudoClient;

            this._iNotifieur.SuccesLogin(this._pseudoClient);
        }

        /*-----------------------*/
        /* Connexion/Inscription */
        /*-----------------------*/
        public async Task ConnecterAuClient(string pseudo, string mdp)
        {
            // Création du message eventDTO
            ChatEventDTO eventDTO = new ChatEventDTO(EventType.LOGIN)
                .Add(EventType.LOGIN.GetClefIndex(1), mdp    )
                .Add(EventType.LOGIN.GetClefIndex(0), pseudo);

            MessageBox.Show(eventDTO.ToJson  () );
            await this._iEnvoyeur.EnvoyerMessageAsync(eventDTO);
        }

        public async Task EnregistrerUtilisateur(string pseudo, string mdp)
        {
            // Création du message eventDTO
            ChatEventDTO eventDTO = new ChatEventDTO(EventType.SIGNUP)
                .Add(EventType.SIGNUP.GetClefIndex(1), mdp   )
                .Add(EventType.SIGNUP.GetClefIndex(0), pseudo);

            MessageBox.Show(eventDTO.ToJson  ());
            await this._iEnvoyeur.EnvoyerMessageAsync(eventDTO);
        }

        public async Task EnvoyerMessageAsync( string message )
        {
            // Création du message eventDTO
            ChatEventDTO eventDTO = new ChatEventDTO(EventType.NEW_MESSAGE)
                .Add(EventType.SIGNUP.GetClefIndex(0), this._idClient)
                .Add(EventType.SIGNUP.GetClefIndex(1), this._idChannel)
                .Add(EventType.SIGNUP.GetClefIndex(3), message);

            MessageBox.Show(eventDTO.ToJson());
            await this._iEnvoyeur.EnvoyerMessageAsync(eventDTO);
        }

        /*-----------------------------------*/
        /* INotificateur                     */
        /*-----------------------------------*/
        public void NotifierMessage(ChatEventDTO eventDTO)
        {
            // EVENT NULL
            // - - - - - - -
            if (eventDTO.Type == null)
            {
                Console.Error.WriteLine("Type d'événement null reçu : " + eventDTO.ToJson());
                return;
            }

            MessageBox.Show(eventDTO.ToJson());

            // CONNEXION
            if ( eventDTO.Type == EventType.SUCCESS_LOGIN.Nom || eventDTO.Type == EventType.SUCCESS_SIGNUP.Nom )
            {
                int id = eventDTO.GetData<int>(0);
                string pseudo = eventDTO.GetData <string>(1);

                this.SetClient( id , pseudo );
                this._idChannel = 1;
                return;
            }

            // MESSAGES
            if ( eventDTO.Type == EventType.MESSAGE_LIST.Nom )
            {
                this._iNotifieur.AfficherListMessage( ChatEventDTO.JsonToLstEventDTO( eventDTO.ToJson() ) );
                return;
            }

            if ( eventDTO.Type == EventType.MESSAGE.Nom )
            {
                this._iNotifieur.AfficherNvMessage( eventDTO );
                return;
            }

            // CHANNEL
            if ( eventDTO.Type == EventType.PERMS_CHANNELS.Nom )
            {
                this._iNotifieur.AfficherListChannel( ChatEventDTO.JsonToLstEventDTO( eventDTO.ToJson() ) );
                return;
            }

            if ( eventDTO.Type == EventType.PERM_CHANNEL.Nom )
            {
                this._iNotifieur.AfficherNvChannel( eventDTO );
                return;
            }

            // ERREUR
            if ( eventDTO.Type == EventType.ERROR.Nom )
            {
                string erreur = eventDTO.GetData<string>(0);
                this._iNotifieur.NotifierErreur( erreur );
                return;
            }

        }
    }
}
