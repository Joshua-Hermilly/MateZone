package client.metier;

import client.metier.interfaces.IEnvoyeur;
import client.metier.interfaces.INotifieur;
import common.dto.ChatEventDTO;
import common.protocol.EventEnum;

/*-------------------------------*/
/* Classe Metier                 */
/*-------------------------------*/
/**
 * Classe Metier - Gère la logique métier de l'application de chat
 * Fait le lien entre l'ihm et ports.
 * La passerelle est vers le serveur est fait grâce à client/metier/ports/IiEnvoyeur.
 */
public class Metier
{
	/*--------------------------*/
	/* Attributs                */
	/*--------------------------*/
	private final IEnvoyeur  iEnvoyeur;
	private final INotifieur iNotifieur;

	private int    idChannel;
	private int    idClient;
	private String pseudoClient;

	/*--------------------------*/
	/* Constructeur             */
	/*--------------------------*/
	public Metier( IEnvoyeur iEnvoyeur, INotifieur iNotifieur )
	{
		this.iEnvoyeur  = iEnvoyeur;
		this.iNotifieur = iNotifieur;
	}

	/*---------------------------*/
	/* Getters                   */
	/*---------------------------*/
	public IEnvoyeur getiEnvoyeur()  { return iEnvoyeur; }


	/*-----------------------------------*/
	/*              METIER               */
	/*-----------------------------------*/
	/*--------------------------*/
	/* Client                   */
	/*--------------------------*/
	public void setClient( int idClient, String pseudoClient )
	{
		this.idClient     = idClient;
		this.pseudoClient = pseudoClient;

		this.iNotifieur.succesLogin( this.pseudoClient );
	}

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
				.add( EventEnum.LOGIN.getKeyInedx(0), pseudo )
				.add( EventEnum.LOGIN.getKeyInedx(1), mdp    );
		this.iEnvoyeur.envoyer( event );
	}

	public void enregistrerUtilisateur( String pseudo, String mdp ) 
	{
		//Création du message eventDTO
		ChatEventDTO event = new ChatEventDTO( EventEnum.SIGNUP )
				.add( EventEnum.SIGNUP.getKeyInedx(0), pseudo )
				.add( EventEnum.SIGNUP.getKeyInedx(1), mdp    );
		this.iEnvoyeur.envoyer (event );
	}

	/*-----------------------*/
	/*    Envoyer Message    */
	/*-----------------------*/
	public void envoyerMessage(String texte) 
	{
		//Création du message eventDTO
		ChatEventDTO event = new ChatEventDTO( EventEnum.NEW_MESSAGE )
				.add( EventEnum.NEW_MESSAGE.getKeyInedx(0), this.idChannel )
				.add( EventEnum.NEW_MESSAGE.getKeyInedx(1), this.idClient  )
				.add( EventEnum.NEW_MESSAGE.getKeyInedx(2), texte          );
		
		this.iEnvoyeur.envoyer( event );
	}

	public void envoyerPieceJoint( byte[] bytes )
	{
		//Création du message eventDTO NEW_MESSAGE_IMG ( List.of( "IdGroupe" , "idCliet", "byte" ) ),
		ChatEventDTO event = new ChatEventDTO( EventEnum.NEW_MESSAGE_IMG )
				.add( EventEnum.NEW_MESSAGE.getKeyInedx(0), this.idChannel )
				.add( EventEnum.NEW_MESSAGE.getKeyInedx(1), this.idClient  )
				.add( EventEnum.NEW_MESSAGE.getKeyInedx(2), bytes          );
		
		this.iEnvoyeur.envoyer( event );
	}

	/*-----------------------------------*/
	/*          INotificateur            */
	/*-----------------------------------*/
	public void notifierMessage( ChatEventDTO event )
	{
		if ( event.getType().equals( EventEnum.SUCCESS_LOGIN ) || event.getType().equals( EventEnum.SUCCESS_SIGNUP ) ) 
		{ 
			int    id     = ( (Number) event.getDataIndex( 0 ) ).intValue();
			String pseudo =   (String) event.getDataIndex( 1 );
			System.out.println( "Metier : " + id     );
			System.out.println( "Metier : " + pseudo );

			this.setClient( id, pseudo );
			return;
		}

		if ( event.getType().equals( EventEnum.ERROR ) )
		{
			String erreur = (String) event.getDataIndex( 0 );

			this.iNotifieur.notifierErreur( erreur );
		}
	}

}
