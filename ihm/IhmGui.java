package client.ihm;

import javax.swing.JOptionPane;

import client.controleur.Controleur;
import client.ihm.frame.affichage.MateZoneFrame;
import client.ihm.frame.connexion.ConnexionFrame;
import common.dto.ChatEventDTO;

/*-------------------------------*/
/* Classe IhmGui                 */
/*-------------------------------*/
/**
 * Classe IhmGui - Gère le lancement et la fermeture des différentes fenêtres.
 * Fait le lien entre le Contrôleur et l'IHM afin d'alléger le contrôleur.
 * Cette classe centralise la gestion des interfaces utilisateur de
 * l'application MateZone.
 *
 * @author Joshua Hermilly
 * @version V1
 * @date 08/11/25
 */
public class IhmGui
{
	/*--------------------------*/
	/* Attributs                */
	/*--------------------------*/
	/**
	 * Référence vers le contrôleur principal de l'application.
	 * Permet à l'IHM de communiquer avec la logique métier.
	 */
	private Controleur controleur;

	/**
	 * Fenêtre de connexion de l'application.
	 * Utilisée pour l'authentification des utilisateurs.
	 */
	private ConnexionFrame connexionFrame;

	/**
	 * Fenêtre principale de MateZone.
	 * Affiche l'interface de chat une fois l'utilisateur connecté.
	 */
	private MateZoneFrame mateZoneFrame;

	/*--------------------------*/
	/* Constructeur             */
	/*--------------------------*/
	/**
	 * Constructeur de la classe IhmGui.
	 * Initialise l'interface utilisateur avec une référence vers le contrôleur.
	 * 
	 * @param controleur le contrôleur principal de l'application
	 */
	public IhmGui(Controleur controleur) 
	{
		this.controleur     = controleur;
		this.connexionFrame = null;
	}

	/**
	 * Lance la fenêtre de connexion de l'application.
	 * Crée et affiche une nouvelle instance de ConnexionFrame.
	 */
	public void lancerConnexionFrame() 
	{
		this.connexionFrame = new ConnexionFrame(this.controleur);
		this.connexionFrame.setVisible(true);
	}

	/**
	 * Lance la fenêtre principale de MateZone après une connexion réussie.
	 * Crée et affiche la fenêtre principale, puis ferme la fenêtre de connexion.
	 * 
	 * @param pseudo le pseudonyme de l'utilisateur connecté
	 */
	public void lancerMateZoneFrame(String pseudo) 
	{
		this.mateZoneFrame = new MateZoneFrame(this.controleur);
		this.mateZoneFrame.setVisible(true);
		this.connexionFrame.dispose();
	}

	/*--------------------------*/
	/* Affichage                */
	/*--------------------------*/
	/**
	 * Affiche un message d'erreur dans une boîte de dialogue modale.
	 * 
	 * @param message le message d'erreur à afficher à l'utilisateur
	 */
	public void afficherErreur(String message) 
	{
		JOptionPane.showMessageDialog(this.connexionFrame, message, "Erreur", JOptionPane.ERROR_MESSAGE);
	}

	/**
	 * Affiche une liste de messages dans la fenêtre principale de MateZone.
	 * Délègue l'affichage à la fenêtre MateZoneFrame.
	 * 
	 * @param eventDTO l'événement contenant la liste des messages à afficher
	 */
	public void afficherListMessage(ChatEventDTO eventDTO) 
	{
		this.mateZoneFrame.afficherListMessage( eventDTO );
	}

	/**
	 * Affiche un nouveau message dans la fenêtre principale de MateZone.
	 * Délègue l'affichage à la fenêtre MateZoneFrame.
	 * 
	 * @param eventDTO l'événement contenant le nouveau message à afficher
	 */
	public void afficherNvMessage(ChatEventDTO eventDTO) 
	{
		this.mateZoneFrame.afficherNvMessage( eventDTO );
	}
}
