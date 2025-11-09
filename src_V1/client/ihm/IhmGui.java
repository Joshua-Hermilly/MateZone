package client.ihm;

import client.Controleur;
import client.ihm.frame.connexion.ConnectionFrame;
import client.ihm.frame.affichage.HomeFrame;

public class IhmGui 
{
	/*--------------------------*/
	/*     Attributs            */
	/*--------------------------*/
	private Controleur controleur;

	private ConnectionFrame frameConnexion;
	private HomeFrame       frameHome;

	/*--------------------------*/
	/*     Constructeur         */
	/*--------------------------*/
	public IhmGui( Controleur controleur ) 
	{
		this.controleur      = controleur;

		this.frameConnexion  = null;
		this.frameHome       = null;
	}

	/*--------------------------*/
	/*  Méthodes Affichage      */
	/*--------------------------*/
	public void afficherFrameConnexionServeur() 
	{
		this.frameConnexion  = new ConnectionFrame( this );
	}

	public void afficherHome( String pseudo ) 
	{
		this.frameConnexion.dispose();
		this.frameHome = new HomeFrame( this, pseudo );
	}

	public void afficherLstMessages( String lstMessages ) 
	{
		if ( this.frameHome != null ) 
		{
			this.frameHome.mettreAJourLstMessages( lstMessages );
		}
	}

	public void afficherNvMessage( String message ) 
	{
		if ( this.frameHome != null ) 
		{
			this.frameHome.ajouterNvMessage( message );
		}
	}

	/*--------------------------*/
	/*  Méthodes Contrôleur     */
	/*--------------------------*/
	public boolean testerConnexionAuServeur( String host, int port )  { return this.controleur.testerConnexionAuServeur(host, port); }

	public void connexionAuClient ( String nom, String mdp )  { this.controleur.connexionAuClient(nom, mdp); }
	public void enregistrerClient ( String nom, String mdp )  { this.controleur.enregistrerClient(nom, mdp); }

	public void envoyerMessage( String message )  { this.controleur.envoyerMessage( message ); }

}
