package client;

import java.io.FileInputStream;
import java.util.Properties;

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
		String ADRESSE  = "";
		int    CHATPORT = 0;
		int    IMGPORT  = 0;
		
		try 
		{	
			Properties props = new Properties();
			FileInputStream fis = new FileInputStream("src/client/config.properties");
			props.load(fis);
			fis.close();

			ADRESSE  = props.getProperty("server.URL"      );
			CHATPORT = Integer.parseInt( props.getProperty("serverChat.PORT" ) );
			IMGPORT  = Integer.parseInt( props.getProperty("serverImg.PORT"  ) );
		
		} catch (Exception e) { System.err.println("config" ); e.printStackTrace(); System.exit( 1 );	}

		try
		{
			Controleur controleur = new Controleur( ADRESSE, CHATPORT, IMGPORT );
			controleur.lancerApp(); // affiche la fenêtre

		} catch (Exception e) { e.printStackTrace(); }
	}
}