package MateZone.metier.client;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;

public class ClientConnexion extends WebSocketClient
{
	public ClientConnexion() throws Exception
	{
		super( new URI( "ws://localhost:8080" ) );
	}

	/**
	 * Constructeur permettant de définir l'hôte et le port du serveur WebSocket.
	 *
	 * @param host adresse de l'hôte (ex: "localhost")
	 * @param port port du serveur (ex: 8080)
	 * @throws Exception si l'URI est invalide
	 */
	public ClientConnexion(String host, int port) throws Exception
	{
		super( new URI( "ws://" + host + ":" + port ) );
	}

	/*-------------------------------*/
	/* Autres Méthodes               */
	/*-------------------------------*/

	// Connexion au Serveur
	@Override
	public void onOpen(ServerHandshake handshake) 
	{
		System.out.println("Connecté au serveur !"); // Message console
		send( "Gentle :)" ); // Envoi d'un message initial au serveur
	}

	// Message Reçu
	@Override
	public void onMessage( String message ) 
	{
		System.out.println("Serveur : " + message); // Affiche le message reçu
	}

	// Connexion arrétée 
	@Override
	public void onClose(int code, String reason, boolean remote) 
	{
		System.out.println("Déconnecté du serveur."); // Message console
	}

	// Erreur Connexion/Communication
	@Override
	public void onError(Exception ex) 
	{
		System.out.println("Erreur : " + ex.getMessage()); 
	}
}