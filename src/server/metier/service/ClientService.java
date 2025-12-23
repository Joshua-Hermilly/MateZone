package server.metier.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.java_websocket.WebSocket;

import common.dto.ChatEventDTO;
import common.protocol.EventEnum;
import server.metier.interfaces.*;
import server.metier.model.Client;
import server.metier.util.InputValidator;

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
import java.util.HashMap;
import java.time.Instant;

public class ClientService 
{
	/*-------------------------------*/
	/* Anti-brute-force              */
	/*-------------------------------*/

	private static final HashMap<String, Tentatives> tentativesParIp = new HashMap<>();
	private static final int NB_MAX_TENTATIVES_CONNEXION  = 3;
	private static final int NB_MAX_TENTATIVES_MESSAGE    = 5;
	private static final long TEMPS_BLOQUAGE_MS = 60_000; // 1 minute

	private static class Tentatives 
	{
		int nb;
		long dernierEssai;
		long bloqueJusqua;

		Tentatives() 
		{ 
			nb           = 0;
			dernierEssai = 0;
			bloqueJusqua = 0; 
		}
	}

	private boolean estBloque(String ip) 
	{
		Tentatives t = ClientService.tentativesParIp.get(ip);

		if (t == null) return false;

		long maintenant = Instant.now().toEpochMilli();

		return t.bloqueJusqua > maintenant;
	}

	private void enregistrerTentative(String ip) 
	{
		long maintenant = Instant.now().toEpochMilli();
		Tentatives t    = ClientService.tentativesParIp.getOrDefault(ip, new Tentatives());

		if (maintenant - t.dernierEssai > ClientService.TEMPS_BLOQUAGE_MS) {t.nb = 1;}
		else {t.nb++;}

		t.dernierEssai = maintenant;

		if (t.nb >= ClientService.NB_MAX_TENTATIVES_CONNEXION) {t.bloqueJusqua = maintenant + ClientService.TEMPS_BLOQUAGE_MS;}

		ClientService.tentativesParIp.put(ip, t);
	}

		private void messageEnvoyer(String ip) 
	{
		long maintenant = Instant.now().toEpochMilli();
		Tentatives t    = ClientService.tentativesParIp.getOrDefault(ip, new Tentatives());

		if (maintenant - t.dernierEssai > ClientService.TEMPS_BLOQUAGE_MS) {t.nb = 1;}
		else {t.nb++;}

		t.dernierEssai = maintenant;

		if (t.nb >= ClientService.NB_MAX_TENTATIVES_MESSAGE) {t.bloqueJusqua = maintenant + ClientService.TEMPS_BLOQUAGE_MS;}

		ClientService.tentativesParIp.put(ip, t);
	}

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
	 * Repository pour les opérations liées aux messages.
	 * Gère la creation 
	 */
	private final IChannelRepository iChannelRep;

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
	public ClientService(IUtilisateurRepository iUserRepo, IMessageRepository iMesRepo, IChannelRepository iChanRepo)
	{
		this.iUserRep           = iUserRepo;
		this.iMesRep            = iMesRepo;
		this.iChannelRep        = iChanRepo;
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
		if (estBloque(client.getRemoteSocketAddress().toString())) 
		{
			ChatEventDTO event = new ChatEventDTO(EventEnum.ERROR).add(EventEnum.ERROR.getKeyIndex(0), "Trop de tentatives. Merci de patienter 1 minute avant de réessayer.");
			client.send(event.toJson());
			return;
		}

		this.enregistrerTentative(client.getRemoteSocketAddress().toString());

		ChatEventDTO event;

		String pseudo = (String) eventRec.getDataIndex(0);
		String mdp    = (String) eventRec.getDataIndex(1);


		// Validation du pseudo
		if (!InputValidator.isValidPseudo(pseudo)) 
		{
			String erreur = "Pseudo invalide. Seuls les caractères alphanumériques et _ sont autorisés (3-20 caractères).";
			event = new ChatEventDTO(EventEnum.ERROR).add(EventEnum.ERROR.getKeyIndex(0), erreur);
			client.send(event.toJson());
			return;
		}

		int idClient = this.iUserRep.authenticate(pseudo, mdp);

		if (idClient == -1)
		{
			String erreur = "Échec de connexion : Identifiants incorrects pour l'utilisateur '" + pseudo + "'.";
			event = new ChatEventDTO(EventEnum.ERROR).add(EventEnum.ERROR.getKeyIndex(0), erreur);
			client.send(event.toJson());

		} 
		else 
		{
			ClientService.tentativesParIp.remove(client.getRemoteSocketAddress().toString());

			event = new ChatEventDTO(EventEnum.SUCCESS_LOGIN)
					.add(EventEnum.SUCCESS_LOGIN.getKeyIndex(0), idClient)
					.add(EventEnum.SUCCESS_LOGIN.getKeyIndex(1), InputValidator.escapeHtml(pseudo));

			this.iWebSocketMateZone.registerClientSocket(idClient, client);
			client.send(event.toJson());
			this.envoyerPermChannel(idClient, client);
			this.handleSwitchChannel(client, new ChatEventDTO(EventEnum.CHANGER_CHANNEL).add(EventEnum.CHANGER_CHANNEL.getKeyIndex(0), 1));
		}
	}

	/*
	 * /**
	 * Gère l'inscription d'un nouveau client
	 * Format attendu: "REGISTER:pseudo:mdp"
	 */
	public void handleRegister(WebSocket client, ChatEventDTO eventRec) 
	{
		if (estBloque(client.getRemoteSocketAddress().toString())) 
		{
			ChatEventDTO event = new ChatEventDTO(EventEnum.ERROR).add(EventEnum.ERROR.getKeyIndex(0), "Trop de tentatives. Merci de patienter 1 minute avant de réessayer.");
			client.send(event.toJson());
			return;
		}

		this.enregistrerTentative(client.getRemoteSocketAddress().toString());

		ChatEventDTO event;

		String pseudo = (String) eventRec.getDataIndex(0);
		String mdp    = (String) eventRec.getDataIndex(1);

		// Validation du pseudo
		if (!InputValidator.isValidPseudo(pseudo)) 
		{
			String erreur = "Pseudo invalide. Seuls les caractères alphanumériques et _ sont autorisés (3-20 caractères).";
			event = new ChatEventDTO(EventEnum.ERROR).add(EventEnum.ERROR.getKeyIndex(0), erreur);
			client.send(event.toJson());
			return;
		}

		// Validation du mot de passe fort
		if (!InputValidator.isStrongPassword(mdp)) 
		{
			String erreur = "Mot de passe trop faible. 8 caractères min, 1 majuscule, 1 minuscule, 1 chiffre, 1 spécial.";
			event = new ChatEventDTO(EventEnum.ERROR).add(EventEnum.ERROR.getKeyIndex(0), erreur);
			client.send(event.toJson());
			return;
		}

		int idClient = this.iUserRep.createClient(new Client(pseudo, mdp));

		if (idClient != -1) 
		{
			this.iChannelRep.ajouterChannelCLient(idClient, 1);
			event = new ChatEventDTO(EventEnum.SUCCESS_SIGNUP)
				.add(EventEnum.SUCCESS_LOGIN.getKeyIndex(0), idClient)
				.add(EventEnum.SUCCESS_LOGIN.getKeyIndex(1), InputValidator.escapeHtml(pseudo));

			client.send(event.toJson());
			return;
		}

		// Gestion de l'erreur (champs vides ou échec de création)
		String erreur = "Échec de l'inscription : impossible de créer l'utilisateur '" + pseudo + "'. Veuillez vérifier les informations fournies.";
		event         = new ChatEventDTO(EventEnum.ERROR).add(EventEnum.ERROR.getKeyIndex(0), erreur);
		client.send(event.toJson());

	}

	/**
	 * Gère l'envoye des accees au channel  pour un utilisateur done
	 * Format attendu:"
	 */
	public void envoyerPermChannel(int idClient , WebSocket clientSocket)
	{
		HashMap<Integer, String> permCli;
		List<ChatEventDTO>       lstEventDTO = new ArrayList<ChatEventDTO>();
		ChatEventDTO event;

		permCli = iUserRep.permChannel(idClient);


		for (Integer key : permCli.keySet()) 
		{
		 lstEventDTO.add(new ChatEventDTO(EventEnum.PERM_CHANNEL)
						.add(EventEnum.PERM_CHANNEL.getKeyIndex(0), key              )
						.add(EventEnum.PERM_CHANNEL.getKeyIndex(1), permCli.get(key) )
						);
		}

		event = new ChatEventDTO(EventEnum.PERMS_CHANNELS, lstEventDTO);

		clientSocket.send(event.toJson());
	}

	/**
	 * Gère l'insertion d'un nouveau message
	 * Format attendu: "NEWMESSAGE:idClient:idChannel:leMessage"
	 */
	public void handleNewMessage(WebSocket client, ChatEventDTO eventRec) 
	{
		if (estBloque(client.getRemoteSocketAddress().toString())) 
		{
			ChatEventDTO event = new ChatEventDTO(EventEnum.ERROR)
				.add(EventEnum.ERROR.getKeyIndex(0), "Trop de messages envoyés. Merci de patienter avant de réessayer.");
			client.send(event.toJson());
			return;
		}

		this.messageEnvoyer(client.getRemoteSocketAddress().toString());

		int   idClient  = ((Double) eventRec.getDataIndex(0)).intValue();
		int   idChannel = ((Double) eventRec.getDataIndex(1)).intValue();
		String nMessage = (String)  eventRec.getDataIndex(2);

		// Validation du message
		if (!InputValidator.isValidMessage(nMessage)) 
		{
			ChatEventDTO event = new ChatEventDTO(EventEnum.ERROR)
				.add(EventEnum.ERROR.getKeyIndex(0), "Message invalide ([1;500]).");
			client.send(event.toJson());
			return;
		}
		// Sanitize le message avant stockage et diffusion
		String safeMessage = InputValidator.escapeHtml(nMessage);

		int idMessage = this.iMesRep.sendMessage(idChannel, idClient, safeMessage);

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
	public void handleSwitchChannel(WebSocket client, ChatEventDTO eventRec) 
	{
		try {
				int idChannel = (int) Double.parseDouble(eventRec.getDataIndex(0).toString());
				this.iWebSocketMateZone.setClientChannel(client, idChannel);
				this.envoyerMessageList(idChannel, client);
		} catch (Exception e) { e.printStackTrace();}
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

		client.send(event.toJson());
	}


	public void handleNewChannel(WebSocket client, ChatEventDTO eventRec)
	{
		try {
				int idClient1 = (int)Integer.parseInt(eventRec.getDataIndex(0).toString());
				int idCLient2 = (int)Integer.parseInt(eventRec.getDataIndex(1).toString());

				String nomClient1 = this.iUserRep.getClientById(idClient1).getPseudo();
				String nomClient2 = this.iUserRep.getClientById(idCLient2).getPseudo();

				if (this.iChannelRep.CreerMp(idClient1, idCLient2, "prive_" + nomClient1 + "_" + nomClient2 ))
				{
					this.envoyerPermChannel(idClient1, client);
					this.envoyerPermChannel(idCLient2, this.iWebSocketMateZone.getWebSocketByClientId(idCLient2));
				}
		} catch (Exception e) { e.printStackTrace();}
	}

}