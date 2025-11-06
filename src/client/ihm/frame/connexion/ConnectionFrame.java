package client.ihm.frame.connexion;

import javax.swing.*;

import client.ihm.IhmGui;
import client.ihm.panel.connexion.SaisieServeurPanel;
import client.ihm.panel.connexion.SaisieClientPanel;
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

	private SaisieServeurPanel saisieServeurPanel;
	private SaisieClientPanel  saisieClientPanel;
	private SortiePanel        sortiePanel;

	/*--------------------------*/
	/*     Constructeur         */
	/*--------------------------*/
	public ConnectionFrame(IhmGui ihmGui)
	{
		this.ihmGui = ihmGui;

		this.setTitle("Connection Frame");
		this.setSize(400, 300);
		this.setLayout(new BorderLayout());

		/*-------------------------------*/
		/* Création des composants       */
		/*-------------------------------*/
		this.saisieServeurPanel = new SaisieServeurPanel( this );
		this.sortiePanel        = new SortiePanel();

		this.add(this.saisieServeurPanel, BorderLayout.NORTH);
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
		boolean resultat;

		resultat = this.ihmGui.testerConnexionAuServeur(host, port);
		
		//Passage ne mode Client
		if (resultat)
		{
			this.remove(this.saisieServeurPanel);
			this.saisieClientPanel = new SaisieClientPanel( this );
			this.add(this.saisieClientPanel, BorderLayout.NORTH);

			this.revalidate();
			this.repaint();
		}

		return resultat;
	}

	public void connexionAuClient(String nom, String mdp)
	{
		this.ihmGui.connexionAuClient(nom, mdp);
	}

	public void enregistrerClient(String nom, String mdp)
	{
		this.ihmGui.enregistrerClient(nom, mdp);
	}


	public void afficherMessage(String message)
	{
		this.sortiePanel.append( message );
	}

}