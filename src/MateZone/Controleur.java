package MateZone;

import MateZone.ihm.frame.ConnectionFrame;
import MateZone.metier.client.ClientConnexion;

/*-------------------------------*/
/* Classe Controleur            */
/*-------------------------------*/
/**
 * Classe principale de contrôle de l'application MateZone.
 * Gère la logique de connexion et le lancement des différentes interfaces utilisateur.
 */
public class Controleur
{
	/*--------------------------*/
	/*        Attributs         */
	/*--------------------------*/

	/** Adresse de l'hôte du serveur. */
	private String host;

	/** Port du serveur. */
	private int port;


	/*--------------------------*/
	/*     Constructeur         */
	/*--------------------------*/
	public Controleur() {}

	public Controleur(String host, int port)
	{
		this.host   = host;
		this.port   = port;
	}

	/*--------------------------*/
	/*     Méthodes             */
	/*--------------------------*/
	public void start() { new ConnectionFrame( this ); }

	/*--------------------------*/
	/*     Client               */
	/*--------------------------*/
	public boolean ConnexionClient( String identifant, String mdp )
	{

		// Création et connexion du WebSocketClient vers le serveur déjà lancé
		try 
		{
			ClientConnexion connexion = new ClientConnexion( this.host, this.port );
			connexion.connectBlocking();
			// Envoyer une identification minimaliste au serveur
			connexion.send( "IDENTIFIANT : " + identifant );

			return true;
		}
		catch (Exception e) { e.printStackTrace(); return false; }
	}

	/*--------------------------*/
	/*     Serveur              */
	/*--------------------------*/
	public boolean ConnexionServeur(String host, int port)
	{
		try
		{
			ClientConnexion connexion = new ClientConnexion(host, port);
			connexion.connectBlocking();
			System.out.println("Connexion réussie au serveur " + host + ":" + port);
			return true;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return false;
		}
	}

	
	/*--------------------------*/
	/*     MAIN                 */
	/*--------------------------*/
	public static void main(String[] args)
	{
		Controleur controleur = new Controleur();
		controleur.start();	
	}
}
