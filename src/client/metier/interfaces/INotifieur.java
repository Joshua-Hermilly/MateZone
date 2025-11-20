package client.metier.interfaces;

import common.dto.ChatEventDTO;

/*-------------------------------*/
/* Interface INotifieur          */
/*-------------------------------*/
/**
 * Interface qui fait office de passerelle entre le serveur et le contrôleur.
 * Définit le contrat pour la notification d'événements depuis la couche
 * infrastructure
 * vers la couche présentation. Cette interface suit le pattern Hexagonal
 * Architecture
 * pour découpler la réception des données du serveur de leur traitement par
 * l'IHM.
 * 
 * Utilisée par : client/infrastructure/websocket/WebSocketChatAdapter
 * Implémentée par : client/controleur/Controleur
 *
 * @author Joshua Hermilly
 * @version V1
 * @date 08/11/25
 */
public interface INotifieur 
{
	/**
	 * Notifie la réception d'un message depuis le serveur.
	 * Cette méthode est appelée par la couche infrastructure pour transmettre
	 * les événements reçus du serveur vers la couche métier pour traitement.
	 * 
	 * @param eventDTO l'événement de chat reçu du serveur
	 */
	void notifierMessage(ChatEventDTO eventDTO);

	/**
	 * Notifie le succès d'une connexion utilisateur.
	 * Déclenche l'affichage de l'interface principale de chat avec le pseudo de
	 * l'utilisateur.
	 * 
	 * @param pseudo le pseudonyme de l'utilisateur connecté avec succès
	 */
	void succesLogin(String pseudo);

	/**
	 * Demande l'affichage d'une liste de messages dans l'interface utilisateur.
	 * Utilisée pour afficher l'historique des messages lors de la connexion au
	 * canal.
	 * 
	 * @param eventDTO l'événement contenant la liste des messages à afficher
	 */
	void afficherListMessage(ChatEventDTO eventDTO);

	/**
	 * Demande l'affichage d'un nouveau message dans l'interface utilisateur.
	 * Utilisée pour afficher en temps réel les nouveaux messages reçus.
	 * 
	 * @param eventDTO l'événement contenant le nouveau message à afficher
	 */
	void afficherNvMessage(ChatEventDTO eventDTO);

	/**
	 * Notifie une erreur à afficher à l'utilisateur.
	 * Utilisée pour informer l'utilisateur des erreurs de connexion,
	 * d'authentification
	 * ou autres problèmes survenus côté serveur.
	 * 
	 * @param erreur le message d'erreur à afficher à l'utilisateur
	 */
	void notifierErreur(String erreur);
}
