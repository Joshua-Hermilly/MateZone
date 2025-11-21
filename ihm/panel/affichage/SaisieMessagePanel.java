package client.ihm.panel.affichage;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import client.controleur.Controleur;

/*-------------------------------*/
/* Class SaisieMessagePanel     */
/*-------------------------------*/
/**
 * Panneau de saisie des messages qui étend JPanel et gère l'envoi de messages
 * et pièces jointes.
 * Contient un champ de texte, un bouton d'envoi et un bouton pour les pièces
 * jointes.
 * L'envoi peut se faire via la touche Entrée ou le bouton Envoyer.
 * Utilise un thème sombre cohérent avec le reste de l'application.
 *
 * @author Joshua Hermilly
 * @version V1
 * @date 08/11/25
 */
public class SaisieMessagePanel extends JPanel implements ActionListener 
{
	/*--------------------------*/
	/* Composants               */
	/*--------------------------*/
	/**
	 * Référence vers le contrôleur principal pour envoyer les messages et pièces
	 * jointes.
	 */
	private final Controleur controleur;

	/**
	 * Champ de texte pour la saisie des messages par l'utilisateur.
	 */
	private JTextField txtMessage;

	/**
	 * Bouton principal pour envoyer le message saisi.
	 */
	private JButton btnEnvoyer;

	/**
	 * Bouton pour ouvrir le sélecteur de fichier et envoyer une pièce jointe.
	 */
	private JButton btnPieceJointe;

	/**
	 * Panneau principal contenant tous les éléments de saisie.
	 */
	private JPanel panelPrincipal;

	/**
	 * Panneau organisant le champ de texte et les boutons horizontalement.
	 */
	private JPanel panelSaisie;

	/**
	 * Panneau contenant les boutons d'action (envoyer et pièce jointe).
	 */
	private JPanel panelBoutons;

	/*--------------------------*/
	/* Constructeur             */
	/*--------------------------*/
	/**
	 * Constructeur du panneau de saisie des messages.
	 * Initialise tous les composants graphiques avec le thème sombre,
	 * configure la mise en page et active les écouteurs d'événements.
	 * 
	 * @param controleur le contrôleur principal de l'application
	 */
	public SaisieMessagePanel(Controleur controleur)
	{
		this.controleur = controleur;

		this.setLayout(new BorderLayout());
		this.setBackground(new Color(18, 18, 18));

		/*-------------------------------*/
		/* Création des composants       */
		/*-------------------------------*/
		this.creerComposants();

		/*-------------------------------*/
		/* Positionnement des composants */
		/*-------------------------------*/
		this.positionnerComposants();

		/*-------------------------------*/
		/* Ajout des listeners           */
		/*-------------------------------*/
		this.ajouterListeners();
	}

	/*--------------------------*/
	/* Création des composants */
	/*--------------------------*/
	/**
	 * Crée et configure tous les composants graphiques du panneau.
	 * Initialise le champ de texte, les boutons et les panneaux conteneurs avec le
	 * style sombre.
	 */
	private void creerComposants() 
	{
		// Champ de texte
		this.txtMessage = new JTextField(30);
		this.txtMessage.setFont(new Font("Arial", Font.PLAIN, 14));
		this.txtMessage.setBackground(new Color(40, 40, 40));
		this.txtMessage.setForeground(Color.WHITE);
		this.txtMessage.setCaretColor(Color.WHITE);
		this.txtMessage.setBorder
		(
			BorderFactory.createCompoundBorder
			(
					BorderFactory.createLineBorder(new Color(60, 60, 60), 1),
					BorderFactory.createEmptyBorder(10, 15, 10, 15)
			)
		);

		// Bouton envoyer
		this.btnEnvoyer = new JButton("Envoyer");
		this.btnEnvoyer.setFont(new Font("Arial", Font.BOLD, 14));
		this.btnEnvoyer.setBackground(new Color(0, 122, 255));
		this.btnEnvoyer.setForeground(Color.WHITE);
		this.btnEnvoyer.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));
		this.btnEnvoyer.setFocusPainted(false);

		// Bouton pièce jointe
		this.btnPieceJointe = new JButton("Pièce jointe");
		this.btnPieceJointe.setFont(new Font("Arial", Font.PLAIN, 14));
		this.btnPieceJointe.setBackground(new Color(30, 30, 30));
		this.btnPieceJointe.setForeground(new Color(0, 122, 255));
		this.btnPieceJointe.setBorder
		(
			BorderFactory.createCompoundBorder
			(
				BorderFactory.createLineBorder(new Color(0, 122, 255), 1),
				BorderFactory.createEmptyBorder(12, 20, 12, 20)
			)
		);
		this.btnPieceJointe.setFocusPainted(false);

		// Panels
		this.panelPrincipal = new JPanel(new BorderLayout());
		this.panelPrincipal.setBackground(new Color(30, 30, 30));
		this.panelPrincipal.setBorder
		(
			BorderFactory.createCompoundBorder
			(
					BorderFactory.createLineBorder(new Color(50, 50, 50), 1),
					BorderFactory.createEmptyBorder(15, 20, 15, 20)
			)
		);

		this.panelSaisie = new JPanel(new BorderLayout(10, 0));
		this.panelSaisie.setOpaque(false);

		this.panelBoutons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
		this.panelBoutons.setOpaque(false);
	}

	/*----------------------------------*/
	/* Positionnement des composants    */
	/*----------------------------------*/
	/**
	 * Organise et positionne tous les composants dans la hiérarchie des panneaux.
	 * Configure la disposition horizontale avec le champ de texte au centre et les
	 * boutons à droite.
	 */
	private void positionnerComposants() 
	{
		this.panelBoutons.add(this.btnPieceJointe);
		this.panelBoutons.add(this.btnEnvoyer    );

		this.panelSaisie.add(this.txtMessage  , BorderLayout.CENTER);
		this.panelSaisie.add(this.panelBoutons, BorderLayout.EAST  );

		this.panelPrincipal.add(this.panelSaisie, BorderLayout.CENTER);

		this.add(this.panelPrincipal, BorderLayout.CENTER);
	}

	/*----------------------------------*/
	/* Ajout des listeners              */
	/*----------------------------------*/
	/**
	 * Ajoute les écouteurs d'événements aux composants interactifs.
	 * Configure les actions pour les boutons et la touche Entrée du champ de texte.
	 */
	private void ajouterListeners() 
	{
		this.btnEnvoyer    .addActionListener(this              );
		this.btnPieceJointe.addActionListener(this              );
		this.txtMessage    .addActionListener(e -> sendMessage());
	}

	/*----------------------------------*/
	/* Override */
	/*----------------------------------*/
	/**
	 * Gère les événements de clic sur les boutons du panneau.
	 * Traite l'envoi de messages texte et l'ouverture du sélecteur de fichiers pour
	 * les pièces jointes.
	 * 
	 * @param e l'événement d'action déclenché par un composant
	 */
	@Override
	public void actionPerformed(ActionEvent e) 
	{
		if (e.getSource() == this.btnEnvoyer) 
		{
			this.sendMessage();
		} 

		else if (e.getSource() == this.btnPieceJointe)
		{
			JFileChooser chooser = new JFileChooser();
			chooser.setDialogTitle("Choisir une image");
			chooser.setCurrentDirectory(new File("."));

			int rv = chooser.showOpenDialog(this);
			if (rv == JFileChooser.APPROVE_OPTION) 
			{
				File selected = chooser.getSelectedFile();
				this.controleur.envoyerPieceJoint(selected.getAbsolutePath());
			}
		}
	}

	/*----------------------------------*/
	/* Méthodes privées                 */
	/*----------------------------------*/
	/**
	 * Envoie le message saisi dans le champ de texte.
	 * Vérifie que le message n'est pas vide, l'envoie via le contrôleur et vide le
	 * champ.
	 */
	private void sendMessage() 
	{
		String message = this.txtMessage.getText().trim();
		
		if (!message.isEmpty()) 
		{
			this.controleur.envoyerMessage(message);
			this.txtMessage.setText("");
		}
	}
}
