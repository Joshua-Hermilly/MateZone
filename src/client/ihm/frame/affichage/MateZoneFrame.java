package client.ihm.frame.affichage;

import java.awt.BorderLayout;

import javax.swing.JFrame;

import client.controleur.Controleur;
import client.ihm.panel.affichage.SaisieMessagePanel;
import client.ihm.panel.affichage.SalonPanel;

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
	private SalonPanel         salonPanel;
	
	/*--------------------------*/
	/*   Constructeur           */
	/*--------------------------*/
	public MateZoneFrame( Controleur controleur )
	{
		this.setTitle("MateZone");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(true);
		this.setSize(800, 600);
		
		/*-------------------------------*/
		/* Création des composants       */
		/*-------------------------------*/
		this.saisieMessagePanel = new SaisieMessagePanel( controleur );
		this.salonPanel         = new SalonPanel        ( "#TEMP14>" );

		/*-------------------------------*/
		/* Positionnement des composants */
		/*-------------------------------*/
		this.add(this.salonPanel        , BorderLayout.CENTER );
		this.add(this.saisieMessagePanel, BorderLayout.SOUTH  );
		
		this.setLocationRelativeTo(null);
	}
}
