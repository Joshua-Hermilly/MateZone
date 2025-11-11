package server.metier.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.print.DocFlavor.STRING;

import org.java_websocket.WebSocket;

import common.dto.ChatEventDTO;
import common.protocol.EventEnum;
import server.metier.interfaces.IMessageRepository;
import server.metier.interfaces.IUtilisateurRepository;
import server.metier.interfaces.IWebSocketMateZone;
import server.metier.model.Client;



public class ClientService
{	

	private final IUtilisateurRepository iUserRep;
	private final IMessageRepository     iMesRep;
	private       IWebSocketMateZone     iWebSocketMateZone;

	public ClientService(IUtilisateurRepository  iUserRepo, IMessageRepository iMesRepo)
	{
		this.iUserRep            = iUserRepo;
		this.iMesRep             = iMesRepo; 
		this.iWebSocketMateZone  = null; 
	}


	public void setIWebSocketMateZone(IWebSocketMateZone iWebSocketMateZone)
	{
		this.iWebSocketMateZone = iWebSocketMateZone;
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
	public void handleRegister(WebSocket client, ChatEventDTO eventRec)
	{
		ChatEventDTO event;

		String pseudo = (String) eventRec.getDataIndex(0);
		String mdp    = (String) eventRec.getDataIndex(1);

		if (!pseudo.isEmpty() && !mdp.isEmpty())
		{
			int idClient = this.iUserRep.createClient(new Client(pseudo,mdp));

			if (idClient != -1)
			{
				event = new ChatEventDTO(EventEnum.SUCCESS_SIGNUP)
				    .add(EventEnum.SUCCESS_LOGIN.getKeyIndex(0), idClient)
				    .add(EventEnum.SUCCESS_LOGIN.getKeyIndex(1), pseudo  );

				client.send(event.toJson());
				return;
			}
		}
		
		// Gestion de l'erreur (champs vides ou échec de création)
		String erreur = "REGISTER: Erreur de creation de l'utilisateur:" + pseudo + ":" + mdp;
		event = new ChatEventDTO(EventEnum.ERROR).add(EventEnum.ERROR.getKeyIndex(0), erreur );
		client.send(event.toJson());

	}

	/**
	 * Gère l'insertion d'un nouveau message
	 * Format attendu: "NEWMESSAGE:idClient:idChannel:leMessage"
	 */
	public void handleNewMessage(WebSocket client, ChatEventDTO eventRec)
	{

		System.out.println(eventRec);
		int    idClient  = ((Double) eventRec.getDataIndex(0)).intValue();
		int    idChannel = ((Double) eventRec.getDataIndex(1)).intValue();
		String nMessage  = (String) eventRec.getDataIndex(2);

		int idMessage = this.iMesRep.sendMessage(idChannel, idClient, nMessage );

		if (idMessage != -1 )
		{
			String[] tabmMessage=  this.iMesRep.getMessage(idMessage);

				ChatEventDTO event = new ChatEventDTO(EventEnum.MESSAGE)
			    .add(EventEnum.MESSAGE.getKeyIndex(0), tabmMessage[0])
			    .add(EventEnum.MESSAGE.getKeyIndex(1), tabmMessage[1])
			    .add(EventEnum.MESSAGE.getKeyIndex(2), tabmMessage[2])
			    .add(EventEnum.MESSAGE.getKeyIndex(3), tabmMessage[3]);


			this.iWebSocketMateZone.broadcast(idChannel, event);
		}
	}


	/**
	 * Savoir ou est l'utilisateur
	 * Fo"
	 */
	public void handleNewChannel(WebSocket client, ChatEventDTO eventRec)
	{
		int    idChannel  = (int)    eventRec.getDataIndex(0);

		this.iWebSocketMateZone.setClientChannel(client,idChannel);
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
			    .add(EventEnum.MESSAGE.getKeyIndex(2), mapMessages.get(key)[2])
			    .add(EventEnum.MESSAGE.getKeyIndex(3), mapMessages.get(key)[3]));
		}

		ChatEventDTO event =  new ChatEventDTO(EventEnum.MESSAGE_LIST, lstEventDTO);
		System.out.println(event);

		client.send(event.toJson());
	}

}