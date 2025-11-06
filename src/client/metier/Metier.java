package client.metier;

import client.metier.connexionServeur.ClientConnexion;

public class Metier 
{
	/*--------------------------*/
	/*        Attributs         */
	/*--------------------------*/
	private static String host;
	private static int    port;

	private static ClientConnexion client;

	/*--------------------------*/
	/*     Constructeur         */
	/*--------------------------*/
	public Metier() 
	{
		Metier.host      = null;
		Metier.port      = -1;
		Metier.client    = null;
	}

	/*--------------------------*/
	/*     Client               */
	/*--------------------------*/
	public void connexionAuClient(String pseudo, String mdp) 
	{
		Metier.client.envoyerMessage("LOGIN:" + pseudo + ":" + mdp);
	}

	public void creerClient(String pseudo, String mdp) 
	{
		Metier.client.envoyerMessage("REGISTER:" + pseudo + ":" + mdp);
	}

	public void envoyerMessage(String message) 
	{
		Metier.client.envoyerMessage("NEWMESSAGE:1:" + Metier.client.getIdClient() + ":" + message);
	}


	/*--------------------------*/
	/*     Serveur              */
	/*--------------------------*/
	public boolean testerConnexionAuServeur(String host, int port) 
	{
		try 
		{
			Metier.client = new ClientConnexion(host, port);
			Metier.client.connectBlocking();

			Metier.host = host;
			Metier.port = port;

			return Metier.client.isOpen();
		
		} catch (Exception e)  { e.printStackTrace(); return false; }
	}

	public void deconnecterDuServeur() 
	{
		try 
		{
			if ( Metier.client != null && Metier.client.isOpen() ) 
			{
				Metier.client.closeBlocking();
			}
		
		} catch (Exception e) { e.printStackTrace(); }
	}


	/*--------------------------*/
	/*   Getters et Setters     */
	/*--------------------------*/
	public static String          getHost     () { return Metier.host;                  }
	public static int             getPort     () { return Metier.port;                  }
	public static ClientConnexion getClient   () { return Metier.client;                }
	public static boolean         estConnectee() { return Metier.client.estConnectee(); }

	public static void   setHost(String host) { Metier.host = host; }
	public static void   setPort(int port   ) { Metier.port = port; }
}
