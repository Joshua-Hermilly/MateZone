package server.metier.interfaces;

import java.util.HashMap;

/*-------------------------------*/
/* Interface IMessageRepository  */
/*-------------------------------*/
/**
 * Interface définissant les opérations de persistance pour les messages de
 * chat.
 * Cette interface fait partie de l'architecture hexagonale côté serveur et
 * définit
 * le contrat pour l'accès aux données de messages (table messages en base de
 * données).
 * Elle permet de découpler la logique métier de l'implémentation de
 * persistance.
 * 
 * Couche métier ↔ Repository messages (Base de données)
 *
 * @author Prévost Donovan
 * @version V1
 * @date 08/11/25
 */
public interface IMessageRepository 
{
	/**
	 * Récupère tous les messages d'un canal de chat spécifique.
	 * Retourne une map associant les identifiants de messages aux données
	 * complètes.
	 * 
	 * @param idchannel l'identifiant du canal dont récupérer les messages
	 * @return une HashMap avec les ID de messages comme clés et les données comme
	 *         valeurs
	 */
	HashMap<Integer, String[]> getMessages(int idchannel);

	/**
	 * Récupère un message spécifique par son identifiant.
	 * Retourne les données complètes du message sous forme de tableau.
	 * 
	 * @param idMessage l'identifiant du message à récupérer
	 * @return un tableau contenant les données du message (id, pseudo, contenu,
	 *         date)
	 */
	String[] getMessage(int idMessage);

	/**
	 * Envoie et persiste un nouveau message dans un canal de chat.
	 * Insère le message en base de données avec l'horodatage automatique.
	 * 
	 * @param groupe_id l'identifiant du canal/groupe de destination
	 * @param idUser    l'identifiant de l'utilisateur expéditeur
	 * @param message   le contenu textuel du message
	 * @return l'identifiant généré du nouveau message, -1 en cas d'échec
	 */
	int sendMessage(int groupe_id, int idUser, String message);
}
