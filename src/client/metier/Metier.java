package client.metier;

import client.metier.interfaces.IEnvoyeur;
import client.metier.interfaces.INotifieur;
import common.dto.ChatEventDTO;
import common.protocol.EventEnum;

/*-------------------------------*/
/* Classe Metier                 */
/*-------------------------------*/
/**
 * Classe Métier - Gère la logique métier de l'application de chat MateZone.
 * Cette classe fait le lien entre l'interface utilisateur (IHM) et la couche
 * infrastructure.
 * Elle encapsule les règles business, gère l'état du client connecté et
 * orchestre
 * les interactions avec le serveur via l'interface IEnvoyeur.
 * La passerelle vers le serveur se fait grâce à l'interface IEnvoyeur.
 *
 * @author Joshua Hermilly
 * @version V1
 * @date 08/11/25
 */
public class Metier
{
	/*--------------------------*/
	/* Attributs                */
	/*--------------------------*/
	/**
	 * Interface d'envoi pour communiquer avec le serveur.
	 * Permet d'envoyer les événements de chat au serveur.
	 */
	private final IEnvoyeur iEnvoyeur;

	/**
	 * Interface de notification pour communiquer avec l'IHM.
	 * Permet de notifier les événements reçus à l'interface utilisateur.
	 */
	private final INotifieur iNotifieur;

	/**
	 * Identifiant du canal de chat actuellement utilisé par le client.
	 */
	private int idChannel;

	/**
	 * Identifiant unique du client connecté au serveur.
	 */
	private int idClient;

	/**
	 * Pseudonyme du client connecté.
	 */
	private String pseudoClient;

	/*--------------------------*/
	/* Constructeur             */
	/*--------------------------*/
	/**
	 * Constructeur de la classe Métier.
	 * Initialise les interfaces d'envoi et de notification pour la communication
	 * bidirectionnelle entre l'IHM et le serveur.
	 * 
	 * @param iEnvoyeur  interface d'envoi pour communiquer avec le serveur
	 * @param iNotifieur interface de notification pour communiquer avec l'IHM
	 */
	public Metier(IEnvoyeur iEnvoyeur, INotifieur iNotifieur)
	{
		this.iEnvoyeur  = iEnvoyeur;
		this.iNotifieur = iNotifieur;
	}

	/*-----------------------------------*/
	/* METIER                            */
	/*-----------------------------------*/
	/*--------------------------*/
	/* Client */
	/*--------------------------*/
	/**
	 * Configure les informations du client après une connexion réussie.
	 * Met à jour l'identifiant et le pseudonyme du client, puis notifie
	 * l'IHM du succès de la connexion.
	 * 
	 * @param idClient     l'identifiant unique attribué au client par le serveur
	 * @param pseudoClient le pseudonyme du client connecté
	 */
	public void setClient(int idClient, String pseudoClient)
	{
		this.idClient     = idClient;
		this.pseudoClient = pseudoClient;

		this.iNotifieur.succesLogin(this.pseudoClient);
	}

	/*-----------------------*/
	/* Connexion/Inscription */
	/*-----------------------*/
	/**
	 * Tente la connexion d'un client au serveur avec ses identifiants.
	 * Crée un événement LOGIN avec le pseudo et le mot de passe, puis l'envoie au
	 * serveur.
	 * 
	 * @param pseudo le pseudonyme du client
	 * @param mdp    le mot de passe du client
	 */
	public void connecterAuClient(String pseudo, String mdp) 
	{
		// Création du message eventDTO
		ChatEventDTO event = new ChatEventDTO(EventEnum.LOGIN)
				.add(EventEnum.LOGIN.getKeyIndex(0), pseudo)
				.add(EventEnum.LOGIN.getKeyIndex(1), mdp   );

		this.iEnvoyeur.envoyer(event);
	}

	/**
	 * Enregistre un nouvel utilisateur sur le serveur.
	 * Crée un événement SIGNUP avec les identifiants, l'envoie au serveur,
	 * puis tente automatiquement la connexion avec ces mêmes identifiants.
	 * 
	 * @param pseudo le pseudonyme souhaité pour le nouvel utilisateur
	 * @param mdp    le mot de passe souhaité pour le nouvel utilisateur
	 */
	public void enregistrerUtilisateur(String pseudo, String mdp) 
	{
		// Création du message eventDTO
		ChatEventDTO event = new ChatEventDTO(EventEnum.SIGNUP)
				.add(EventEnum.SIGNUP.getKeyIndex(0), pseudo)
				.add(EventEnum.SIGNUP.getKeyIndex(1), mdp   );

		this.iEnvoyeur.envoyer(event);
		this.connecterAuClient(pseudo, mdp);
	}

	/*-----------------------*/
	/* Envoyer Message       */
	/*-----------------------*/
	/**
	 * Envoie un message texte dans le canal de chat actuel.
	 * Crée un événement NEW_MESSAGE avec l'identifiant du client, du canal et le
	 * contenu,
	 * puis l'envoie au serveur. Affiche des informations de débogage dans la
	 * console.
	 * 
	 * @param texte le contenu du message à envoyer
	 */
	public void envoyerMessage(String texte)
	{
		// Création du message eventDTO
		ChatEventDTO event = new ChatEventDTO(EventEnum.NEW_MESSAGE)
				.add(EventEnum.NEW_MESSAGE.getKeyIndex(0), this.idClient )
				.add(EventEnum.NEW_MESSAGE.getKeyIndex(1), this.idChannel)
				.add(EventEnum.NEW_MESSAGE.getKeyIndex(2), texte         );

		// System.out.println(this.idClient );
		// System.out.println(this.idChannel);
		this.iEnvoyeur.envoyer(event);
	}

	/**
	 * Envoie une pièce jointe (image) dans le canal de chat actuel.
	 * Crée un événement NEW_MESSAGE_IMG avec l'identifiant du canal, du client
	 * et les données binaires de la pièce jointe, puis l'envoie au serveur.
	 * 
	 * @param bytes les données binaires de la pièce jointe à envoyer
	 */
	public void envoyerPieceJoint(byte[] bytes) 
	{
		// Création du message eventDTO NEW_MESSAGE_IMG ( List.of( "IdGroupe" , "idClient", "byte" ) ),
		ChatEventDTO event = new ChatEventDTO(EventEnum.NEW_MESSAGE_IMG)
				.add(EventEnum.NEW_MESSAGE.getKeyIndex(0), this.idChannel)
				.add(EventEnum.NEW_MESSAGE.getKeyIndex(1), this.idClient )
				.add(EventEnum.NEW_MESSAGE.getKeyIndex(2), bytes         );

		this.iEnvoyeur.envoyer(event);
	}

	/*-----------------------------------*/
	/* INotificateur                     */
	/*-----------------------------------*/
	/**
	 * Traite les événements de notification reçus du serveur.
	 * Analyse le type d'événement et effectue l'action appropriée :
	 * - SUCCESS_LOGIN/SUCCESS_SIGNUP : configure le client et initialise le canal
	 * - MESSAGE_LIST : affiche la liste des messages via l'IHM
	 * - MESSAGE : affiche un nouveau message via l'IHM
	 * - ERROR : notifie l'erreur à l'utilisateur via l'IHM
	 * 
	 * @param event l'événement de chat reçu du serveur à traiter
	 */
	public void notifierMessage(ChatEventDTO event)
	{
		if ( event.getType().equals(EventEnum.SUCCESS_LOGIN) || event.getType().equals(EventEnum.SUCCESS_SIGNUP) ) 
		{
			int id        = ((Number) event.getDataIndex(0)).intValue();
			String pseudo = (String) event.getDataIndex(1);

			this.setClient(id, pseudo);
			this.idChannel = 1;
			return;
		}

		if (event.getType().equals(EventEnum.MESSAGE_LIST)) 
		{
			this.iNotifieur.afficherListMessage(ChatEventDTO.jsonToLstEventDTO(event.toJson()));
			return;
		}

		if (event.getType().equals(EventEnum.MESSAGE)) 
		{
			this.iNotifieur.afficherNvMessage(event);
			return;
		}

		if (event.getType().equals(EventEnum.ERROR)) 
		{
			String erreur = (String) event.getDataIndex(0);

			this.iNotifieur.notifierErreur(erreur);
			return;
		}
	}

}
