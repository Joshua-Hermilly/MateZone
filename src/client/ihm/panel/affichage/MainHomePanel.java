package client.ihm.panel.affichage;

import client.ihm.frame.affichage.HomeFrame;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

/**
 * Panel principal de l'écran d'accueil, similaire à l'interface Discord.
 * Contient la liste des serveurs/salles et les fonctionnalités principales.
 */
public class MainHomePanel extends JPanel implements ActionListener {
	/*--------------------------*/
	/* Composants */
	/*--------------------------*/
	private HomeFrame frame;

	private JList<String> lstServeurs;
	private DefaultListModel<String> modelServeurs;
	private JButton btnAjouterServeur;
	private JButton btnQuitter;
	private JLabel lblBienvenue;
	private JTextArea txtZoneMessages;
	private JScrollPane scrollMessages;

	/*--------------------------*/
	/* Constructeur */
	/*--------------------------*/
	public MainHomePanel(HomeFrame frame, String pseudo) {
		this.setLayout(new BorderLayout());

		this.frame = frame;

		/*-------------------------------*/
		/* Création des composants */
		/*-------------------------------*/
		this.lblBienvenue = new JLabel("Bienvenue " + pseudo + " !", JLabel.CENTER);
		this.lblBienvenue.setFont(new Font("Arial", Font.BOLD, 16));

		// Liste des serveurs (côté gauche)
		this.modelServeurs = new DefaultListModel<>();
		this.modelServeurs.addElement("Serveur Général");
		this.modelServeurs.addElement("Serveur Gaming");
		this.modelServeurs.addElement("Serveur Études");

		this.lstServeurs = new JList<>(this.modelServeurs);
		this.lstServeurs.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		this.lstServeurs.setBorder(BorderFactory.createTitledBorder("Serveurs"));

		// Zone de messages (centre)
		this.txtZoneMessages = new JTextArea(15, 40);
		this.txtZoneMessages.setEditable(false);
		this.txtZoneMessages.setBackground(Color.LIGHT_GRAY);
		this.txtZoneMessages.append("=== Zone de messages ===\n");
		this.txtZoneMessages.append("Sélectionnez un serveur pour commencer à discuter.\n");
		this.scrollMessages = new JScrollPane(this.txtZoneMessages);

		// Boutons
		this.btnAjouterServeur = new JButton("Ajouter Serveur");
		this.btnQuitter = new JButton("Quitter");

		/*-------------------------------*/
		/* Panel des boutons */
		/*-------------------------------*/
		JPanel panelBoutons = new JPanel(new FlowLayout());
		panelBoutons.add(this.btnAjouterServeur);
		panelBoutons.add(this.btnQuitter);

		/*-------------------------------*/
		/* Panel latéral gauche */
		/*-------------------------------*/
		JPanel panelGauche = new JPanel(new BorderLayout());
		panelGauche.add(new JScrollPane(this.lstServeurs), BorderLayout.CENTER);
		panelGauche.add(panelBoutons, BorderLayout.SOUTH);
		panelGauche.setPreferredSize(new Dimension(200, 0));

		/*-------------------------------*/
		/* Ajout des composants */
		/*-------------------------------*/
		this.add(this.lblBienvenue, BorderLayout.NORTH);
		this.add(panelGauche, BorderLayout.WEST);
		this.add(this.scrollMessages, BorderLayout.CENTER);

		/*-------------------------------*/
		/* Activation des composants */
		/*-------------------------------*/
		this.btnAjouterServeur.addActionListener(this);
		this.btnQuitter.addActionListener(this);

	}

	/*--------------------------*/
	/* Méthodes */
	/*--------------------------*/

	/**
	 * Ajoute un message dans la zone de messages
	 */
	public void afficherMessage(String message) {
		this.txtZoneMessages.append(message + "\n");
		this.txtZoneMessages.setCaretPosition(this.txtZoneMessages.getDocument().getLength());
	}

	/**
	 * Ajoute un nouveau serveur à la liste
	 */
	public void ajouterServeur(String nomServeur) {
		this.modelServeurs.addElement(nomServeur);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == this.btnAjouterServeur) {
			String nomServeur = JOptionPane.showInputDialog(this,
					"Nom du nouveau serveur :",
					"Ajouter un serveur",
					JOptionPane.QUESTION_MESSAGE);

			if (nomServeur != null && !nomServeur.trim().isEmpty()) {
				this.ajouterServeur(nomServeur.trim());
				this.afficherMessage("Serveur '" + nomServeur.trim() + "' ajouté.");
			}
		} else if (e.getSource() == this.btnQuitter) {
			this.frame.quitterApplication();
		}

		System.out.println("Action performed: " + e.getActionCommand());
	}
}