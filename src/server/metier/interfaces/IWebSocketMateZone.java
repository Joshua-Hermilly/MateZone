package server.metier.interfaces;

import org.java_websocket.WebSocket;

import common.dto.ChatEventDTO;

public interface IWebSocketMateZone {

	void broadcast(int idChannel, ChatEventDTO enventRec);
	void setClientChannel(WebSocket client, int idChannel);
}
