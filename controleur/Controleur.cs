using MateZone.common.dto;
using MateZone.infrastructure.websocket;
using MateZone.metier;
using MateZone.metier.interfaces;
using MateZone.vue.frames;
using System.Windows;

namespace MateZone.controleur
{
   public class Controleur : INotifieur
   {
        /*-------------------------------*/
        /* Attributs                     */
        /*-------------------------------*/
        public const string ADRESSE_SERVEUR   = "176.160.53.67";
        //public const string ADRESSE_SERVEUR   = "localhost";
        public const string PORT_SERVEUR_CHAT = ":14000";
        public const string PORT_SERVEUR_IMG  = ":14001";

        
        private FenetreConnexion _fenetreConnexion;
        private FenetreMateZone  _fenetreMateZone;
        private Metier           _metier;

        /*-------------------------------*/
        /* Constructeur                  */
        /*-------------------------------*/
        public Controleur()
        {
            INotifieur iNotifieur = this;
            IEnvoyeur  iEnvoyeur  = new WebSocketClient(iNotifieur);
            iEnvoyeur.ConnexionServeur("ws://" + Controleur.ADRESSE_SERVEUR + Controleur.PORT_SERVEUR_CHAT);
            //if ( !iEnvoyeur.EstConnectee() )
            //{
            //    MessageBox.Show("Connexion au serveur impossible");
            //    return;
            //}


            this._fenetreConnexion = new FenetreConnexion(this);
            this._fenetreConnexion.Show();
            this._metier = new Metier(iEnvoyeur, iNotifieur);
        }

        /*-----------------------------------*/
        /* Action vers WebSocket/Metier      */
        /*-----------------------------------*/
        public void ConnecterAsync(string pseudo, string mdp)
        {
            Console.WriteLine( "ConnecterAsync : pseudo " + pseudo + " ; mdp " + mdp );
            this._metier.ConnecterAuClient(pseudo, mdp);
        }

        public void InscrireAsync(string pseudo, string mdp)
        {
            Console.WriteLine( "InscrireAsync : pseudo " + pseudo + " ; mdp " + mdp );
            this._metier.EnregistrerUtilisateur(pseudo, mdp);
        }

        public async Task EnvoyerMessageAsync( string message )
        {
            Console.WriteLine( "EnvoyerMessageAsync : " + message );
            this._metier.EnvoyerMessageAsync( message );
        }

        /*-----------------------------------*/
        /* Implémentation INotifieur         */
        /*-----------------------------------*/
        public void NotifierMessage(ChatEventDTO eventDTO)
        {
            Console.WriteLine( "NotifierMessage : " + eventDTO.ToJson() );
            this._metier.NotifierMessage(eventDTO);
        }

        public void SuccesLogin(string pseudo)
        {
            Console.WriteLine( "SuccesLoginAsync : " + pseudo );

            ExecuterSurUI(() =>
            {
                this._fenetreConnexion.Close();
                this._fenetreMateZone = new FenetreMateZone( this );
                this._fenetreMateZone.Show();
            });
        }

        public void NotifierErreur(string erreur)
        {
            Console.WriteLine( "NotifierErreur : " + erreur );
            ExecuterSurUI(() => MessageBox.Show(erreur));
        }

        public void AfficherListMessage(ChatEventDTO eventDTO)
        {
            Console.WriteLine( "AfficherListMessage : " + eventDTO.ToJson() );
            ExecuterSurUI(() => this._fenetreMateZone.AfficherLstMessage(eventDTO));
        }

        public void AfficherNvMessage(ChatEventDTO eventDTO)
        {
            Console.WriteLine( "AfficherNvMessage : " + eventDTO.ToJson() );
            ExecuterSurUI(() => this._fenetreMateZone.AfficherNvMessage(eventDTO));
        }

        public void AfficherListChannel(ChatEventDTO eventDTO)
        {
            Console.WriteLine( "AfficherListChannel : " + eventDTO.ToJson() );
            ExecuterSurUI(() => this._fenetreMateZone.AfficherLstGroupe(eventDTO));
        }

        public void AfficherNvChannel(ChatEventDTO eventDTO)
        {
            Console.WriteLine( "AfficherNvChannel : " + eventDTO.ToJson() );
            ExecuterSurUI(() => this._fenetreMateZone.AfficherNvGroupe(eventDTO));
        }


        /*-------------------------------*/
        /* Helper pour exécuter sur UI   */
        /*-------------------------------*/
        private void ExecuterSurUI( Action action )
        {
            Application.Current.Dispatcher.Invoke( action );
        }
    }
}
