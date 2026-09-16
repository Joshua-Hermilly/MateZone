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
    /// Logique d'interaction pour GroupeItem.xaml
    /// </summary>
    public partial class GroupeItem : UserControl
    {
        /*-------------------------------*/
        /* Attributs                     */
        /*-------------------------------*/
        private Controleur _ctrl;
        public  Brush      Photo { get; set; }
        public  string     Nom { get; set; }
        private int        _idChannel;

        /*-------------------------------*/
        /* Consctructeur                 */
        /*-------------------------------*/
        public GroupeItem(Controleur ctrl, ChatEventDTO eventDTO)
        {
            InitializeComponent();
            this.DataContext = this;

            this._ctrl      = ctrl;
            this._idChannel = eventDTO.GetData<int>(0);
            this.Nom        = eventDTO.GetDataIndex(1)?.ToString();

            string imageUrl = "http://" + Controleur.ADRESSE_SERVEUR + Controleur.PORT_SERVEUR_IMG + "/avatar?id=" + eventDTO.GetDataIndex(0)?.ToString();
            this.Photo = new ImageBrush(new BitmapImage(new Uri(imageUrl)));
        }
    }
}
