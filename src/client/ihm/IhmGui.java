package client.ihm;

import client.Controleur;
import client.ihm.frame.connexion.ConnectionFrame;

public class IhmGui 
{
	/*--------------------------*/
	/*     Attributs            */
	/*--------------------------*/
	private Controleur controleur;


	/*--------------------------*/
	/*     Constructeur         */
	/*--------------------------*/
	public IhmGui( Controleur controleur ) 
	{
		this.controleur = controleur;
	}

	/*--------------------------*/
	/*  Méthodes Affichage      */
	/*--------------------------*/

	public void afficherFrameConnexionServeur() 
	{
		new ConnectionFrame( this );
	}

	public void afficherFrameConnexionClient() 
	{
	}


	/*--------------------------*/
	/*  Méthodes Contrôleur     */
	/*--------------------------*/
	public boolean testerConnexionAuServeur( String host, int port ) 
	{
		return this.controleur.testerConnexionAuServeur(host, port);
	}

	public void connexionAuClient( String nom, String mdp ) 
	{
		this.controleur.connexionAuClient(nom, mdp);
	}

	public void enregistrerClient( String nom, String mdp ) 
	{
		this.controleur.enregistrerClient(nom, mdp);
	}

}
