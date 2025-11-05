package client;

import client.ihm.IhmGui;
import client.metier.Metier;

/*-------------------------------*/
/* Classe Controleur            */
/*-------------------------------*/
/**
 * Classe principale de contrôle de l'application MateZone côté Client.
 * Gère la logique de connexion et le lancement des différentes interfaces utilisateur.
 */
public class Controleur
{
	/*--------------------------*/
	/*        Attributs         */
	/*--------------------------*/
	private IhmGui ihmGui;
	private Metier metier;

	/*--------------------------*/
	/*     Constructeur         */
	/*--------------------------*/
	public Controleur() 
	{
		this.ihmGui = new IhmGui( this );
		this.metier = new Metier();

		// Initialiser la connexion
		this.lancerFrameConnexionServeur();
	}

	/*--------------------------*/
	/*   Getters et Setters     */
	/*--------------------------*/
	public void setHostPort( String host, int port ) 
	{
		Metier.setHost(host);
		Metier.setPort(port);
	}


	/*--------------------------*/
	/*     Client               */
	/*--------------------------*/

	/*--------------------------*/
	/*     Serveur              */
	/*--------------------------*/
	private void lancerFrameConnexionServeur()
	{
		this.ihmGui.afficherFrameConnexionServeur();
	}

	public boolean testerConnexionAuServeur( String host, int port ) 
	{
		return this.metier.testerConnexionAuServeur(host, port);
	}

	
	/*--------------------------*/
	/*     MAIN                 */
	/*--------------------------*/
	public static void main(String[] args)
	{
		Controleur controleur = new Controleur();
	}
}
