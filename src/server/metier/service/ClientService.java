package server.metier.service;

import java.util.HashMap;
import java.util.Map;

import org.java_websocket.WebSocket;

import common.dto.ChatEventDTO;
import common.protocol.EventEnum;
import server.bd.repository.UtilisateurRepository;
import server.metier.interfaces.IUtilisateurRepository;
import server.metier.model.Client;



public class ClientService
{	

	private final IUtilisateurRepository iUserRep;

	public ClientService(IUtilisateurRepository  iUserRepo)
	{
		this.iUserRep = iUserRepo;
	}

	/*-------------------------------*/
	/* GESTION CLIENT                */
	/*-------------------------------*/

	/**
	 * Gère la connexion d'un client
	 * Format attendu: "LOGIN:pseudo:mdp"
	 */
	public void handleLogin(WebSocket client, Map<String, Object> data)
	{
		ChatEventDTO event;
		
		String pseudo = (String) data.get(EventEnum.LOGIN.getRequiredKeys().get(0));
		String mdp    = (String) data.get(EventEnum.LOGIN.getRequiredKeys().get(1));

		int idClient = this.iUserRep.authenticate(pseudo, mdp);

		if ( idClient == -1 )
		{
			String erreur = "LOGIN: Client introuvable:"+pseudo+":"+mdp;
			event = new ChatEventDTO(EventEnum.ERROR).add("message", erreur );
		}
		else
		{
			event = new ChatEventDTO(EventEnum.SUCCESS_LOGIN)
			        .add((String) (EventEnum.SUCCESS_LOGIN.getRequiredKeys().get(0)), idClient)
			        .add((String) (EventEnum.SUCCESS_LOGIN.getRequiredKeys().get(1)), pseudo  );
		}

		System.out.println(event);
		client.send(event.toJson());
			



		/*String[] parties = message.split(":");
		
		if (parties.length == 3) 
		{
			String pseudo = parties[1];
			String mdp = parties[2];
			
			int idClient = ServeurMateZone.bd.authenticate(pseudo, mdp);
			client.send("CONNECT:" + idClient + ":" + ServeurMateZone.bd.getClientById(idClient).getPseudo());
			
			// Convertir la HashMap en JSON avec Gson
			HashMap<Integer, String[]> messages = hsmessages(1);
			Map<String, Object> datasJson = gson.toJson(messages);
			client.send("MESSAGES_LIST:" + messagesJson);
		}*/
	}


/*
	/**
	 * Gère l'inscription d'un nouveau client
	 * Format attendu: "REGISTER:pseudo:mdp"
	 */
	public void handleRegister(WebSocket client, Map<String, Object> data)
	{
		/*String[] parties = message.split(":");
		
		if (parties.length == 3) 
		{
			String pseudo = parties[1];
			String mdp = parties[2];
			
			Client user = new Client(pseudo, mdp);
			client.send("REGISTERED:" + ServeurMateZone.bd.createClient(user));
		}*/
	}

	/**
	 * Gère l'envoi d'un nouveau message
	 * Format attendu: "NEWMESSAGE:idClient:idChannel:leMessage"
	 */
	public void handleNewMessage(WebSocket client, Map<String, Object> data)
	{
		/*String[] parties = message.split(":", 4); // Limite à 4 parties maximum
		
		if (parties.length == 4) 
		{
			int idClient = Integer.parseInt(parties[1]);
			int idChannel = Integer.parseInt(parties[2]);
			String nMessage = parties[3]; // Contient tout le reste, même avec des ":"
			
			if (ServeurMateZone.bd.sendMessage(idClient, idChannel, nMessage))
			{
				this.broadcast();
			}
		}*/
	}

}