package server.metier.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.java_websocket.WebSocket;

import common.dto.ChatEventDTO;
import common.protocol.EventEnum;
import server.metier.interfaces.IMessageRepository;
import server.metier.interfaces.IUtilisateurRepository;
import server.metier.interfaces.IWebSocketMateZone;
import server.metier.model.Client;

/**
 * Service métier gérant les opérations liées aux clients de l'application
 * MateZone.
 * Cette classe encapsule la logique business pour l'authentification,
 * l'inscription,
 * l'envoi de messages et la gestion des canaux de chat.
 * Elle fait le lien entre la couche présentation (WebSocket) et la couche
 * données (repositories).
 *
 * @author Prévost Donovan
 * @version V1
 * @date 08/11/25
 */
public class ClientService 
{
	/*-------------------------------*/
	/* Attributs                     */
	/*-------------------------------*/

	/**
	 * Repository pour les opérations liées aux utilisateurs.
	 * Gère l'authentification, la création et la récupération des clients.
	 */
	private final IUtilisateurRepository iUserRep;

	/**
	 * Repository pour les opérations liées aux messages.
	 * Gère l'envoi, la récupération et le stockage des messages de chat.
	 */
	private final IMessageRepository iMesRep;

	/**
	 * Interface WebSocket pour la communication temps réel avec les clients.
	 * Permet le broadcast et la gestion des connexions WebSocket.
	 */
	private IWebSocketMateZone iWebSocketMateZone;

	/*-------------------------------*/
	/* Constructeur                  */
	/*-------------------------------*/

	/**
	 * Constructeur du service client.
	 * Initialise les repositories nécessaires pour les opérations de données.
	 * L'interface WebSocket sera configurée ultérieurement via
	 * setIWebSocketMateZone.
	 * 
	 * @param iUserRepo repository pour les opérations utilisateur
	 * @param iMesRepo  repository pour les opérations de messages
	 */
	public ClientService(IUtilisateurRepository iUserRepo, IMessageRepository iMesRepo)
	{
		this.iUserRep           = iUserRepo;
		this.iMesRep            = iMesRepo;
		this.iWebSocketMateZone = null;
	}

	/**
	 * Configure l'interface WebSocket pour ce service.
	 * Nécessaire pour permettre la communication temps réel avec les clients
	 * connectés.
	 * 
	 * @param iWebSocketMateZone l'interface WebSocket à utiliser pour les
	 *                           communications
	 */
	public void setIWebSocketMateZone(IWebSocketMateZone iWebSocketMateZone)
	{
		this.iWebSocketMateZone = iWebSocketMateZone;
	}

	/*-------------------------------*/
	/* GESTION CLIENT                */
	/*-------------------------------*/

	/**
	 * Gère la tentative de connexion d'un client.
	 * Vérifie les identifiants via le repository utilisateur et renvoie
	 * soit un succès avec l'ID du client, soit une erreur d'authentification.
	 * En cas de succès, envoie également la liste des messages du canal par défaut.
	 * 
	 * @param client   la connexion WebSocket du client
	 * @param eventRec l'événement de connexion reçu contenant pseudo et mot de
	 *                 passe
	 */
	public void handleLogin(WebSocket client, ChatEventDTO eventRec) 
	{
		ChatEventDTO event;

		String pseudo = (String) eventRec.getDataIndex(0);
		String mdp    = (String) eventRec.getDataIndex(1);

		int idClient = this.iUserRep.authenticate(pseudo, mdp);

		if (idClient == -1)
		{
			String erreur = "LOGIN: Client introuvable:" + pseudo + ":" + mdp;
			event = new ChatEventDTO(EventEnum.ERROR).add(EventEnum.ERROR.getKeyIndex(0), erreur);
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
	}

	/*
	 * /**
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
			int idClient = this.iUserRep.createClient(new Client(pseudo, mdp));

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
		event         = new ChatEventDTO(EventEnum.ERROR).add(EventEnum.ERROR.getKeyIndex(0), erreur);
		client.send(event.toJson());

	}

	/**
	 * Gère l'insertion d'un nouveau message
	 * Format attendu: "NEWMESSAGE:idClient:idChannel:leMessage"
	 */
	public void handleNewMessage(WebSocket client, ChatEventDTO eventRec) 
	{

		System.out.println(eventRec);
		int   idClient  = ((Double) eventRec.getDataIndex(0)).intValue();
		int   idChannel = ((Double) eventRec.getDataIndex(1)).intValue();
		String nMessage = (String)  eventRec.getDataIndex(2);

		int idMessage = this.iMesRep.sendMessage(idChannel, idClient, nMessage);

		if (idMessage != -1) 
		{
			String[] tabmMessage = this.iMesRep.getMessage(idMessage);

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
		int idChannel = (int) eventRec.getDataIndex(0);

		this.iWebSocketMateZone.setClientChannel(client, idChannel);
	}

	private void envoyerMessageList(int IdGroupe, WebSocket client)
	 {
		List<ChatEventDTO> lstEventDTO = new ArrayList<ChatEventDTO>();

		Map<Integer, String[]> mapMessages = this.iMesRep.getMessages(IdGroupe);

		for (Integer key : mapMessages.keySet()) 
		{
			lstEventDTO.add(new ChatEventDTO(EventEnum.MESSAGE)
					.add(EventEnum.MESSAGE.getKeyIndex(0), mapMessages.get(key)[0])
					.add(EventEnum.MESSAGE.getKeyIndex(1), mapMessages.get(key)[1])
					.add(EventEnum.MESSAGE.getKeyIndex(2), mapMessages.get(key)[2])
					.add(EventEnum.MESSAGE.getKeyIndex(3), mapMessages.get(key)[3]));
		}

		ChatEventDTO event = new ChatEventDTO(EventEnum.MESSAGE_LIST, lstEventDTO);
		System.out.println(event);

		client.send(event.toJson());
	}

}