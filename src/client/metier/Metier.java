package client.metier;

import client.metier.ports.IChatPort;
import common.dto.ChatEventDTO;
import common.protocol.EventEnum;

/*-------------------------------*/
/* Classe Metier                 */
/*-------------------------------*/
/**
 * Classe Metier - Gère la logique métier de l'application de chat
 * Fait le lien entre l'ihm et ports.
 * La passerelle est vers le serveur est fait grâce à client/metier/ports/IChatPort.
 */
public class Metier 
{
	/*--------------------------*/
	/* Attributs                */
	/*--------------------------*/
	private final IChatPort chatPort;

	/*--------------------------*/
	/* Constructeur             */
	/*--------------------------*/
	public Metier( IChatPort chatPort )
	{
		this.chatPort = chatPort;
	}

	/*---------------------------*/
	/* Getters                   */
	/*---------------------------*/
	public IChatPort getChatPort()  { return chatPort; }


	/*-----------------------------------*/
	/*              METIER               */
	/*-----------------------------------*/
	/*--------------------------*/
	/* Client                   */
	/*--------------------------*/

	/*-----------------------*/
	/* Connexion/Inscription */
	/*-----------------------*/
	// public void connecterAuServeur(String host, int port) 
	// {

	// }

	public void connecterAuClient( String pseudo, String mdp ) 
	{
		//Création du message eventDTO
		ChatEventDTO event = new ChatEventDTO( EventEnum.LOGIN )
				.add( "pseudo", pseudo )
				.add( "mdp"   , mdp    );

		this.chatPort.envoyer( event );
	}

	public void enregistrerUtilisateur( String pseudo, String mdp ) 
	{
		//Création du message eventDTO
		ChatEventDTO event = new ChatEventDTO( EventEnum.SIGNUP )
				.add( "pseudo", pseudo )
				.add( "mdp"   , mdp    );

		this.chatPort.envoyer (event );
	}

	/*-----------------------*/
	/*    Envoyer Message    */
	/*-----------------------*/

	public void envoyerMessage(String texte) 
	{
		//Création du message eventDTO
		ChatEventDTO event = new ChatEventDTO( EventEnum.MESSAGE )
				.add( "message", texte );
		
		this.chatPort.envoyer( event );
	}
}
