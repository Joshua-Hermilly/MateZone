package client.ihm.frame.affichage;

import java.awt.BorderLayout;

import javax.swing.JFrame;

import client.controleur.Controleur;
import client.ihm.panel.affichage.SaisieMessagePanel;

/*-------------------------------*/
/* Class MateZoneFrame           */
/*-------------------------------*/
/**
 * Classe extend JFrame qui fait office de fenètre principale du client.
 * Le panel est fait avec le client/ihm/panel/affichage/SaisieMessagePanel.
 */
public class MateZoneFrame extends JFrame
{
	/*--------------------------*/
	/*   Attributs              */
	/*--------------------------*/
	private SaisieMessagePanel saisieMessagePanel;
	
	/*--------------------------*/
	/*   Constructeur           */
	/*--------------------------*/
	public MateZoneFrame( Controleur controleur )
	{
		this.setTitle("MateZone");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);
		
		/*-------------------------------*/
		/* Création des composants       */
		/*-------------------------------*/
		this.saisieMessagePanel = new SaisieMessagePanel( controleur );

		/*-------------------------------*/
		/* Positionnement des composants */
		/*-------------------------------*/
		this.add(this.saisieMessagePanel, BorderLayout.SOUTH );
		
		this.pack();
		this.setLocationRelativeTo(null);
	}
}
