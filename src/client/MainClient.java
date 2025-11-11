package client;

import client.controleur.Controleur;

/*-------------------------------*/
/* Class MainClient              */
/*-------------------------------*/
/**
 * Classe principale du client qui sert de point d'entrée pour l'application.
 * Cette classe contient la méthode main qui initialise et lance l'application
 * en créant une instance de contrôleur.
 * 
 * @author MateZone Team
 * @author Joshua Hermilly
 * @author Prévost Donovan
 * @version V1
 * @date 08/11/25
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