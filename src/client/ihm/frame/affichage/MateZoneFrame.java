package client.ihm.frame.affichage;

import java.awt.BorderLayout;

import javax.swing.JFrame;

import client.controleur.Controleur;
import client.ihm.panel.affichage.SaisieMessagePanel;
import client.ihm.panel.affichage.SalonPanel;
import common.dto.ChatEventDTO;

/*-------------------------------*/
/* Class MateZoneFrame           */
/*-------------------------------*/
/**
 * Fenêtre principale de MateZone qui étend JFrame et sert d'interface de chat.
 * Cette fenêtre contient le panneau d'affichage des messages (SalonPanel) et
 * le panneau de saisie des messages (SaisieMessagePanel).
 * Elle est affichée après une connexion réussie de l'utilisateur.
 *
 * @author Joshua Hermilly
 * @version V1
 * @date 08/11/25
 */
public class MateZoneFrame extends JFrame 
{
	/*--------------------------*/
	/* Attributs                */
	/*--------------------------*/
	/**
	 * Panneau de saisie des messages situé en bas de la fenêtre.
	 * Permet à l'utilisateur de taper et envoyer des messages ou pièces jointes.
	 */
	private SaisieMessagePanel saisieMessagePanel;

	/**
	 * Panneau d'affichage des messages situé au centre de la fenêtre.
	 * Affiche l'historique et les nouveaux messages du chat.
	 */
	private SalonPanel salonPanel;

	/*--------------------------*/
	/* Constructeur             */
	/*--------------------------*/
	/**
	 * Constructeur de la fenêtre principale MateZone.
	 * Configure les propriétés de la fenêtre et initialise les panneaux d'affichage
	 * et de saisie.
	 * La fenêtre est redimensionnable et centrée sur l'écran.
	 * 
	 * @param controleur le contrôleur principal de l'application
	 */
	public MateZoneFrame(Controleur controleur) 
	{
		this.setTitle("MateZone");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(true);
		this.setSize(800, 600);

		/*-------------------------------*/
		/* Création des composants */
		/*-------------------------------*/
		this.saisieMessagePanel = new SaisieMessagePanel(controleur);
		this.salonPanel         = new SalonPanel("#TEMP14>");

		/*-------------------------------*/
		/* Positionnement des composants */
		/*-------------------------------*/
		this.add(this.salonPanel        , BorderLayout.CENTER);
		this.add(this.saisieMessagePanel, BorderLayout.SOUTH );

		this.setLocationRelativeTo(null);
	}

	/**
	 * Affiche une liste de messages dans le salon de chat.
	 * Délègue l'affichage au panneau SalonPanel.
	 * 
	 * @param eventDTO l'événement contenant la liste des messages à afficher
	 */
	public void afficherListMessage(ChatEventDTO eventDTO) 
	{
		 this.salonPanel.addLstMessage(eventDTO);
	}

	/**
	 * Affiche un nouveau message dans le salon de chat.
	 * Délègue l'affichage au panneau SalonPanel.
	 * 
	 * @param eventDTO l'événement contenant le nouveau message à afficher
	 */
	public void afficherNvMessage(ChatEventDTO eventDTO) 
	{
		this.salonPanel.addMessage(eventDTO);
	}
}
