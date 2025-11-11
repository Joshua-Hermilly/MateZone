package server.Protocol.webSocket;

import java.net.InetSocketAddress;

import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import common.dto.ChatEventDTO;
import common.protocol.EventEnum;
import server.metier.service.ClientService;

import org.java_websocket.WebSocket;


public class WebSocketMateZone extends WebSocketServer
{
	private ClientService clientService;

	public WebSocketMateZone(int port, ClientService clientService) 
	{
		super( new InetSocketAddress( port ) );

		this.clientService = clientService;

	}	


	public void broadcast()
	{
		for ( WebSocket clientCo : this.getConnections() ) {clientCo.send("Broadcast");}
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

		ChatEventDTO event = ChatEventDTO.jsonToEventDTO(message);

		// Traitement des différents types de messages
		if ( event.getType() == EventEnum.LOGIN       )  { this.clientService.handleLogin(client, event);      }
		if ( event.getType() == EventEnum.SIGNUP      )  { this.clientService.handleRegister(client, event);   }
		if ( event.getType() == EventEnum.NEW_MESSAGE )  { this.clientService.handleNewMessage(client, event); }

		System.out.println( event );
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
}