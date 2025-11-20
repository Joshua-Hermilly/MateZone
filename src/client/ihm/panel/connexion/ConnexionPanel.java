package client.ihm.panel.connexion;

import javax.swing.*;

import client.controleur.Controleur;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/*-------------------------------*/
/* Class ConnexionPanel          */
/*-------------------------------*/
/**
 * Panneau de connexion qui étend JPanel et gère l'interface de connexion
 * utilisateur.
 * Cette classe gère la saisie du pseudo et du mot de passe du client qui seront
 * envoyés au Contrôleur.
 * Le panneau est utilisé dans la fenêtre ConnexionFrame avec un design moderne
 * en thème sombre.
 *
 * @author Joshua Hermilly
 * @version V1
 * @date 08/11/25
 */
public class ConnexionPanel extends JPanel implements ActionListener 
{
	/*--------------------------*/
	/* Composants               */
	/*--------------------------*/
	/**
	 * Référence vers le contrôleur principal de l'application.
	 * Utilisé pour transmettre les actions de connexion et d'enregistrement.
	 */
	private Controleur controleur;

	/**
	 * Champ de saisie pour le pseudonyme de l'utilisateur.
	 */
	private JTextField txtPseudo;

	/**
	 * Champ de saisie sécurisé pour le mot de passe de l'utilisateur.
	 */
	private JPasswordField pwdMdp;

	/**
	 * Bouton pour déclencher la tentative de connexion.
	 */
	private JButton btnConnecter;

	/**
	 * Bouton pour déclencher l'enregistrement d'un nouvel utilisateur.
	 */
	private JButton btnEnregistrer;

	/*--------------------------*/
	/* Constructeur             */
	/*--------------------------*/
	/**
	 * Constructeur du panneau de connexion.
	 * Initialise tous les composants graphiques avec un thème sombre moderne,
	 * configure le layout et active les écouteurs d'événements.
	 * 
	 * @param controleur le contrôleur principal de l'application
	 */
	public ConnexionPanel(Controleur controleur) 
	{
		this.setLayout    (new BorderLayout()          );
		this.setBackground(new Color(18, 18, 18));

		JPanel panelPrincipal = new JPanel(new BorderLayout()         );
		JPanel panelSaisie    = new JPanel(new GridLayout(2, 1, 0, 15));
		JPanel panelBoutons   = new JPanel(new GridLayout(2, 1, 0, 10));

		this.controleur = controleur;
		/*-------------------------------*/
		/* Création des composants       */
		/*-------------------------------*/
		this.txtPseudo      = new JTextField    (       20      );
		this.pwdMdp         = new JPasswordField(       20      );
		this.btnConnecter   = new JButton       ("Se connecter" );
		this.btnEnregistrer = new JButton       ("S'enregistrer");

		// Style dark pour les champs de texte
		this.txtPseudo.setFont(new Font("Arial", Font.PLAIN, 14));
		this.txtPseudo.setBackground( new Color(40, 40, 40) );
		this.txtPseudo.setForeground( Color.WHITE           );
		this.txtPseudo.setCaretColor( Color.WHITE           );
		this.txtPseudo.setBorder
		(
			BorderFactory.createCompoundBorder
			(
					BorderFactory.createLineBorder (new Color(60, 60, 60), 1),
					BorderFactory.createEmptyBorder(10, 15, 10, 15          )
			)
		);

		this.pwdMdp.setFont(new Font("Arial", Font.PLAIN, 14));
		this.pwdMdp.setBackground( new Color(40, 40, 40) );
		this.pwdMdp.setForeground( Color.WHITE           );
		this.pwdMdp.setCaretColor( Color.WHITE           );
		this.pwdMdp.setBorder
		(
			BorderFactory.createCompoundBorder
			(
					BorderFactory.createLineBorder (new Color(60, 60, 60), 1),
					BorderFactory.createEmptyBorder(10, 15, 10, 15          )
			)
		);

		// Style dark pour les boutons
		this.btnConnecter.setFont(new Font("Arial", Font.BOLD, 14));
		this.btnConnecter.setBackground( new Color(0, 122, 255) );
		this.btnConnecter.setForeground( Color.WHITE            );
		this.btnConnecter.setBorder( BorderFactory.createEmptyBorder(12, 30, 12, 30) );
		this.btnConnecter.setFocusPainted(false);

		this.btnEnregistrer.setFont(new Font("Arial", Font.PLAIN, 14));
		this.btnEnregistrer.setBackground(new Color(30, 30,  30));
		this.btnEnregistrer.setForeground(new Color(0, 122, 255));
		this.btnEnregistrer.setBorder
		(
			BorderFactory.createCompoundBorder
			(
					BorderFactory.createLineBorder (new Color(0, 122, 255), 1),
					BorderFactory.createEmptyBorder(12, 30, 12, 30           )
			)
		);
		this.btnEnregistrer.setFocusPainted(false);

		/*-------------------------------*/
		/* Positionnement des composants */
		/*-------------------------------*/
		JLabel lblTitre = new JLabel("Connexion", SwingConstants.CENTER);
		lblTitre.setFont(new Font("Arial", Font.BOLD, 24));
		lblTitre.setForeground(Color.WHITE);

		// Panel pour pseudo avec placeholder effect
		JPanel panelPseudo = new JPanel(new BorderLayout());
		JLabel lblPseudo   = new JLabel( "Pseudo"         );
		lblPseudo.setFont(new Font("Arial", Font.PLAIN, 12));
		lblPseudo.setForeground(new Color(180, 180, 180));
		panelPseudo.add(lblPseudo     , BorderLayout.NORTH );
		panelPseudo.add(this.txtPseudo, BorderLayout.CENTER);
		panelPseudo.setOpaque(false);

		// Panel pour mot de passe
		JPanel panelMdp = new JPanel(new BorderLayout());
		JLabel lblMdp   = new JLabel( "Mot de passe"   );
		lblMdp.setFont(new Font("Arial", Font.PLAIN, 12));
		lblMdp.setForeground(new Color(180, 180, 180));
		panelMdp.add(lblMdp     , BorderLayout.NORTH );
		panelMdp.add(this.pwdMdp, BorderLayout.CENTER);
		panelMdp.setOpaque(false);

		panelSaisie.add(panelPseudo);
		panelSaisie.add(panelMdp   );
		panelSaisie.setOpaque(false);

		panelBoutons.add(this.btnConnecter  );
		panelBoutons.add(this.btnEnregistrer);
		panelBoutons.setOpaque(false);

		// Container principal avec style carte dark
		panelPrincipal.setBackground(new Color(30, 30, 30));
		panelPrincipal.setBorder
		(
			BorderFactory.createCompoundBorder
			(
				BorderFactory.createLineBorder (new Color(50, 50, 50), 1),
				BorderFactory.createEmptyBorder(40, 40, 40, 40          )
			)
		);

		panelPrincipal.add(lblTitre                   , BorderLayout.NORTH );
		panelPrincipal.add(Box.createVerticalStrut(30), BorderLayout.CENTER);

		JPanel panelCentre = new JPanel(new BorderLayout());
		panelCentre.setOpaque(false);
		panelCentre.add(panelSaisie                , BorderLayout.NORTH );
		panelCentre.add(Box.createVerticalStrut(30), BorderLayout.CENTER);
		panelCentre.add(panelBoutons               , BorderLayout.SOUTH );

		panelPrincipal.add(panelCentre, BorderLayout.CENTER);

		// Centrer le panel principal
		this.add(Box.createGlue(), BorderLayout.NORTH );
		this.add(Box.createGlue(), BorderLayout.SOUTH );
		this.add(Box.createGlue(), BorderLayout.WEST  );
		this.add(Box.createGlue(), BorderLayout.EAST  );
		this.add(panelPrincipal  , BorderLayout.CENTER);

		/*-----------------------------*/
		/* Activation des composants   */
		/*-----------------------------*/
		this.btnConnecter  .addActionListener(this);
		this.btnEnregistrer.addActionListener(this);
	}

	/*-----------------------------*/
	/* Méthode Listener            */
	/*-----------------------------*/
	/**
	 * Gère les événements de clic sur les boutons du panneau.
	 * Récupère les valeurs saisies et déclenche l'action appropriée via le
	 * contrôleur.
	 * 
	 * @param e l'événement d'action déclenché par un composant
	 */
	public void actionPerformed(ActionEvent e)
	{
		String pseudo = txtPseudo.getText().trim();
		String mdp    = new String(pwdMdp.getPassword()).trim();

		if (e.getSource() == this.btnConnecter  ) { this.controleur.tenterConnexionClient(pseudo, mdp); }
		if (e.getSource() == this.btnEnregistrer) { this.controleur.tenterEnregistrement (pseudo, mdp); }
	}
}