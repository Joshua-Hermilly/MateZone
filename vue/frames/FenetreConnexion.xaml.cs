using MateZone.controleur;
using MateZone.vue.panels;
using System;
using System.Collections.Generic;
using System.Text;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Data;
using System.Windows.Documents;
using System.Windows.Input;
using System.Windows.Media;
using System.Windows.Media.Imaging;
using System.Windows.Shapes;

namespace MateZone.vue.frames
{
    /// <summary>
    /// Logique d'interaction pour FenetreConnexion.xaml
    /// </summary>
    public partial class FenetreConnexion : Window
    {
        /*-------------------------------*/
        /* Attributs                     */
        /*-------------------------------*/
        private Controleur _ctrl;

        /*-------------------------------*/
        /* Constructeur                  */
        /*-------------------------------*/
        public FenetreConnexion(Controleur ctrl)
        {
            InitializeComponent();

            this._ctrl = ctrl;
            this.SetVueConnexion();
        }

        /*-------------------------------*/
        /* Définir la vue de la Frame    */
        /*-------------------------------*/
        public void SetVueConnexion()
        {
            Connexion connexion = new Connexion( this );

            this.Grid.Children.Clear();
            this.Grid.Children.Add( connexion );
        }

        public void SetVueInscription()
        {
            Inscription inscription = new Inscription( this );

            this.Grid.Children.Clear();
            this.Grid.Children.Add( inscription );
        }

        /*-------------------------------*/
        /* Action vers Controleur        */
        /*-------------------------------*/
        public void Connecter( string pseudo, string mdp )
        {
            this._ctrl.ConnecterAsync( pseudo, mdp );
        }

        public void Inscrire(string pseudo, string mdp)
        {
            this._ctrl.InscrireAsync(pseudo, mdp);
        }
    }
}
