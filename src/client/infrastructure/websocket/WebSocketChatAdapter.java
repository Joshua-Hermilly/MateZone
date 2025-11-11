package client.infrastructure.websocket;

import common.dto.ChatEventDTO;
import com.google.gson.Gson;

import client.metier.interfaces.IEnvoyeur;
import client.metier.interfaces.INotifieur;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;

/*-------------------------------*/
/* Class WebSocketChatAdapter    */
/*-------------------------------*/
/**
 * Adaptateur WebSocket pour la communication avec le serveur de chat MateZone.
 * Cette classe étend WebSocketClient et implémente IEnvoyeur pour gérer la
 * connexion,
 * l'envoi et la réception des messages via WebSocket. Elle fait le pont entre
 * la couche métier et la communication réseau en utilisant le protocole
 * WebSocket.
 * Les messages sont sérialisés/désérialisés en JSON via Gson.
 *
 * @author Joshua Hermilly
 * @version V1
 * @date 08/11/25
 */
public class WebSocketChatAdapter extends WebSocketClient implements IEnvoyeur 
{
	/*-------------------------------*/
	/* Attributs                     */
	/*-------------------------------*/
	/**
	 * Interface de notification utilisée pour transmettre les messages reçus
	 * du serveur vers la couche métier de l'application.
	 */
	private final INotifieur iNotifieur;

	/**
	 * Instance Gson pour la sérialisation et désérialisation des messages JSON
	 * échangés avec le serveur WebSocket.
	 */
	private Gson gson = new Gson();

	/*-------------------------------*/
	/* Constructeur                  */
	/*-------------------------------*/
	/**
	 * Constructeur de l'adaptateur WebSocket.
	 * Initialise la connexion WebSocket vers le serveur et configure le notifieur
	 * pour la gestion des messages entrants.
	 * 
	 * @param serverUri  l'URI du serveur WebSocket (ex: "ws://localhost:8080")
	 * @param iNotifieur l'interface de notification pour transmettre les messages
	 *                   reçus
	 * @throws Exception si l'URI du serveur est invalide ou si l'initialisation
	 *                   échoue
	 */
	public WebSocketChatAdapter(String serverUri, INotifieur iNotifieur) throws Exception 
	{
		super( new URI(serverUri) );
		this.iNotifieur = iNotifieur;
	}

	/*-------------------------------*/
	/* Envoie message                */
	/*-------------------------------*/
	/**
	 * Envoie un événement de chat au serveur via WebSocket.
	 * Vérifie d'abord que la connexion est ouverte, sérialise l'événement en JSON
	 * et l'envoie au serveur. Affiche une erreur si la connexion n'est pas
	 * disponible.
	 * 
	 * @param event l'événement de chat à envoyer au serveur
	 */
	@Override
	public void envoyer(ChatEventDTO event) 
	{
		if (!this.isOpen())
		{
			System.err.println("Erreur serveur");
			return;
		}

		// transfomer messageDTO en Json
		String json = gson.toJson(event);
		this.send(json);
	}

	/*-------------------------------*/
	/* Connexion au serveur          */
	/*-------------------------------*/
	/**
	 * Établit une connexion WebSocket avec le serveur.
	 * Utilise une connexion bloquante qui attend la confirmation de connexion
	 * avant de continuer. Ne tente la connexion que si elle n'est pas déjà ouverte.
	 * 
	 * @throws InterruptedException si la connexion est interrompue pendant
	 *                              l'attente
	 */
	@Override
	public void connecter() throws InterruptedException 
	{
		if (!this.isOpen())
			this.connectBlocking(); // Bloque jusqu'à connexion
	}

	/*-------------------------------*/
	/* Méthodes Override             */
	/*-------------------------------*/

	/**
	 * Callback appelé lors de l'ouverture réussie de la connexion WebSocket.
	 * Affiche un message de confirmation de connexion dans la console.
	 * 
	 * @param handshakedata les données de handshake fournies par le serveur
	 */
	@Override
	public void onOpen(ServerHandshake handshakedata) 
	{
		System.out.println("Connecté au serveur WebSocket !");
	}

	/**
	 * Callback appelé lors de la réception d'un message du serveur.
	 * Désérialise le message JSON reçu en ChatEventDTO et le transmet
	 * au notifieur pour traitement par la couche métier.
	 * 
	 * @param message le message JSON reçu du serveur
	 */
	@Override
	public void onMessage(String message) 
	{
		System.out.println("Message reçu : " + message);

		ChatEventDTO event = ChatEventDTO.jsonToEventDTO(message);
		this.iNotifieur.notifierMessage(event);
	}

	/**
	 * Callback appelé lors de la fermeture de la connexion WebSocket.
	 * Affiche la raison de la déconnexion dans la console.
	 * 
	 * @param code   le code de fermeture WebSocket
	 * @param reason la raison de la fermeture fournie par le serveur
	 * @param remote indique si la fermeture a été initiée par le serveur distant
	 */
	@Override
	public void onClose(int code, String reason, boolean remote) 
	{
		System.out.println("Déconnecté : " + reason);
	}

	/**
	 * Callback appelé en cas d'erreur dans la connexion WebSocket.
	 * Affiche la trace complète de l'exception dans la console pour le débogage.
	 * 
	 * @param ex l'exception qui a provoqué l'erreur
	 */
	@Override
	public void onError(Exception ex) 
	{
		ex.printStackTrace();
	}
}
