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

using MateZone.controleur;

namespace MateZone.vue.panels
{
    /// <summary>
    /// Logique d'interaction pour GroupeDiscussion.xaml
    /// </summary>
    public partial class GroupeDiscussion : UserControl
    {
        /*-------------------------------*/
        /* Attributs                     */
        /*-------------------------------*/
        private Controleur   _ctrl;
        public string        NomGroupe { get; set; }
        public List<Message> LstMessage { get; set; }

        /*-------------------------------*/
        /* Consctructeurs                */
        /*-------------------------------*/
        public GroupeDiscussion( Controleur ctrl )
        {
            InitializeComponent();

            this._ctrl       = ctrl;
            this.NomGroupe   = "PUBLIC";
            this.LstMessage  = new List<Message>();

            this.DataContext = this;
            
        }

        /*-------------------------------*/
        /* Actions                       */
        /*-------------------------------*/
        private async void BtnEnvoyer_Click(object sender, RoutedEventArgs e)
        {
            await this._ctrl.EnvoyerMessageAsync(this.txtMessageSaisie.Text);
            this.txtMessageSaisie.Text = "";
        }
    }
}
