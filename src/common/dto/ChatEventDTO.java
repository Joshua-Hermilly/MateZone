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
 * Class responable de la fomre des JSon à envoyé.
 * Elle permet d'avoir une forme centralisé entre le Client et le Serveur.
 */
public class ChatEventDTO 
{
	/*--------------------------*/
	/*        Attributs         */
	/*--------------------------*/
	private EventEnum           type; // Type d'événement
	private Map<String, Object> data; // Données dynamiques

	private List<ChatEventDTO>  lstEventDTO; //Liste utilisé dans EventEnum.MESSAGE_LIST

	/*--------------------------*/
	/*     Constructeurs        */
	/*--------------------------*/
	public ChatEventDTO() 
	{
		this.data = new HashMap<>();
	}

	public ChatEventDTO( EventEnum type ) 
	{
		this();
		this.type = type;
	}

	public ChatEventDTO( EventEnum type, List<ChatEventDTO>  lstEventDTO ) 
	{
		this.lstEventDTO = lstEventDTO;
		this.type = type;
	}

	/*--------------------------*/
	/*   Getters et Setters     */
	/*--------------------------*/
	public EventEnum getType ()               { return type;      }
	public void      setType (EventEnum type) { this.type = type; }

	public Map<String, Object> getData ()                         { return data;      }
	public void                setData (Map<String, Object> data) { this.data = data; }

	public List<ChatEventDTO> getLstMes () { return this.lstEventDTO; }

	public Object getDataIndex ( int index )
	{
		if ( index >= this.getType().getRequiredKeys().size() ) return null;

		Object object = this.getData().get( this.getType().getRequiredKeys().get( index ) );
		return object;
	}


	/*--------------------------*/
	/*   Ajouter une donnée     */
	/*--------------------------*/
	public ChatEventDTO add(String key, Object value) 
	{
		this.data.put(key, value);
		return this;
	}

	/*--------------------------*/
	/*   Transformer Json       */
	/*--------------------------*/
	public static ChatEventDTO jsonToEventDTO(String json) 
	{
		Gson gson = new Gson();
		return gson.fromJson(json, ChatEventDTO.class);
	}

	public static ChatEventDTO jsonToLstEventDTO(String json) 
	{
		Gson gson = new Gson();
		ChatEventDTO mainEvent = gson.fromJson(json, ChatEventDTO.class);
		
		// Créer un nouveau ChatEventDTO avec le constructeur qui prend type et lstEventDTO
		return new ChatEventDTO(mainEvent.getType(), mainEvent.getLstEventDTO());
	}

	public String toJson() 
	{
		Gson gson = new Gson();
		return gson.toJson(this);
	}

	/*--------------------------*/
	/*   toString               */
	/*--------------------------*/
	@Override
	public String toString() 
	{ 
		return "ChatEventDTO{type='" + type + "', data=" + data + "}"; 
	}

	public List<ChatEventDTO> getLstEventDTO() {
		return lstEventDTO;
	}

	public void setLstEventDTO(List<ChatEventDTO> lstEventDTO) {
		this.lstEventDTO = lstEventDTO;
	}
}