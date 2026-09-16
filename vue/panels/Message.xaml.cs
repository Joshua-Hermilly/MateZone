using MateZone.common.dto;
using MateZone.controleur;
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
    /// Logique d'interaction pour Message.xaml
    /// </summary>
    public partial class Message : UserControl
    {
        /*-------------------------------*/
        /* Attributs                     */
        /*-------------------------------*/
        public ImageSource ImageProfil { get; set; }
        public string Pseudo { get; set; }
        public string Date { get; set; }
        public string Contenu { get; set; }

        /*-------------------------------*/
        /* Consctructeur                 */
        /*-------------------------------*/
        public Message(ChatEventDTO eventDTO)
        {
            InitializeComponent();
            this.DataContext = this;

            this.Pseudo  = eventDTO.GetDataIndex(1)?.ToString();
            this.Date    = eventDTO.GetDataIndex(3)?.ToString();
            this.Contenu = eventDTO.GetDataIndex(2)?.ToString();

            string imageUrl = "http://" + Controleur.ADRESSE_SERVEUR + Controleur.PORT_SERVEUR_IMG + "/avatar?id=" + eventDTO.GetDataIndex(0)?.ToString();
            if (!string.IsNullOrEmpty(imageUrl))
            {
                this.ImageProfil = new BitmapImage(new Uri(imageUrl));
            }
        }
    }
}
