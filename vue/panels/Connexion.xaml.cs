using MateZone.vue.frames;
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
using System.Windows.Navigation;
using System.Windows.Shapes;

namespace MateZone.vue.panels
{
    /// <summary>
    /// Logique d'interaction pour Connexion.xaml
    /// </summary>
    public partial class Connexion : UserControl
    {
        /*-------------------------------*/
        /* Attributs                     */
        /*-------------------------------*/
        private FenetreConnexion _frame;

        /*-------------------------------*/
        /* Constructeur                  */
        /*-------------------------------*/
        public Connexion(FenetreConnexion frame)
        {
            InitializeComponent();
            this._frame = frame;        
        }

        /*-------------------------------*/
        /* Méthodes                      */
        /*-------------------------------*/
        private void BtnConnexion_Click(object sender, RoutedEventArgs e)
        {
            string txtPseudo  = this.TxtPseudo .Text;
            string motDePasse = this.MotDePasse.Password;

            if (txtPseudo .Length <= 0) { MessageBox.Show("Merci de saisir un pseudo"       ); return; }
            if (motDePasse.Length <= 0) { MessageBox.Show("Merci de saisir un mot de passe" ); return; }

            this._frame.Connecter( txtPseudo, motDePasse );
        }

        private void BtnSinscrire_Click(object sender, RoutedEventArgs e)
        {
            this._frame.SetVueInscription();
        }
    }
}
