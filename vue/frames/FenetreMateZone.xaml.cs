using MateZone.common.dto;
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
    /// Logique d'interaction pour FenetreMateZone.xaml
    /// </summary>
    public partial class FenetreMateZone : Window
    {
        /*-------------------------------*/
        /* Attributs                     */
        /*-------------------------------*/
        private Controleur      _ctrl;
        public GroupeDiscussion GroupeDiscussion { get; private set; }
        public List<GroupeItem> LstGroupeItem { get; private set; }
        private Image           _imageProfil = new Image();

        /*-------------------------------*/
        /* Consctructeur                 */
        /*-------------------------------*/
        public FenetreMateZone(Controleur ctrl)
        {
            InitializeComponent();

            this._ctrl                             = ctrl;
            this.GroupeDiscussion                  = new GroupeDiscussion(this._ctrl);
            this.LstGroupeItem                     = new List<GroupeItem>();

            this.DataContext                       = this;
            this.GroupeDiscussion.DataContext      = this.GroupeDiscussion;
            this.GroupeDiscussionContainer.Content = this.GroupeDiscussion;

            // pour ne pas fermer la frame quand FrameConnexion est fermé
            this.Closed += (s, e) => Application.Current.Shutdown();
        }

        /*-------------------------------*/
        /* Méthodes                      */
        /*-------------------------------*/
        public void AfficherLstMessage(ChatEventDTO lstEvent)
        {
            List<ChatEventDTO> lstEvenDTO = lstEvent.LstEventDTO;

            this.GroupeDiscussion.LstMessage.Clear();

            foreach (ChatEventDTO eventDTO in lstEvenDTO)
            {
                this.AfficherNvMessage(eventDTO);
            }
        }

        public void AfficherNvMessage(ChatEventDTO eventDTO)
        {
            this.GroupeDiscussion.LstMessage.Add(new Message(eventDTO));
        }

        public void AfficherLstGroupe(ChatEventDTO lstEvenDTO)
        {
            List<ChatEventDTO> lstEvent = lstEvenDTO.LstEventDTO;

            this.LstGroupeItem.Clear();
            foreach (ChatEventDTO eventDTO in lstEvent)
            {
                this.LstGroupeItem.Add(new GroupeItem(this._ctrl, eventDTO));
            }
        }

        public void AfficherNvGroupe(ChatEventDTO eventDTO)
        {
            this.LstGroupeItem.Add(new GroupeItem(this._ctrl, eventDTO));
        }
    }
}
