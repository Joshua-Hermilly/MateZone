package server.Protocol.webSocket;

import org.java_websocket.WebSocket;

import common.dto.ChatEventDTO;

import java.util.HashMap;

/**
 * Utilitaire pour gérer la correspondance entre idClient et WebSocket.
 */
	public class WebSocketClientsConnecter 
	{
		private final HashMap<Integer, WebSocket> idToSocket      = new HashMap<>();
		private final HashMap<WebSocket, Integer> socketToId      = new HashMap<>();
		private final HashMap<WebSocket, Integer> socketToChannel = new HashMap<>();

		public void register(int idClient, WebSocket socket) 
		{
			idToSocket.put(idClient, socket);
			socketToId.put(socket, idClient);
			socketToChannel.put(socket, 0);
		}

		public void unregister(WebSocket socket)
		{
			Integer id = socketToId.remove(socket);
			if (id != null) 
			{
				idToSocket.remove(id);
			}
			socketToChannel.remove(socket);
		}

		public void broadcast(int idChannel, ChatEventDTO eventRec) 
		{
			for (WebSocket client : this.socketToChannel.keySet()) 
			{
			if (this.socketToChannel.get(client) == idChannel) 
			{
				client.send(eventRec.toJson());
			}
			}
		}

		public WebSocket getSocket (int idClient)     { return this.idToSocket.get(idClient);   }
		public Integer   getId     (WebSocket socket) { return this.socketToId.get(socket);     }
		public Integer   getChannel(WebSocket socket) { return this.socketToChannel.get(socket);}

		public void setChannel(WebSocket socket, int idChannel) {socketToChannel.put(socket, idChannel);}

	}
