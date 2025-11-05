import org.java_websocket.server.WebSocketServer;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;

import java.net.InetSocketAddress;


public class ServeurMateZone extends WebSocketServer 
{
	/*-------------------------------*/
	/* Constructeur                  */
	/*-------------------------------*/
	public ServeurMateZone( int port ) 
	{
		super( new InetSocketAddress( port ) );
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
		System.out.println( "Message reçu : " + message );

		// Diffusion du message à tous les clients connectés (broadcast)
		for ( WebSocket clientCo : this.getConnections() ) 
		{
			clientCo.send( "Serveur → " + message );
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