package client.metier.interfaces;

import common.dto.ChatEventDTO;

/*-------------------------------*/
/* Interface IEnvoyeur           */
/*-------------------------------*/
/**
 * Interface qui fait office de passerelle entre la couche métier et le serveur.
 * Définit le contrat pour l'envoi d'événements de chat vers le serveur.
 * Cette interface suit le pattern Hexagonal Architecture (Port) pour découpler
 * la logique métier de l'implémentation de communication réseau.
 * 
 * Implémentée par : client/infrastructure/websocket/WebSocketChatAdapter
 * Utilisée par : client/metier/Metier
 *
 * @author Joshua Hermilly
 * @version V1
 * @date 08/11/25
 */
public interface IEnvoyeur 
{
	/**
	 * Établit une connexion avec le serveur de chat.
	 * Cette méthode doit être appelée avant tout envoi d'événements.
	 * 
	 * @throws Exception si la connexion au serveur échoue
	 */
	void connecter() throws Exception;

	/**
	 * Envoie un événement de chat au serveur.
	 * L'événement contient les données du message, connexion, ou autre action
	 * à transmettre au serveur selon le protocole de communication défini.
	 * 
	 * @param event l'événement de chat à envoyer au serveur
	 */
	void envoyer(ChatEventDTO event);
}
