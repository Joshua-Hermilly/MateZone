package client.metier.connexionServeur;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;

import client.Controleur;

/*---------------------------------*/
/*  Class ClientConnexion          */
/*---------------------------------*/

public class ClientConnexion extends WebSocketClient
{
	/*-------------------------------*/
	/*     Attributs                 */
	/*-------------------------------*/
	private boolean    connectee;
	private int        idClient;

	/*-------------------------------*/
	/*     Constructeurs             */
	/*-------------------------------*/
	public ClientConnexion() throws Exception
	{
		super( new URI( "ws://localhost:8080" ) );

		this.connectee  = false;
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
	/* Envoie message                */
	/*-------------------------------*/
	public void envoyerMessage(String message) 
	{
		if   (this.isOpen()){ this.send(message);                                       } 
		else                { System.out.println("Impossible d'envoyer le message."); }
	}

	/*-------------------------------*/
	/* Message reçu                  */
	/*-------------------------------*/
	public void traiterMessage( String message ) 
	{
		if ( message.startsWith("LOGIN:"   ) ) { this.estConnectee( message ); };
		if ( message.startsWith("REGISTERED:") ) { this.estEnregistre( message ); }
	
		System.out.println("Message reçu du serveur : " + message);
	}


	/*-------------------------------*/
	/* Traitement message            */
	/*-------------------------------*/
	private void estConnectee(String message) 
	{
		// modifier !
		
		if ( ! message.equals("LOGIN:-1") ) { this.connectee = false; } 
		else 
		{ 
			this.connectee = true; 
			try { this.idClient = Integer.parseInt( message.split(":")[1] ); } catch (Exception e) {}
		}

		System.out.println( this.connectee ? "Connexion réussie !" : "Échec de la connexion." );
	}

	private void estEnregistre(String message) 
	{		
		if ( message.equals("REGISTERED:true") ) { System.out.println("Enregistrement réussi !"); } 
		else                                    { System.out.println("Échec de l'enregistrement."); }
	}



	/*-------------------------------*/
	/* Getters                       */
	/*-------------------------------*/
	public boolean estConnectee() { return connectee; }


	/*-------------------------------*/
	/* Méthodes Override             */
	/*-------------------------------*/

	// Connexion au Serveur
	@Override
	public void onOpen(ServerHandshake handshake) 
	{
		System.out.println("Connecté au serveur !"); // Message console
		send( "CONNECT:" ); // Envoi d'un message initial au serveur
	}

	// Message Reçu
	@Override
	public void onMessage( String message ) 
	{
		this.traiterMessage(message); // Affiche le message reçu
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