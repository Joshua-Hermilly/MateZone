package client;

import client.controleur.Controleur;

/*-------------------------------*/
/* Class MainClient              */
/*-------------------------------*/
/**
 * Classe MainClient lance l'application.
 * Créer un Controleur et lance l'application via la méthode lancerApp de Controleur.
 */
public class MainClient 
{
	/*--------------------------*/
	/*        Main              */
	/*--------------------------*/
	public static void main(String[] args) 
	{
		try
		{
			Controleur controleur = new Controleur();
			controleur.lancerApp(); // affiche la fenêtre

		} catch (Exception e) { e.printStackTrace(); }
	}
}