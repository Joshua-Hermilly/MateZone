package client;

import client.ihm.IhmGui;
import client.metier.Metier;
import client.metier.interfaces.INotificateur;

/*-------------------------------*/
/* Classe Controleur            */
/*-------------------------------*/
/**
 * Classe principale de contrôle de l'application MateZone côté Client.
 * Gère la logique de connexion et le lancement des différentes interfaces
 * utilisateur.
 */
public class Controleur implements INotificateur 
{
	/*--------------------------*/
	/* Attributs                */
	/*--------------------------*/
	private IhmGui ihmGui;
	private Metier metier;

	/*--------------------------*/
	/* Constructeur             */
	/*--------------------------*/
	public Controleur() 
	{
		this.ihmGui = new IhmGui(this);
		this.metier = new Metier();

		// Initialiser la connexion
		this.lancerFrameConnexionServeur();
	}

	/*--------------------------*/
	/* Client                   */
	/*--------------------------*/
	public void connexionAuClient(String nom, String mdp)  { this.metier.connexionAuClient(nom, mdp); }
	public void enregistrerClient(String nom, String mdp)  { this.metier.creerClient      (nom, mdp); }

	/*--------------------------*/
	/* Serveur */
	/*--------------------------*/
	private void lancerFrameConnexionServeur() 
	{
		this.ihmGui.afficherFrameConnexionServeur();
	}

	public boolean testerConnexionAuServeur(String host, int port) 
	{
		boolean resultat = this.metier.testerConnexionAuServeur(host, port);

		// Si la connexion réussit, on s'enregistre comme notificateur
		if (resultat && Metier.getClient() != null) {
			Metier.getClient().setNotificateur(this);
		}

		return resultat;
	}

	/*---------------------------*/
	/* Notifications (Interface) */
	/*---------------------------*/

	public void notifierMessage(String message) 
	{
		System.out.println("Contrôleur - Message reçu : " + message);
	}

	public void notifierConnexionServeur(boolean etat)
	{
		System.out.println("Contrôleur - Connexion serveur : " + (etat ? "CONNECTÉ" : "DÉCONNECTÉ"));
	}

	public void notifierConnexionClient(boolean etat, String pseudo) 
	{
		if ( etat ) { this.ihmGui.afficherHome(pseudo); }

		System.out.println("Contrôleur - Connexion client : " + (etat ? "AUTHENTIFIÉ" : "NON AUTHENTIFIÉ") + " (" + pseudo + ")");
	}

	public void notifierEnregistrement(boolean etat) 
	{
		System.out.println("Contrôleur - Enregistrement : " + (etat ? "RÉUSSI" : "ÉCHEC"));
	}

	/*--------------------------*/
	/* MAIN                     */
	/*--------------------------*/
	public static void main(String[] args) 
	{
		Controleur controleur = new Controleur();
	}
}
