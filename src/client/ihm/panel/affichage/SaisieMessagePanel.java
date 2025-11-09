package client.ihm.panel.affichage;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import client.controleur.Controleur;

/*-------------------------------*/
/* Class SaisieMessagePanel      */
/*-------------------------------*/
/**
 * Classe extend JPanel qui fait office de panel de pour la saisie des message (...images ??).
 * Cette class gère la saisie du client avec les messages, images pieces jointes qu'il voudra afficher.
 * La frame est faite avec le client/ihm/frame/affichage/MateZoneFrame
 */
public class SaisieMessagePanel extends JPanel implements ActionListener
{
	/*--------------------------*/
	/*        Composants        */
	/*--------------------------*/
	private Controleur     controleur;

	private JTextField     txtMessage;
	private JButton        btnEnvoyer;
	private JButton        btnPieceJointe;

	/*--------------------------*/
	/*       Constructeur       */
	/*--------------------------*/
	public SaisieMessagePanel( Controleur controleur )
	{
		this.controleur = controleur;
		this.setLayout(new BorderLayout());
		this.setBackground(new Color(18, 18, 18));
		
		/*-------------------------------*/
		/* Création des composants       */
		/*-------------------------------*/
		this.txtMessage = new JTextField(30);
		this.btnEnvoyer = new JButton("Envoyer");
		this.btnPieceJointe = new JButton("Pièce jointe");

		// Style dark pour le champ de texte
		this.txtMessage.setFont(new Font("Arial", Font.PLAIN, 14));
		this.txtMessage.setBackground(new Color(40, 40, 40));
		this.txtMessage.setForeground(Color.WHITE);
		this.txtMessage.setCaretColor(Color.WHITE);
		this.txtMessage.setBorder(
			BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(new Color(60, 60, 60), 1),
				BorderFactory.createEmptyBorder(10, 15, 10, 15)
			)
		);

		// Style dark pour le bouton envoyer (bouton principal)
		this.btnEnvoyer.setFont(new Font("Arial", Font.BOLD, 14));
		this.btnEnvoyer.setBackground(new Color(0, 122, 255));
		this.btnEnvoyer.setForeground(Color.WHITE);
		this.btnEnvoyer.setBorder(
			BorderFactory.createEmptyBorder(12, 20, 12, 20)
		);
		this.btnEnvoyer.setFocusPainted(false);

		// Style dark pour le bouton pièce jointe (bouton secondaire)
		this.btnPieceJointe.setFont(new Font("Arial", Font.PLAIN, 14));
		this.btnPieceJointe.setBackground(new Color(30, 30, 30));
		this.btnPieceJointe.setForeground(new Color(0, 122, 255));
		this.btnPieceJointe.setBorder(
			BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(new Color(0, 122, 255), 1),
				BorderFactory.createEmptyBorder(12, 20, 12, 20)
			)
		);
		this.btnPieceJointe.setFocusPainted(false);

		/*-------------------------------*/
		/* Positionnement des composants */
		/*-------------------------------*/
		JPanel panelPrincipal = new JPanel(new BorderLayout());
		panelPrincipal.setBackground(new Color(30, 30, 30));
		panelPrincipal.setBorder(
			BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(new Color(50, 50, 50), 1),
				BorderFactory.createEmptyBorder(15, 20, 15, 20)
			)
		);

		JPanel panelSaisie = new JPanel(new BorderLayout(10, 0));
		panelSaisie.setOpaque(false);
		
		JPanel panelBoutons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
		panelBoutons.setOpaque(false);
		panelBoutons.add(this.btnPieceJointe);
		panelBoutons.add(this.btnEnvoyer);
		
		panelSaisie.add(this.txtMessage, BorderLayout.CENTER);
		panelSaisie.add(panelBoutons, BorderLayout.EAST);
		
		panelPrincipal.add(panelSaisie, BorderLayout.CENTER);
		
		this.add(panelPrincipal, BorderLayout.CENTER);

		/*-------------------------------*/
		/*  Activation des composants    */
		/*-------------------------------*/
		this.btnEnvoyer.addActionListener(this);
		this.btnPieceJointe.addActionListener(this);
	}

	/*-----------------------------*/
	/*  Méthode Listener           */
	/*-----------------------------*/
	public void actionPerformed(ActionEvent e) 
	{
		if (e.getSource() == this.btnEnvoyer) 
		{
			String message = this.txtMessage.getText().trim();
		
			if (!message.isEmpty()) 
			{
				// this.controleur.envoyerMessage(message);
				this.txtMessage.setText(""); // Vider le champ après envoi
			}
		}

		if (e.getSource() == this.btnPieceJointe) 
		{
			// this.controleur.ajouterPieceJointe();
		}
	}
}
