package client.metier.ports;

import common.dto.ChatEventDTO;

/*-------------------------------*/
/* Intrerface IChatPort          */
/*-------------------------------*/
/**
 * Intefarce qui fait office de passerelle entre le metier et la passerelle serveur.
 * client/metier  <->  client/infrastructure/websocket/WebSocketChatAdapter
 */
public interface IChatPort 
{
	void connecter() throws Exception;
	void envoyer(ChatEventDTO event);
}
