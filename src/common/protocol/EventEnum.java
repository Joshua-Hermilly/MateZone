package common.protocol;

import java.util.List;

/*-------------------------------*/
/* Enum EventEnum                */
/*-------------------------------*/
/**
 * Énumération responsable des types d'événements et de leurs clés de données
 * pour ChatEventDTO.
 * Cette enum définit le protocole de communication standardisé entre le Client
 * et le Serveur
 * en spécifiant les types d'événements disponibles et les clés de données
 * requises pour chacun.
 * Elle garantit la cohérence et la validation des échanges de messages dans
 * l'application MateZone.
 *
 * @author MateZone Team
 * @author Joshua Hermilly
 * @author Prévost Donovan
 * @version V1
 * @date 08/11/25
 */
public enum EventEnum
{
	/*--------------------------*/
	/* Constantes               */
	/*--------------------------*/

	/**
	 * Événement de connexion utilisateur.
	 * Clés requises : pseudo, mot de passe
	 */
	LOGIN(List.of("pseudo", "mdp")),

	/**
	 * Événement d'inscription d'un nouvel utilisateur.
	 * Clés requises : pseudo, mot de passe
	 */
	SIGNUP(List.of("pseudo", "mdp")),
	
	/**
	 * Événement 
	 * Clés requises : idChannel, nomChannel
	 */
	PERM_CHANNEL(List.of("idChannel", "nomChannel")),

	/**
	 * Événement 
	 * Clés requises : liste d'event 
	 */
	PERMS_CHANNELS(List.of()),

	/**
	 * Événement de succès de connexion.
	 * Clés requises : identifiant utilisateur, pseudo
	 */
	SUCCESS_LOGIN(List.of("id", "pseudo")),

	/**
	 * Événement de succès d'inscription.
	 * Clés requises : identifiant utilisateur, pseudo
	 */
	SUCCESS_SIGNUP(List.of("id", "pseudo")),

	/**
	 * Événement de connexion à un nouveau canal de chat.
	 * Clés requises : identifiant du canal
	 */
	NEW_CHANNEL(List.of("idChannel")),

	/**
	 * Événement représentant un message de chat complet.
	 * Clés requises : identifiant client, pseudo, contenu, date
	 */
	MESSAGE(List.of("idClient", "pseudo", "contenu", "date")),

	/**
	 * Événement contenant une liste de messages de chat.
	 * Aucune clé requise (utilise la liste d'événements)
	 */
	MESSAGE_LIST(List.of()),

	/**
	 * Événement d'envoi d'un nouveau message texte.
	 * Clés requises : identifiant client, identifiant canal, contenu
	 */
	NEW_MESSAGE(List.of("idClient", "idChannel", "contenu")),

	/**
	 * Événement d'envoi d'un nouveau message avec image.
	 * Clés requises : identifiant groupe, identifiant client, données binaires
	 */
	NEW_MESSAGE_IMG(List.of("IdGroupe", "idClient", "byte")),

	 /**
     * Événement représentant le changement d'un canal côté client.
     */
    CHANGER_CHANNEL(List.of("idChannel")),

	/**
	 * Événement de succès général avec message.
	 * Clés requises : message de succès
	 */
	SUCCESS(List.of("message")),

	/**
	 * Événement d'erreur avec message explicatif.
	 * Clés requises : message d'erreur
	 */
	ERROR(List.of("message"));

	/*--------------------------*/
	/* Attribut                 */
	/*--------------------------*/
	/**
	 * Liste des clés de données requises pour ce type d'événement.
	 * Définit l'ordre et les noms des données attendues dans le ChatEventDTO.
	 */
	private final List<String> composants;

	/*--------------------------*/
	/* Constructeur             */
	/*--------------------------*/
	/**
	 * Constructeur de l'énumération EventEnum.
	 * Initialise les clés de données requises pour ce type d'événement.
	 * 
	 * @param composants la liste des noms de clés requis pour ce type d'événement
	 */
	EventEnum(List<String> composants) 
	{
		this.composants = composants;
	}

	/*--------------------------*/
	/* Getter                   */
	/*--------------------------*/
	/**
	 * Récupère la liste complète des clés de données requises pour ce type
	 * d'événement.
	 * 
	 * @return la liste des noms de clés requis dans l'ordre défini
	 */
	public List<String> getRequiredKeys()
	{
		return this.composants;
	}

	/**
	 * Récupère le nom de la clé à un index spécifique.
	 * Permet d'accéder aux clés par leur position dans l'ordre défini.
	 * 
	 * @param index l'index de la clé dans la liste des clés requises
	 * @return le nom de la clé à cet index, ou null si l'index est invalide
	 */
	public String getKeyIndex(int index) 
	{
		if (index >= this.getRequiredKeys().size())
			return null;
	
		return this.getRequiredKeys().get(index);
	}
}
