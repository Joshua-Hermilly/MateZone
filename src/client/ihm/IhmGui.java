package client.ihm;

import javax.swing.JOptionPane;

import client.controleur.Controleur;
import client.ihm.frame.affichage.MateZoneFrame;
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
	private MateZoneFrame  mateZoneFrame;

	/*--------------------------*/
	/*     Constructeur         */
	/*--------------------------*/
	public IhmGui( Controleur controleur ) 
	{
		this.controleur      = controleur;
		this.connexionFrame  = null;
	}

	public void lancerConnexionFrame()
	{
		this.connexionFrame = new ConnexionFrame( this.controleur );
		this.connexionFrame.setVisible(true);
	}

	public void lancerMateZoneFrame()
	{
		this.mateZoneFrame = new MateZoneFrame( this.controleur );
		this.mateZoneFrame.setVisible( true );
		this.connexionFrame.dispose();;
	}


	/*--------------------------*/
	/*     Affichage            */
	/*--------------------------*/
	public void afficherErreur ( String message )
	{
		JOptionPane.showMessageDialog(this.connexionFrame, message, "Erreur", JOptionPane.ERROR_MESSAGE);
	}
}
