package server;

import org.java_websocket.server.WebSocketServer;

import server.gestionBD.Request;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;

import java.net.InetSocketAddress;




public class ServeurMateZone extends WebSocketServer 
{
	private static Request bd;


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
		if ( message != null)
		{
		// Si le message est une annonce de nom+mdp : "LOGIN:pseudo:mdp"
		if ( message.startsWith("LOGIN:") ) 
		{
			String[] parties = message.split(":");
			
			if (parties.length == 3) 
			{
				client.send("CONNECT:" + ServeurMateZone.bd.authenticate(parties[1],parties[2]));
			}
		}

		// Si le message est une annonce: "REGISTER:pseudo:mdp"
		if ( message.startsWith("REGISTER:") ) 
		{
			String[] parties = message.split(":");
			
			
			if (parties.length == 3) 
			{
				Client user = new Client(parties[1],parties[2]);
				client.send("REGISTERED:" + ServeurMateZone.bd.createClient(user));
			}
		}

		// Si le message est une annonce: "NEWMESSAGE:idchannel:leMessage"
		if ( message.startsWith("NEWMESSAGE:") ) 
		{
			String[] parties = message.split(":", 4); // Limite à 3 parties maximum
			
			if (parties.length == 3) 
			{
				int idClient = Integer.parseInt(parties[1]);
				int idChannel = Integer.parseInt(parties[2]);
				String nMessage = parties[3]; // Contient tout le reste, même avec des ":"
				
				if(ServeurMateZone.bd.sendMessage(idClient, idChannel, nMessage))
				{
					this.broadcast();
				}
			}
		}


		}		

	}

	private void broadcast()
	{
		for ( WebSocket clientCo : this.getConnections() ) 
		{
			//clientCo.send();
		}
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