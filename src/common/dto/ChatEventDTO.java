package common.dto;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;

import common.protocol.EventEnum;

/*-------------------------------*/
/* Class ChatEventDTO            */
/*-------------------------------*/
/**
 * Classe responsable de la structure des événements de chat échangés en JSON.
 * Cette classe DTO (Data Transfer Object) permet d'avoir un format standardisé
 * et centralisé pour la communication entre le Client et le Serveur.
 * Elle encapsule le type d'événement, les données associées et gère la
 * sérialisation
 * JSON via Gson pour les échanges réseau.
 *
 * @author MateZone Team
 * @author Joshua Hermilly
 * @author Prévost Donovan
 * @version V1
 * @date 08/11/25
 */
public class ChatEventDTO 
{
	/*--------------------------*/
	/* Attributs                */
	/*--------------------------*/
	/**
	 * Type d'événement qui définit la nature de l'action (LOGIN, MESSAGE, etc.).
	 * Utilise l'énumération EventEnum pour garantir la cohérence des types.
	 */
	private EventEnum type;

	/**
	 * Données dynamiques associées à l'événement sous forme de map clé-valeur.
	 * Permet de stocker des informations variables selon le type d'événement.
	 */
	private Map<String, Object> data;

	/**
	 * Liste d'événements de chat utilisée spécifiquement pour
	 * EventEnum.MESSAGE_LIST.
	 * Permet de transporter plusieurs messages dans un seul événement.
	 */
	private List<ChatEventDTO> lstEventDTO;

	/*--------------------------*/
	/* Constructeurs            */
	/*--------------------------*/
	/**
	 * Constructeur par défaut.
	 * Initialise la map de données vide, prête à recevoir des éléments.
	 */
	public ChatEventDTO() 
	{
		this.data = new HashMap<>();
	}

	/**
	 * Constructeur avec type d'événement.
	 * Initialise l'événement avec un type spécifique et une map de données vide.
	 * 
	 * @param type le type d'événement à associer à cette instance
	 */
	public ChatEventDTO(EventEnum type)
	{
		this();
		this.type = type;
	}

	/**
	 * Constructeur pour les événements contenant une liste d'autres événements.
	 * Utilisé spécifiquement pour les événements de type MESSAGE_LIST.
	 * 
	 * @param type        le type d'événement (généralement MESSAGE_LIST)
	 * @param lstEventDTO la liste des événements de chat à inclure
	 */
	public ChatEventDTO(EventEnum type, List<ChatEventDTO> lstEventDTO) 
	{
		this.lstEventDTO = lstEventDTO;
		this.type        = type;
	}

	/*--------------------------*/
	/* Getters et Setters       */
	/*--------------------------*/
	/**
	 * Récupère le type d'événement.
	 * 
	 * @return le type d'événement associé à cette instance
	 */
	public EventEnum getType() 
	{
		return type;
	}

	/**
	 * Définit le type d'événement.
	 * 
	 * @param type le nouveau type d'événement à associer
	 */
	public void setType(EventEnum type) 
	{
		this.type = type;
	}

	/**
	 * Récupère la map des données associées à l'événement.
	 * 
	 * @return la map contenant les données clé-valeur de l'événement
	 */
	public Map<String, Object> getData() 
	{
		return data;
	}

	/**
	 * Définit la map des données associées à l'événement.
	 * 
	 * @param data la nouvelle map de données à associer
	 */
	public void setData(Map<String, Object> data)
	{
		this.data = data;
	}

	/**
	 * Récupère la liste des messages (utilisé pour les événements de type
	 * MESSAGE_LIST).
	 * 
	 * @return la liste des événements de chat contenus
	 */
	public List<ChatEventDTO> getLstMes() 
	{
		return this.lstEventDTO;
	}

	/**
	 * Récupère une donnée spécifique par son index dans l'ordre des clés requises.
	 * Utilise l'ordre défini dans EventEnum.getRequiredKeys() pour accéder aux
	 * données.
	 * 
	 * @param index l'index de la donnée dans l'ordre des clés requises du type
	 *              d'événement
	 * @return la valeur associée à l'index, ou null si l'index est invalide
	 */
	public Object getDataIndex(int index) 
	{
		if (index >= this.getType().getRequiredKeys().size())
			return null;

		Object object = this.getData().get(this.getType().getRequiredKeys().get(index));
		return object;
	}

	/**
	 * Récupère la liste complète des événements de chat.
	 * 
	 * @return la liste des événements de chat contenus
	 */
	public List<ChatEventDTO> getLstEventDTO()
	{
		return lstEventDTO;
	}

	/**
	 * Définit la liste des événements de chat.
	 * 
	 * @param lstEventDTO la nouvelle liste d'événements à associer
	 */
	public void setLstEventDTO(List<ChatEventDTO> lstEventDTO)
	{
		this.lstEventDTO = lstEventDTO;
	}

	/*--------------------------*/
	/* Ajouter une donnée       */
	/*--------------------------*/
	/**
	 * Ajoute une paire clé-valeur aux données de l'événement.
	 * Utilise le pattern Builder pour permettre le chaînage des appels.
	 * 
	 * @param key   la clé sous laquelle stocker la valeur
	 * @param value la valeur à associer à la clé
	 * @return cette instance pour permettre le chaînage des appels
	 */
	public ChatEventDTO add(String key, Object value)
	{
		this.data.put(key, value);
		return this;
	}

	/*--------------------------*/
	/* Transformer Json         */
	/*--------------------------*/
	/**
	 * Désérialise une chaîne JSON en instance ChatEventDTO.
	 * Utilise Gson pour la conversion automatique depuis le format JSON.
	 * 
	 * @param json la chaîne JSON à désérialiser
	 * @return l'instance ChatEventDTO correspondant au JSON
	 */
	public static ChatEventDTO jsonToEventDTO(String json) 
	{
		Gson gson = new Gson();
		return gson.fromJson(json, ChatEventDTO.class);
	}

	/**
	 * Désérialise une chaîne JSON en instance ChatEventDTO avec liste d'événements.
	 * Spécialement conçue pour les événements de type MESSAGE_LIST qui contiennent
	 * une liste d'autres événements de chat.
	 * 
	 * @param json la chaîne JSON contenant un événement avec liste
	 * @return l'instance ChatEventDTO avec sa liste d'événements correctement
	 *         configurée
	 */
	public static ChatEventDTO jsonToLstEventDTO(String json) 
	{
		Gson gson = new Gson();
		ChatEventDTO mainEvent = gson.fromJson(json, ChatEventDTO.class);

		// Créer un nouveau ChatEventDTO avec le constructeur qui prend type et lstEventDTO
		return new ChatEventDTO(mainEvent.getType(), mainEvent.getLstEventDTO());
	}

	/**
	 * Sérialise cette instance en chaîne JSON.
	 * Utilise Gson pour la conversion automatique vers le format JSON.
	 * 
	 * @return la représentation JSON de cette instance
	 */
	public String toJson() 
	{
		Gson gson = new Gson();
		return gson.toJson(this);
	}

	/*--------------------------*/
	/* toString                 */
	/*--------------------------*/
	/**
	 * Représentation textuelle de l'événement de chat.
	 * Affiche le type et les données pour faciliter le débogage.
	 * 
	 * @return une chaîne décrivant le type et les données de l'événement
	 */
	@Override
	public String toString() 
	{
		return "ChatEventDTO{type='" + type + "', data=" + data + "}";
	}
}