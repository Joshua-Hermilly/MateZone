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


public class WebSocketMateZone extends WebSocketServer implements IWebSocketMateZone
{
	private ClientService clientService;
	private HashMap<WebSocket,Integer> hsClientChannel;

	public WebSocketMateZone(int port, ClientService clientService) 
	{
		super( new InetSocketAddress( port ) );

		this.clientService = clientService;
		this.hsClientChannel = new HashMap<WebSocket,Integer>();

	}	


	public void setClientChannel(WebSocket client, int idChannel)
	{
		this.hsClientChannel.put(client, idChannel);
	}	
	
	public void delClientChannel(WebSocket client)
	{
		this.hsClientChannel.remove(client);
	}

	public void broadcast(int idChannel, ChatEventDTO eventRec)
	{
		for (WebSocket client : this.hsClientChannel.keySet()) 
		{
			if ( this.hsClientChannel.get(client) == idChannel)
			{
				client.send(eventRec.toJson());
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

		this.setClientChannel(client, 1);
	}

	// Message Reçu
	@Override
	public void onMessage(WebSocket client, String message) 
	{
		if ( message == null ) return;

		ChatEventDTO event = ChatEventDTO.jsonToEventDTO(message);

		// Traitement des différents types de messages
		if ( event.getType() == EventEnum.LOGIN       )  { this.clientService.handleLogin     (client, event); }
		if ( event.getType() == EventEnum.SIGNUP      )  { this.clientService.handleRegister  (client, event); }
		if ( event.getType() == EventEnum.NEW_MESSAGE )  { this.clientService.handleNewMessage(client, event); }
		if ( event.getType() == EventEnum.NEW_CHANNEL )  { this.clientService.handleNewChannel(client, event); }

		System.out.println( event );
	}

	// Client Déconnécté
	@Override
	public void onClose(WebSocket client, int code, String reason, boolean remote) 
	{
		System.out.println("Un client s'est déconnecté.");

		this.delClientChannel(client);
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