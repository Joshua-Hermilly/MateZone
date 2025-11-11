package server.metier.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.java_websocket.WebSocket;

import common.dto.ChatEventDTO;
import common.protocol.EventEnum;
import server.bd.repository.UtilisateurRepository;
import server.metier.interfaces.IMessageRepository;
import server.metier.interfaces.IUtilisateurRepository;
import server.metier.model.Client;



public class ClientService
{	

	private final IUtilisateurRepository iUserRep;
	private final IMessageRepository     iMesRep;

	public ClientService(IUtilisateurRepository  iUserRepo ,IMessageRepository iMesRepo )
	{
		this.iUserRep = iUserRepo;
		this.iMesRep  = iMesRepo; 
	}

	/*-------------------------------*/
	/* GESTION CLIENT                */
	/*-------------------------------*/

	/**
	 * Gère la connexion d'un client
	 * Format attendu: "LOGIN:pseudo:mdp"
	 */
	public void handleLogin(WebSocket client, ChatEventDTO eventRec)
	{
		ChatEventDTO event;

		String pseudo = (String) eventRec.getDataIndex(0);
		String mdp    = (String) eventRec.getDataIndex(1);

		int idClient  = this.iUserRep.authenticate(pseudo, mdp);

		if ( idClient == -1 )
		{
			String erreur = "LOGIN: Client introuvable:" + pseudo+":" + mdp;
			event = new ChatEventDTO(EventEnum.ERROR).add(EventEnum.ERROR.getKeyIndex(0), erreur );
			client.send(event.toJson());

		}
		else
		{
			event = new ChatEventDTO(EventEnum.SUCCESS_LOGIN)
			        .add(EventEnum.SUCCESS_LOGIN.getKeyIndex(0), idClient)
			        .add(EventEnum.SUCCESS_LOGIN.getKeyIndex(1), pseudo  );
			
			client.send(event.toJson());
			this.envoyerMessageList(1, client);
		}

		System.out.println(event);
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


	private void envoyerMessageList(int IdGroupe, WebSocket client)
	{
		List<ChatEventDTO>  lstEventDTO = new ArrayList<ChatEventDTO>();

		Map<Integer,String[]> mapMessages = this.iMesRep.getMessages( IdGroupe );

		for ( Integer key : mapMessages.keySet() ) 
		{
			lstEventDTO.add( new ChatEventDTO(EventEnum.MESSAGE)
			    .add(EventEnum.MESSAGE.getKeyIndex(0), mapMessages.get(key)[0])
			    .add(EventEnum.MESSAGE.getKeyIndex(1), mapMessages.get(key)[1])
			    .add(EventEnum.MESSAGE.getKeyIndex(2), mapMessages.get(key)[2]));
		}

		ChatEventDTO event =  new ChatEventDTO(EventEnum.MESSAGE_LIST, lstEventDTO);
		System.out.println(event);

		client.send(event.toJson());
	}

}