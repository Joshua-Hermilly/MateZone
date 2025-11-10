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
 * Classe principale de la relation avec le serveur.
 * Gère la connexion, envoie reception des messages du serveur.
 */
public class WebSocketChatAdapter extends WebSocketClient implements IEnvoyeur 
{
	/*-------------------------------*/
	/* Attributs                     */
	/*-------------------------------*/
	private final INotifieur iNotifieur;
	private Gson gson = new Gson();

	/*-------------------------------*/
	/* Constructeur                  */
	/*-------------------------------*/
	public WebSocketChatAdapter( String serverUri, INotifieur iNotifieur ) throws Exception 
	{
		super( new URI( serverUri ) );
		this.iNotifieur = iNotifieur;
	}


	/*-------------------------------*/
	/* Envoie message                */
	/*-------------------------------*/
	@Override
	public void envoyer( ChatEventDTO event ) 
	{
		if ( !this.isOpen() ) { System.err.println( "Erreur serveur" ); return; }
		
		//transfomer messageDTO en Json 
		String json = gson.toJson( event );
		this.send( json );
	}


	/*-------------------------------*/
	/* Connexion au serveur          */
	/*-------------------------------*/
	@Override
	public void connecter() throws InterruptedException 
	{
		if ( !this.isOpen() ) this.connectBlocking(); // Bloque jusqu'à connexion
	}


	/*-------------------------------*/
	/* Méthodes Override             */
	/*-------------------------------*/

	@Override
	public void onOpen( ServerHandshake handshakedata )
	{
		System.out.println( "Connecté au serveur WebSocket !" );
	}

	@Override
	public void onMessage( String message )
	{
		System.out.println( "Message reçu : " + message );

		ChatEventDTO event = ChatEventDTO.jsonToEventDTO(message);
		this.iNotifieur.notifierMessage( event );
	}

	@Override
	public void onClose( int code, String reason, boolean remote )
	{
		System.out.println( "Déconnecté : " + reason );
	}

	@Override
	public void onError( Exception ex )
	{
		ex.printStackTrace();
	}
}
