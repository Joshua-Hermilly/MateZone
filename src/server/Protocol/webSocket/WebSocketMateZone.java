package server.Protocol.webSocket;

import java.net.InetSocketAddress;
import java.util.HashMap;

import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import common.dto.ChatEventDTO;
import common.protocol.EventEnum;
import server.metier.interfaces.IWebSocketMateZone;
import server.metier.service.ClientService;
import org.java_websocket.WebSocket;

/**
 * Serveur WebSocket principal de l'application MateZone.
 * Cette classe étend WebSocketServer et implémente IWebSocketMateZone pour
 * gérer
 * la communication temps réel avec les clients connectés. Elle orchestre les
 * connexions,
 * traite les messages entrants selon le protocole défini et gère l'association
 * des clients aux canaux de chat.
 * 
 * Le serveur suit l'architecture hexagonale en implémentant l'interface métier
 * et en déléguant la logique business au ClientService.
 *
 * @author Prévost Donovan
 * @version V1
 * @date 08/11/25
 */
public class WebSocketMateZone extends WebSocketServer implements IWebSocketMateZone 
{
	/*-------------------------------*/
	/* Attributs                     */
	/*-------------------------------*/

	/**
	 * Service métier gérant la logique business des clients.
	 * Traite l'authentification, l'inscription et l'envoi de messages.
	 */
	private ClientService clientService;

	/**
	 * Map associant chaque connexion WebSocket à son canal de chat actuel.
	 * Permet de savoir dans quel canal se trouve chaque client connecté.
	 */
	private HashMap<WebSocket, Integer> hsClientChannel;

	/*-------------------------------*/
	/* Constructeur                  */
	/*-------------------------------*/

	/**
	 * Constructeur du serveur WebSocket MateZone.
	 * Initialise le serveur sur le port spécifié et configure le service client
	 * pour traiter la logique métier. Initialise également la map des canaux
	 * clients.
	 * 
	 * @param port          le port d'écoute du serveur WebSocket
	 * @param clientService le service métier pour traiter les opérations client
	 */
	public WebSocketMateZone(int port, ClientService clientService) 
	{
		super(new InetSocketAddress(port));

		this.clientService    = clientService;
		this.hsClientChannel = new HashMap<WebSocket, Integer>();

	}

	/*-------------------------------*/
	/* Méthodes utilitaires          */
	/*-------------------------------*/

	/**
	 * Associe un client WebSocket à un canal de chat spécifique.
	 * Permet de suivre dans quel canal se trouve chaque client connecté.
	 * 
	 * @param client    la connexion WebSocket du client
	 * @param idChannel l'identifiant du canal de chat
	 */
	public void setClientChannel(WebSocket client, int idChannel) 
	{
		this.hsClientChannel.put(client, idChannel);
	//	this.clientService.handleNewChannel(client, idChannel);
	}

	/**
	 * Supprime l'association d'un client avec son canal de chat.
	 * Utilisé lors de la déconnexion d'un client.
	 * 
	 * @param client la connexion WebSocket du client à supprimer
	 */
	public void delClientChannel(WebSocket client) 
	{
		this.hsClientChannel.remove(client);
	}

	/**
	 * Diffuse un événement de chat à tous les clients connectés sur un canal donné.
	 * Parcourt tous les clients connectés et envoie le message uniquement
	 * à ceux présents dans le canal spécifié.
	 * 
	 * @param idChannel l'identifiant du canal de diffusion
	 * @param eventRec  l'événement de chat à diffuser (contient le message et les
	 *                  métadonnées)
	 */
	public void broadcast(int idChannel, ChatEventDTO eventRec) 
	{
		for (WebSocket client : this.hsClientChannel.keySet()) 
		{
			if (this.hsClientChannel.get(client) == idChannel) 
			{
				client.send(eventRec.toJson());
			}
		}
	}

	/*-------------------------------*/
	/* Méthodes WebSocket @Override  */
	/*-------------------------------*/

	/**
	 * Méthode appelée lors de l'ouverture d'une nouvelle connexion WebSocket.
	 * Ajoute automatiquement le client au canal par défaut (canal 1) et
	 * affiche un message de confirmation de connexion.
	 * 
	 * @param client    la nouvelle connexion WebSocket
	 * @param handshake les informations de négociation WebSocket
	 */
	@Override
	public void onOpen(WebSocket client, ClientHandshake handshake) 
	{
		System.out.println("Un client vient de se connecter : " + client.getRemoteSocketAddress());
	}

	/**
	 * Méthode appelée lors de la réception d'un message WebSocket.
	 * Désérialise le message JSON en ChatEventDTO et dispatche le traitement
	 * vers le service client selon le type d'événement (LOGIN, SIGNUP, NEW_MESSAGE,
	 * NEW_CHANNEL).
	 * 
	 * @param client  la connexion WebSocket ayant envoyé le message
	 * @param message le message JSON reçu
	 */
	@Override
	public void onMessage(WebSocket client, String message) 
	{
		if (message == null)
			return;

		ChatEventDTO event = ChatEventDTO.jsonToEventDTO(message);

		// Traitement des différents types de messages
		if (event.getType() == EventEnum.LOGIN      ) { this.clientService.handleLogin     (client, event); }
		if (event.getType() == EventEnum.SIGNUP     ) { this.clientService.handleRegister  (client, event); }
		if (event.getType() == EventEnum.NEW_MESSAGE) { this.clientService.handleNewMessage(client, event); }
		if (event.getType() == EventEnum.CHANGER_CHANNEL) { this.clientService.handleNewChannel(client, event);}

		System.out.println(event);
	}

	/**
	 * Méthode appelée lors de la fermeture d'une connexion WebSocket.
	 * Supprime le client de la map des canaux pour libérer les ressources
	 * et affiche un message de déconnexion.
	 * 
	 * @param client la connexion WebSocket fermée
	 * @param code   le code de fermeture de la connexion
	 * @param reason la raison de la fermeture
	 * @param remote indique si la fermeture est initiée par le client distant
	 */
	@Override
	public void onClose(WebSocket client, int code, String reason, boolean remote) 
	{
		System.out.println("Un client s'est déconnecté.");

		this.delClientChannel(client);
	}

	/**
	 * Méthode appelée en cas d'erreur sur une connexion WebSocket.
	 * Affiche le message d'erreur pour faciliter le debug.
	 * 
	 * @param client la connexion WebSocket en erreur (peut être null pour les
	 *               erreurs serveur)
	 * @param ex     l'exception rencontrée
	 */
	@Override
	public void onError(WebSocket client, Exception ex) 
	{
		System.out.println("Erreur : " + ex.getMessage());
	}

	/**
	 * Méthode appelée au démarrage du serveur WebSocket.
	 * Affiche un message de confirmation indiquant que le serveur est opérationnel.
	 */
	@Override
	public void onStart() 
	{
		System.out.println("Serveur WebSocket démarré !");
	}

}