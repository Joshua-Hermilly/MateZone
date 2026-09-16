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
    /// Logique d'interaction pour Inscription.xaml
    /// </summary>
    public partial class Inscription : UserControl
    {
        /*----------------------------*/
        /*  Attribut                  */
        /*----------------------------*/
        private FenetreConnexion _frame;

        /*----------------------------*/
        /*  Consctructeur             */
        /*----------------------------*/
        public Inscription(FenetreConnexion frame)
        {
            InitializeComponent();
            this._frame = frame;
        }

        /*----------------------------*/
        /*  Méthodes                  */
        /*----------------------------*/
        private void BtnInscription_Click(object sender, RoutedEventArgs e)
        {
            string txtPseudo    = this.TxtPseudo   .Text;
            string motDePasse   = this.MotDePasse  .Password;
            string confirmation = this.Confirmation.Password;

            if (txtPseudo .Length <= 0          ) { MessageBox.Show("Merci de saisir un pseudo"                              ); return; }
            if (motDePasse.Length <= 0          ) { MessageBox.Show("Merci de saisir un mot de passe"                        ); return; }
            if (motDePasse.Length <= 0          ) { MessageBox.Show("Merci de confirmer le mot de passe"                     ); return; }

            if (txtPseudo .Length <= 3          ) { MessageBox.Show("Merci de saisir un pseudo de plus de 3 caracètres"      ); return; }
            if (motDePasse.Length <= 7          ) { MessageBox.Show("Merci de saisir un mot de passe de plus de 7 caracètres"); return; }
            if (!motDePasse.Equals(confirmation)) { MessageBox.Show("Merci de confirmer correctement le mot de passe"        ); return; }

            this._frame.Inscrire( txtPseudo, motDePasse );
        }

        private void BtnSeConnection_Click(object sender, RoutedEventArgs e)
        {
            this._frame.SetVueConnexion();
        }
    }
}
