package client.metier.interfaces;

import common.dto.ChatEventDTO;

/*-------------------------------*/
/* Intrerface IChatPort          */
/*-------------------------------*/
/**
 * Intefarce qui fait office de passerelle entre le metier et le serveur.
 * client/metier  ->  client/infrastructure/websocket/WebSocketChatAdapter
 */
public interface IEnvoyeur 
{
	void connecter() throws Exception;
	void envoyer(ChatEventDTO event);
}
