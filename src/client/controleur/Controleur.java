package client.controleur;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import client.ihm.IhmGui;
import client.infrastructure.websocket.WebSocketChatAdapter;
import client.metier.Metier;
import client.metier.interfaces.IEnvoyeur;
import client.metier.interfaces.INotifieur;
import common.dto.ChatEventDTO;

/*-------------------------------*/
/* Class Controleur              */
/*-------------------------------*/
/**
 * Classe principale de contrôle de l'application MateZone côté Client.
 * Gère la logique de connexion et le lancement des différentes interfaces
 * utilisateur.
 *
 * @author Joshua Hermilly
 * @version V1
 * @date 08/11/25
 */

public class Controleur implements INotifieur 
{
	/*--------------------------*/
	/* Attributs                */
	/*--------------------------*/
	/**
	 * Adresse du serveur WebSocket pour la connexion au chat MateZone.
	 */
	private static String ADRESSE_SERVEUR = "";

	/**
	 * Port du serveur WebSocket utilisé pour les communications de chat.
	 * Ce port est utilisé pour établir la connexion avec le serveur de messagerie.
	 */
	private static int PORT_SERVEUR_CHAT = 0;

	/**
	 * Port du serveur utilisé pour le transfert d'images et de pièces jointes.
	 * Ce port est dédié aux communications de fichiers et médias.
	 */
	private static int PORT_SERVEUR_IMG  = 0;


	/**
	 * Interface utilisateur graphique de l'application.
	 * Gère l'affichage des différentes fenêtres et dialogues.
	 */
	private IhmGui ihmGui;

	/**
	 * Couche métier de l'application.
	 * Contient la logique business et fait le lien avec les services de
	 * communication.
	 */
	private Metier metier;

	/*--------------------------*/
	/* Constructeur             */
	/*--------------------------*/
	/**
	 * Constructeur de la classe Controleur.
	 * Initialise l'interface graphique, établit la connexion WebSocket avec le serveur
	 * et configure la couche métier de l'application.
	 * 
	 * @throws Exception si une erreur survient lors de l'initialisation ou de la connexion
	 */
	public Controleur( String ADRESSE, int CHATPORT, int IMGPORT ) throws Exception 
	{
		Controleur.ADRESSE_SERVEUR   = ADRESSE;
		Controleur.PORT_SERVEUR_CHAT = CHATPORT;
		Controleur.PORT_SERVEUR_IMG  = IMGPORT;

		this.ihmGui = new IhmGui( this );

		INotifieur iNotifieur = this;
		IEnvoyeur  iEnvoyeur = new WebSocketChatAdapter( "ws://"+Controleur.ADRESSE_SERVEUR+Controleur.PORT_SERVEUR_CHAT, iNotifieur );
		System.out.println( Controleur.ADRESSE_SERVEUR+Controleur.PORT_SERVEUR_CHAT );
		iEnvoyeur.connecter();

		this.metier = new Metier( iEnvoyeur, iNotifieur );
	}

	/**
	 * Lance l'application en affichant la fenêtre de connexion.
	 */
	public void lancerApp()
	{
		this.ihmGui.lancerConnexionFrame();
	}


	/*-----------------------------------*/
	/* METIER                            */
	/*-----------------------------------*/
	/*--------------------------*/
	/* Client                   */
	/*--------------------------*/
	/**
	 * Tente la connexion d'un client avec ses identifiants.
	 * Valide d'abord le format des identifiants avant de procéder à la connexion.
	 * 
	 * @param pseudo le pseudonyme de l'utilisateur
	 * @param mdp    le mot de passe de l'utilisateur
	 */
	public void tenterConnexionClient(String pseudo, String mdp) 
	{
		// Format valide ?
		if ( !this.validerIdentifiants( pseudo, mdp ) ) { return; }

		this.metier.connecterAuClient( pseudo, mdp );
	}

	/**
	 * Tente l'enregistrement d'un nouvel utilisateur.
	 * Valide d'abord le format des identifiants avant de procéder à l'enregistrement.
	 * 
	 * @param pseudo le pseudonyme souhaité pour le nouvel utilisateur
	 * @param mdp    le mot de passe souhaité pour le nouvel utilisateur
	 */
	public void tenterEnregistrement(String pseudo, String mdp) 
	{
		// Format valide ?
		if ( !this.validerIdentifiants( pseudo, mdp ) ) { return; }

		this.metier.enregistrerUtilisateur(pseudo, mdp);
	}

	/**
	 * Envoie un message texte dans le chat en appelant le méthode dans le métier.
	 * 
	 * @param message le contenu du message à envoyer
	 */
	public void envoyerMessage(String message) 
	{
		this.metier.envoyerMessage( message );
	}

	/**
	 * Envoie une pièce jointe dans le chat.
	 * Lit le fichier depuis le chemin spécifié et l'envoie sous forme de tableau d'octets.
	 * 
	 * @param cheminFic le chemin vers le fichier à envoyer en pièce jointe
	 */
	public void envoyerPieceJoint(String cheminFic) 
	{
		//Créer le tableau de byte
		byte[] bytes = this.extractBytes(cheminFic);

		//Tableau créé ?
		if (bytes == null) 
		{
			this.ihmGui.afficherErreur("Envoie de piece jointe impossible: " + cheminFic);
			return;
		}

		this.metier.envoyerPieceJoint(bytes);
	}

	/**
	 * Extrait les octets d'un fichier depuis son chemin.
	 * 
	 * @param ImageName le chemin vers le fichier à lire
	 * @return un tableau d'octets contenant le contenu du fichier, ou null si une
	 *         erreur survient
	 */
	private byte[] extractBytes(String ImageName) 
	{
		try 
		{
			return Files.readAllBytes( Paths.get( ImageName ) );

		} catch (IOException e1) { e1.printStackTrace(); }

		return null;
	}

	/*--------------------------*/
	/* Serveur                  */
	/*--------------------------*/
	public String getAdrServImg() { return Controleur.ADRESSE_SERVEUR + Controleur.PORT_SERVEUR_IMG; }


	/*---------------------------*/
	/* Notifications (Interface) */
	/*---------------------------*/
	/**
	 * Notifie la réception d'un message depuis le serveur.
	 * Délègue la gestion du message à la couche métier.
	 * 
	 * @param event l'événement de chat contenant les données du message
	 */
	public void notifierMessage(ChatEventDTO eventDTO) 
	{
		this.metier.notifierMessage( eventDTO );
	}

	/**
	 * Traite le succès de la connexion d'un utilisateur.
	 * Lance l'interface principale de MateZone avec le pseudo de l'utilisateur connecté.
	 * 
	 * @param pseudo le pseudonyme de l'utilisateur qui s'est connecté avec succès
	 */
	public void succesLogin(String pseudo) 
	{
		this.ihmGui.lancerMateZoneFrame( pseudo );
	}

	/**
	 * Notifie une erreur à l'interface utilisateur.
	 * 
	 * @param erreur le message d'erreur à afficher
	 */
	public void notifierErreur(String erreur) 
	{
		this.ihmGui.afficherErreur( erreur );
	}

	/**
	 * Affiche une liste de messages dans l'interface utilisateur.
	 * 
	 * @param lstEventDTO l'événement contenant la liste des messages à afficher
	 */
	public void afficherListMessage(ChatEventDTO lstEventDTO) 
	{
		this.ihmGui.afficherListMessage( lstEventDTO );
	}

	/**
	 * Affiche un nouveau message dans l'interface utilisateur.
	 * 
	 * @param eventDTO l'événement contenant le nouveau message à afficher
	 */
	public void afficherNvMessage(ChatEventDTO eventDTO) 
	{
		this.ihmGui.afficherNvMessage( eventDTO );
	}

	/*---------------------------*/
	/* Validations               */
	/*---------------------------*/
	/**
	 * Valide le format des identifiants de connexion.
	 * Vérifie que le pseudo contient au moins 3 caractères et que le mot de passe
	 * contient au moins 6 caractères. Affiche un message d'erreur en cas de format
	 * invalide.
	 * 
	 * @param pseudo le pseudonyme à valider
	 * @param mdp    le mot de passe à valider
	 * @return true si les identifiants sont valides, false sinon
	 */
	private boolean validerIdentifiants(String pseudo, String mdp) 
	{
		// pseudo valide ?
		if ( pseudo.isEmpty() || pseudo.length() < 3 ) 
		{
			this.ihmGui.afficherErreur("Le pseudo doit contenir au moins 3 caractères.");
			return false;
		}

		// mdp valide ?
		if (mdp.isEmpty() || mdp.length() < 6) 
		{
			this.ihmGui.afficherErreur("Le mot de passe doit contenir au moins 6 caractères.");
			return false;
		}

		return true;
	}

}
