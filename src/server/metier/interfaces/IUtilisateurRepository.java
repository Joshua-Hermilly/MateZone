package server.metier.interfaces;

import java.net.http.WebSocket;
import java.util.HashMap;

import server.metier.model.Client;

/*-----------------------------------*/
/* Interface IUtilisateurRepository  */
/*-----------------------------------*/
/**
 * Interface définissant les opérations de persistance pour les utilisateurs.
 * Cette interface fait partie de l'architecture hexagonale côté serveur et
 * définit
 * le contrat pour l'accès aux données utilisateur (table clients en base de
 * données).
 * Elle permet de découpler la logique métier de l'implémentation de
 * persistence.
 * 
 * Couche métier ↔ Repository utilisateur (Base de données)
 *
 * @author Prévost Donovan
 * @version V1
 * @date 08/11/25
 */
public interface IUtilisateurRepository 
{
	/**
	 * Authentifie un utilisateur avec ses identifiants.
	 * Vérifie si le couple pseudo/mot de passe existe en base de données.
	 * 
	 * @param pseudo le pseudonyme de l'utilisateur
	 * @param mdp    le mot de passe de l'utilisateur
	 * @return l'identifiant de l'utilisateur si l'authentification réussit, -1
	 *         sinon
	 */
	int authenticate(String pseudo, String mdp);

	/**
	 * Crée un nouveau client en base de données.
	 * Insère les informations du client et génère un identifiant unique.
	 * 
	 * @param client l'objet client contenant les informations à persister
	 * @return l'identifiant généré du nouveau client, -1 en cas d'échec
	 */
	int createClient(Client client);

	/**
	 * Récupère l'avatar d'un client par son identifiant.
	 * Retourne les données binaires de l'image d'avatar stockée en base.
	 * 
	 * @param clientId l'identifiant du client
	 * @return les données binaires de l'avatar, null si non trouvé
	 */
	byte[] getAvatarById(int clientId);


	/**
	 * Récupère l'avatar d'un client par son identifiant.
	 * Retourne les données binaires de l'image d'avatar stockée en base.
	 * 
	 * @param clientId l'identifiant du client
	 * @return les données binaires de l'avatar, null si non trouvé
	 */
	HashMap<Integer, String> permChannel(int idclient);


	

}
