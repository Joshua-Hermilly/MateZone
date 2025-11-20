package server.metier.interfaces;

import org.java_websocket.WebSocket;

import common.dto.ChatEventDTO;

/**
 * Interface définissant les opérations WebSocket pour la communication temps
 * réel.
 * Cette interface fait partie de l'architecture hexagonale côté serveur et
 * définit
 * le contrat pour la diffusion de messages et la gestion des canaux de chat.
 * Elle permet de découpler la logique métier de l'implémentation WebSocket.
 *
 * @author Prévost Donovan
 * @version V1
 * @date 08/11/25
 */
public interface IWebSocketMateZone 
{

	/**
	 * Diffuse un événement de chat à tous les clients connectés d'un canal
	 * spécifique.
	 * Utilisée pour envoyer un message à tous les participants d'une conversation.
	 * 
	 * @param idChannel l'identifiant du canal dans lequel diffuser le message
	 * @param eventRec  l'événement de chat à diffuser aux clients du canal
	 */
	void broadcast(int idChannel, ChatEventDTO eventRec);

	/**
	 * Associe une connexion client à un canal de chat spécifique.
	 * Permet de gérer dans quel canal se trouve chaque client connecté.
	 * 
	 * @param client    la connexion WebSocket du client
	 * @param idChannel l'identifiant du canal auquel associer le client
	 */
	void setClientChannel(WebSocket client, int idChannel);
}
