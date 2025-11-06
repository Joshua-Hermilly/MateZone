package client.ihm.frame.affichage;

import javax.swing.*;

import client.ihm.IhmGui;
import client.ihm.panel.affichage.MainHomePanel;

import java.awt.*;

/*-------------------------------*/
/* HomeFrame                     */
/*-------------------------------*/
/**
 * Fenêtre principale de l'application (écran d'accueil) similaire à Discord.
 * Point de départ après la connexion réussie.
 */
public class HomeFrame extends JFrame {
	/*--------------------------*/
	/* Attributs */
	/*--------------------------*/
	private IhmGui ihmGui;
	private String pseudo;

	private MainHomePanel mainHomePanel;

	/*--------------------------*/
	/* Constructeur */
	/*--------------------------*/
	public HomeFrame(IhmGui ihmGui, String pseudo) {
		this.ihmGui = ihmGui;
		this.pseudo = pseudo;

		this.setTitle("MateZone - " + pseudo);
		this.setSize(800, 600);
		this.setLayout(new BorderLayout());

		/*-------------------------------*/
		/* Création des composants */
		/*-------------------------------*/
		this.mainHomePanel = new MainHomePanel(this, pseudo);

		this.add(this.mainHomePanel, BorderLayout.CENTER);

		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);

		this.setVisible(true);

		// Message de bienvenue dans les logs
		this.afficherMessage("=== Connexion réussie ===");
		this.afficherMessage("Bienvenue dans MateZone, " + pseudo + " !");
	}

	/*--------------------------*/
	/* Méthodes */
	/*--------------------------*/

	/**
	 * Affiche un message dans la zone de messages du panel principal
	 */
	public void afficherMessage(String message) {
		this.mainHomePanel.afficherMessage(message);
	}

	/**
	 * Ajoute un nouveau serveur à la liste
	 */
	public void ajouterServeur(String nomServeur) {
		this.mainHomePanel.ajouterServeur(nomServeur);
	}

	/**
	 * Quitte l'application avec confirmation
	 */
	public void quitterApplication() {
		int choix = JOptionPane.showConfirmDialog(this,
				"Êtes-vous sûr de vouloir quitter MateZone ?",
				"Confirmation",
				JOptionPane.YES_NO_OPTION,
				JOptionPane.QUESTION_MESSAGE);

		if (choix == JOptionPane.YES_OPTION) {
			System.exit(0);
		}
	}

	/**
	 * Retourne le pseudo de l'utilisateur connecté
	 */
	public String getPseudo() {
		return this.pseudo;
	}
}
