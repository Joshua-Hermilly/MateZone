package common.dto;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import org.w3c.dom.events.Event;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

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

	/*--------------------------*/
	/*   Getters et Setters     */
	/*--------------------------*/
	public EventEnum getType ()               { return type;      }
	public void      setType (EventEnum type) { this.type = type; }

	public Map<String, Object> getData ()                         { return data;      }
	public void                setData (Map<String, Object> data) { this.data = data; }

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
		System.out.println("sdqsdqsd");
		return "ChatEventDTO{type='" + type + "', data=" + data + "}"; 
	}
}