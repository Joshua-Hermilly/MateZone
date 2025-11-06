package server;

import org.java_websocket.server.WebSocketServer;

import server.gestionBD.Request;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;

import java.net.InetSocketAddress;
import java.util.HashMap;
import com.google.gson.Gson;




public class ServeurMateZone extends WebSocketServer 
{
	private static Request bd;
	private static Gson gson = new Gson();


	/*-------------------------------*/
	/* Constructeur                  */
	/*-------------------------------*/
	public ServeurMateZone( int port ) 
	{
		super( new InetSocketAddress( port ) );
		ServeurMateZone.bd = new Request();
	}


	/*-------------------------------*/
	/* Autres Méthodes               */
	/*-------------------------------*/

	private void broadcast()
	{
		for ( WebSocket clientCo : this.getConnections() ) {clientCo.send("Broadcast");}
	}

	public HashMap<Integer, String[]>  hsmessages(int idChannel) { return ServeurMateZone.bd.getMessages(idChannel); }

	/*-------------------------------*/
	/* GESTION CLIENT                */
	/*-------------------------------*/

	/**
	 * Gère la connexion d'un client
	 * Format attendu: "LOGIN:pseudo:mdp"
	 */
	private void handleLogin(WebSocket client, String message)
	{
		String[] parties = message.split(":");
		
		if (parties.length == 3) 
		{
			String pseudo = parties[1];
			String mdp = parties[2];
			
			int idClient = ServeurMateZone.bd.authenticate(pseudo, mdp);
			client.send("CONNECT:" + idClient + ":" + ServeurMateZone.bd.getClientById(idClient).getPseudo());
			
			// Convertir la HashMap en JSON avec Gson
			HashMap<Integer, String[]> messages = hsmessages(1);
			String messagesJson = gson.toJson(messages);
			client.send("MESSAGES_LIST:" + messagesJson);
		}
	}

	/**
	 * Gère l'inscription d'un nouveau client
	 * Format attendu: "REGISTER:pseudo:mdp"
	 */
	private void handleRegister(WebSocket client, String message)
	{
		String[] parties = message.split(":");
		
		if (parties.length == 3) 
		{
			String pseudo = parties[1];
			String mdp = parties[2];
			
			Client user = new Client(pseudo, mdp);
			client.send("REGISTERED:" + ServeurMateZone.bd.createClient(user));
		}
	}

	/**
	 * Gère l'envoi d'un nouveau message
	 * Format attendu: "NEWMESSAGE:idClient:idChannel:leMessage"
	 */
	private void handleNewMessage(WebSocket client, String message)
	{
		String[] parties = message.split(":", 4); // Limite à 4 parties maximum
		
		if (parties.length == 4) 
		{
			int idClient = Integer.parseInt(parties[1]);
			int idChannel = Integer.parseInt(parties[2]);
			String nMessage = parties[3]; // Contient tout le reste, même avec des ":"
			
			if (ServeurMateZone.bd.sendMessage(idClient, idChannel, nMessage))
			{
				this.broadcast();
			}
		}
	}




	/*-------------------------------*/
	/* @Override                     */
	/*-------------------------------*/


	// Client Connécté
	@Override
	public void onOpen(WebSocket client, ClientHandshake handshake) 
	{
		System.out.println( "Un client vient de se connecter : " + client.getRemoteSocketAddress() );
	}

	// Message Reçu
	@Override
	public void onMessage(WebSocket client, String message) 
	{
		if ( message == null ) return;

		// Traitement des différents types de messages
		if ( message.startsWith("LOGIN:"     ) ) { handleLogin(client, message);     }
		if ( message.startsWith("REGISTER:"  ) ) { handleRegister(client, message);  }
		if ( message.startsWith("NEWMESSAGE:") ) { handleNewMessage(client, message);}
	}

	// Client Déconnécté
	@Override
	public void onClose(WebSocket client, int code, String reason, boolean remote) 
	{
		System.out.println("Un client s'est déconnecté.");
	}

	// Erreur
	@Override
	public void onError(WebSocket client, Exception ex) {
		System.out.println("Erreur : " + ex.getMessage());
	}

	// Lancement du serveur
	@Override
	public void onStart() 
	{
		System.out.println("Serveur WebSocket démarré !");
	}
	

	/*-------------------------------*/
	/* Main                          */
	/*-------------------------------*/

	// Point d'entrée du serveur
	public static void main(String[] args) 
	{
		int port = 8080; // port par défaut
		if (args.length > 0) {
			try {
				port = Integer.parseInt(args[0]);
			} catch (NumberFormatException nfe) {
				System.out.println("Argument invalide pour le port. Usage: java ServeurMateZone [port]");
				System.out.println("Utilisation du port par défaut : " + port);
			}
		}

		ServeurMateZone serveur = new ServeurMateZone(port);
		serveur.start(); // Création et démarrage du serveur
		System.out.println("Serveur démarré sur le port " + port);
	}
}