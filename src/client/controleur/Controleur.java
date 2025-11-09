package client.controleur;

import client.ihm.IhmGui;
import client.infrastructure.websocket.WebSocketChatAdapter;
import client.metier.Metier;
import client.metier.ports.IChatPort;

/*-------------------------------*/
/* Class Controleur              */
/*-------------------------------*/
/**
 * Classe principale de contrôle de l'application MateZone côté Client.
 * Gère la logique de connexion et le lancement des différentes interfaces utilisateur.
 *
 * @author   Joshua Hermilly
 * @version  V1.1
 * @date     08/11/25
 */
public class Controleur 
{
	/*--------------------------*/
	/* Attributs                */
	/*--------------------------*/
	private static final String ADRESSE_SERVEUR = "ws://localhost:8080";

	private IhmGui ihmGui;
	private Metier metier;

	/*--------------------------*/
	/* Constructeur             */
	/*--------------------------*/
	public Controleur() throws Exception 
	{
		this.ihmGui = new IhmGui(this);

		IChatPort chatPort = new WebSocketChatAdapter( Controleur.ADRESSE_SERVEUR );
		chatPort.connecter();
		
		this.metier = new Metier(chatPort);
	}

	/*-----------------------------------*/
	/*               IHM                 */
	/*-----------------------------------*/
	/*--------------------------*/
	/* Affichage                */
	/*--------------------------*/
	public void lancerApp()
	{
		this.ihmGui.lancerApp();
	}

	/*-----------------------------------*/
	/*              METIER               */
	/*-----------------------------------*/
	/*--------------------------*/
	/* Client                   */
	/*--------------------------*/
	// public void tenterConnexion(String host, int port) 
	// {
	// 	this.metier.connecterAuServeur(host, port);
	// }

	public void tenterConnexionClient(String pseudo, String mdp) 
	{
		//Format valide ?
		if ( !this.validerIdentifiants(pseudo, mdp) ) return;

		this.metier.connecterAuClient(pseudo, mdp);
	}

	public void tenterEnregistrement(String pseudo, String mdp) 
	{
		//Format valide ?
		if ( !this.validerIdentifiants(pseudo, mdp) ) return;

		this.metier.enregistrerUtilisateur(pseudo, mdp);
	}



	/*--------------------------*/
	/* Serveur                  */
	/*--------------------------*/
	
	/*---------------------------*/
	/* Notifications (Interface) */
	/*---------------------------*/


	/*---------------------------*/
	/* Validations               */
	/*---------------------------*/
	private boolean validerIdentifiants(String pseudo, String mdp)
	{
		//pseudo valide ?
		if ( pseudo.isEmpty() || pseudo.length() < 3 ) 
		{
			this.ihmGui.afficherErreur("Le pseudo doit contenir au moins 3 caractères.");
			return false;
		}

		//mdp valide ?
		if ( mdp.isEmpty() || mdp.length() < 6 ) 
		{
			this.ihmGui.afficherErreur("Le mot de passe doit contenir au moins 6 caractères.");
			return false;
		}

		return true;
	}

}
