package client.ihm.frame.connexion;

import javax.swing.JFrame;

import client.controleur.Controleur;
import client.ihm.panel.connexion.ConnexionPanel;

/*-------------------------------*/
/* Class ConnexionFrame          */
/*-------------------------------*/
/**
 * Classe extend JFrame qui fait office de fenètre de connexion.
 * Le panel est fait avec le client/ihm/panel/connexion/ConnexionPanel.
 */
public class ConnexionFrame extends JFrame
{
	/*--------------------------*/
	/*   Attributs              */
	/*--------------------------*/
	private ConnexionPanel panelConnexion;
	
	/*--------------------------*/
	/*   Constructeur           */
	/*--------------------------*/
	public ConnexionFrame( Controleur controleur )
	{
		this.setTitle("Connexion");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);
		
		/*-------------------------------*/
		/* Création des composants       */
		/*-------------------------------*/
		this.panelConnexion = new ConnexionPanel( controleur );

		/*-------------------------------*/
		/* Positionnement des composants */
		/*-------------------------------*/
		this.add(this.panelConnexion);
		
		this.pack();
		this.setLocationRelativeTo(null);
	}
}
