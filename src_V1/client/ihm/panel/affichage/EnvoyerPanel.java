package client.ihm.panel.affichage;

import javax.swing.*;

import client.ihm.IhmGui;
import client.ihm.frame.affichage.HomeFrame;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Panel de saisie et d'envoi de messages pour l'application MateZone.
 * Contient une zone de saisie, un bouton d'envoi et un bouton pour les images.
 * Version statique pour l'interface utilisateur.
 */
public class EnvoyerPanel extends JPanel implements ActionListener
{
	/*--------------------------*/
	/* Composants               */
	/*--------------------------*/
	private HomeFrame  frame;

	private JTextField txtSaisieMessage;
	private JButton    btnEnvoyer;
	private JButton    btnImage;
	private JPanel     panelSaisie;
	private JPanel     panelBoutons;

	/*--------------------------*/
	/* Constructeur             */
	/*--------------------------*/
	public EnvoyerPanel( HomeFrame frame ) 
	{
		this.setLayout(new BorderLayout());

		this.frame = frame;
		/*-------------------------------*/
		/* Création des composants       */
		/*-------------------------------*/

		// Zone de saisie du message
		this.txtSaisieMessage = new JTextField();
		this.txtSaisieMessage.setFont(new Font("Arial", Font.PLAIN, 14));
		this.txtSaisieMessage.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(new Color(114, 118, 125), 1),
				BorderFactory.createEmptyBorder(8, 12, 8, 12)));
		this.txtSaisieMessage.setBackground(new Color(64, 68, 75));
		this.txtSaisieMessage.setForeground(Color.WHITE);
		this.txtSaisieMessage.setCaretColor(Color.WHITE);

		// Placeholder text (sera remplacé par du vrai texte lors de la saisie)
		this.txtSaisieMessage.setText("");
		this.txtSaisieMessage.setForeground(new Color(163, 166, 170));

		// Bouton d'envoi
		this.btnEnvoyer = new JButton("Envoyer");
		this.btnEnvoyer.setFont(new Font("Arial", Font.BOLD, 12));
		this.btnEnvoyer.setBackground(new Color(88, 101, 242));
		this.btnEnvoyer.setForeground(Color.WHITE);
		this.btnEnvoyer.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
		this.btnEnvoyer.setFocusPainted(false);
		this.btnEnvoyer.setCursor(new Cursor(Cursor.HAND_CURSOR));

		// Bouton pour envoyer une image
		this.btnImage = new JButton("📎");
		this.btnImage.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 16));
		this.btnImage.setBackground(new Color(64, 68, 75));
		this.btnImage.setForeground(new Color(163, 166, 170));
		this.btnImage.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
		this.btnImage.setFocusPainted(false);
		this.btnImage.setCursor(new Cursor(Cursor.HAND_CURSOR));
		this.btnImage.setToolTipText("Joindre une image");
		this.btnImage.setPreferredSize(new Dimension(45, 0));

		/*-------------------------------*/
		/* Panels de regroupement        */
		/*-------------------------------*/

		// Panel principal de saisie (contient le champ de texte et le bouton image)
		this.panelSaisie = new JPanel(new BorderLayout());
		this.panelSaisie.setBackground(new Color(54, 57, 63));
		this.panelSaisie.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

		// Panel des boutons (contient le bouton d'envoi)
		this.panelBoutons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
		this.panelBoutons.setBackground(new Color(54, 57, 63));

		/*-------------------------------*/
		/* Positionnement des composants */
		/*-------------------------------*/

		// Ajout dans le panel de saisie
		this.panelSaisie.add(this.btnImage, BorderLayout.WEST);
		this.panelSaisie.add(this.txtSaisieMessage, BorderLayout.CENTER);

		// Ajout dans le panel des boutons
		this.panelBoutons.add(this.btnEnvoyer);

		// Ajout des panels principaux
		this.add(this.panelSaisie, BorderLayout.CENTER);
		this.add(this.panelBoutons, BorderLayout.EAST);

		// Style général du panel
		this.setBackground(new Color(54, 57, 63));
		this.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(47, 49, 54)),
				BorderFactory.createEmptyBorder(10, 15, 10, 15)));
		this.setPreferredSize(new Dimension(0, 70));

		/*-------------------------------*/
		/* Ajout des listeners           */
		/*-------------------------------*/
		this.btnEnvoyer.addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		if ( e.getSource() == this.btnEnvoyer )
		{
			this.frame.envoyerMessage( this.txtSaisieMessage.getText() );
			this.viderSaisie();
		}
	}


	/*--------------------------*/
	/* Méthodes                 */
	/*--------------------------*/

	public void viderSaisie() { this.txtSaisieMessage.setText(""); }

	public void setBoutonImageActif(boolean actif) {
		this.btnImage.setEnabled(actif);
		if (actif)  { this.btnImage.setForeground(new Color(163, 166, 170)); } 
		else        { this.btnImage.setForeground(new Color( 79,  84,  92)); }
	}
}
