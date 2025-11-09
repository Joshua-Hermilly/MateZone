package client.ihm;

import javax.swing.JOptionPane;

import client.controleur.Controleur;
import client.ihm.frame.connexion.ConnexionFrame;

/*-------------------------------*/
/* Classe IhmGui                 */
/*-------------------------------*/
/**
 * Classe IhmGui - Gère le lacnement et fermeture des difféentes frames.
 * Fait le lien entre Controleur -> ihm affin d'alléger le controleur.
 */
public class IhmGui 
{
	/*--------------------------*/
	/*     Attributs            */
	/*--------------------------*/
	private Controleur controleur;

	private ConnexionFrame connexionFrame;

	/*--------------------------*/
	/*     Constructeur         */
	/*--------------------------*/
	public IhmGui( Controleur controleur ) 
	{
		this.controleur      = controleur;
		this.connexionFrame  = null;
	}

	public void lancerApp()
	{
		this.connexionFrame = new ConnexionFrame( this.controleur );
		this.connexionFrame.setVisible(true);
	}


	/*--------------------------*/
	/*     Affichage            */
	/*--------------------------*/
	public void afficherErreur ( String message )
	{
		JOptionPane.showMessageDialog(this.connexionFrame, message, "Erreur", JOptionPane.ERROR_MESSAGE);
	}
}
