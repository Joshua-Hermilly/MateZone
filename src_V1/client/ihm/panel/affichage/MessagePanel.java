package client.ihm.panel.affichage;

import javax.swing.*;
import java.awt.*;

/**
 * Panel représentant un message individuel dans le chat.
 * Structure en 2 colonnes : marge/photo de profil à gauche, contenu du message
 * à droite.
 */
public class MessagePanel extends JPanel 
{
	/*--------------------------*/
	/* Composants               */
	/*--------------------------*/
	private JPanel panelMarge;
	private JPanel panelContenu;
	private JLabel lblPhotoProfil;
	private JLabel lblNomUtilisateur;
	private JLabel lblDate;
	private JLabel lblMessage;

	/*--------------------------*/
	/* Constructeur             */
	/*--------------------------*/
	public MessagePanel(String nomUtilisateur, String date, String message) 
	{
		this.setLayout(new BorderLayout());

		/*-------------------------------*/
		/* Création des composants       */
		/*-------------------------------*/

		// Panel de marge/photo de profil (colonne gauche)
		this.panelMarge = new JPanel(new FlowLayout(FlowLayout.CENTER));
		this.panelMarge.setPreferredSize(new Dimension(60, 0));
		this.panelMarge.setBackground(new Color(54, 57, 63));

		// Photo de profil temporaire (sera remplacée plus tard)
		this.lblPhotoProfil = new JLabel("👤");
		this.lblPhotoProfil.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 24));
		this.lblPhotoProfil.setForeground(new Color(114, 118, 125));
		this.lblPhotoProfil.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

		// Panel de contenu (colonne droite)
		this.panelContenu = new JPanel();
		this.panelContenu.setLayout(new BoxLayout(this.panelContenu, BoxLayout.Y_AXIS));
		this.panelContenu.setBackground(new Color(54, 57, 63));
		this.panelContenu.setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 10));

		// Nom d'utilisateur
		this.lblNomUtilisateur = new JLabel(nomUtilisateur);
		this.lblNomUtilisateur.setFont(new Font("Arial", Font.BOLD, 13));
		this.lblNomUtilisateur.setForeground(new Color(220, 221, 222));
		this.lblNomUtilisateur.setAlignmentX(Component.LEFT_ALIGNMENT);

		// Date/heure
		this.lblDate = new JLabel(date);
		this.lblDate.setFont(new Font("Arial", Font.PLAIN, 11));
		this.lblDate.setForeground(new Color(163, 166, 170));
		this.lblDate.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));

		// Message
		this.lblMessage = new JLabel("<html><div style='width: 400px;'>" + message + "</div></html>");
		this.lblMessage.setFont(new Font("Arial", Font.PLAIN, 12));
		this.lblMessage.setForeground(new Color(220, 221, 222));
		this.lblMessage.setBorder(BorderFactory.createEmptyBorder(3, 0, 0, 0));
		this.lblMessage.setAlignmentX(Component.LEFT_ALIGNMENT);

		/*-------------------------------*/
		/* Panel en-tête (nom + date)    */
		/*-------------------------------*/
		JPanel panelEnTete = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
		panelEnTete.setBackground(new Color(54, 57, 63));
		panelEnTete.add(this.lblNomUtilisateur);
		panelEnTete.add(this.lblDate);
		panelEnTete.setAlignmentX(Component.LEFT_ALIGNMENT);

		/*-------------------------------*/
		/* Positionnement des composants */
		/*-------------------------------*/

		// Ajout dans le panel de marge
		this.panelMarge.add(this.lblPhotoProfil);

		// Ajout dans le panel de contenu
		this.panelContenu.add(panelEnTete);
		this.panelContenu.add(this.lblMessage);

		// Ajout des panels principaux
		this.add(this.panelMarge, BorderLayout.WEST);
		this.add(this.panelContenu, BorderLayout.CENTER);

		// Style général du panel
		this.setBackground(new Color(54, 57, 63));
		this.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(47, 49, 54)),
				BorderFactory.createEmptyBorder(2, 5, 2, 5)));

		this.setMaximumSize(new Dimension(Integer.MAX_VALUE, this.getPreferredSize().height));
	}
}
