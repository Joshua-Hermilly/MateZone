package client.ihm.frame.connexion;

import javax.swing.*;

import client.ihm.IhmGui;
import client.ihm.panel.connexion.SaisiePanel;
import client.ihm.panel.connexion.SortiePanel;

import java.awt.*;


/*-------------------------------*/
/* ConnectionFrame               */
/*-------------------------------*/
/**
 * Fenêtre de connexion en package par défaut pour permettre l'accès au
 * contrôleur qui se trouve lui aussi dans le package par défaut.
 */
public class ConnectionFrame extends JFrame
{
	/*--------------------------*/
	/*     Attributs            */
	/*--------------------------*/
	private IhmGui ihmGui;

	private SaisiePanel saisiePanel;
	private SortiePanel sortiePanel;

	/*--------------------------*/
	/*     Constructeur         */
	/*--------------------------*/
	public ConnectionFrame(IhmGui ihmGui)
	{
		this.ihmGui = ihmGui;

		setTitle("Connection Frame");
		setSize(400, 300);
		setLayout(new BorderLayout());

		/*-------------------------------*/
		/* Création des composants       */
		/*-------------------------------*/
		this.saisiePanel = new SaisiePanel( this );
		this.sortiePanel = new SortiePanel();

		this.add(this.saisiePanel, BorderLayout.NORTH);
		this.add(this.sortiePanel, BorderLayout.CENTER);


		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);

		this.setVisible(true);
	}

	/*--------------------------*/
	/*  Méthodes                */
	/*--------------------------*/

	public boolean testerConnexionAuServeur(String host, int port)
	{
		return this.ihmGui.testerConnexionAuServeur(host, port);
	}


	public void afficherMessage(String message)
	{
		this.sortiePanel.append( message );
	}

}
