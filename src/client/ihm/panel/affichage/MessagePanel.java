package client.ihm.panel.affichage;

import javax.swing.*;

import common.dto.ChatEventDTO;

import java.awt.*;
/*-------------------------------*/
/* Class MessagePanel            */
/*-------------------------------*/
/**
 * Classe extend JPanel qui fait office de panel des message (...images ??).
 * La frame est faite avec le client/ihm/frame/affichage/MateZoneFrame
 */
public class MessagePanel extends JPanel
{
	/*--------------------------*/
	/*       Constructeur       */
	/*--------------------------*/
	public MessagePanel( ChatEventDTO event )
	{
		this.setLayout( new BorderLayout() );
		this.setBackground(new Color(18, 18, 18));

		//Composants
		JPanel panelMarge;
		JPanel panelContenu;
	
		JLabel lblPhotoProfil;
		JLabel lblNomUtilisateur;
		JLabel lblDate;
		JLabel lblMessage;

		/*-------------------------------*/
		/* Création des composants       */
		/*-------------------------------*/
		panelMarge = new JPanel();
		panelMarge.setOpaque(false);
		
		panelContenu = new JPanel(new BorderLayout());
		panelContenu.setBackground(new Color(30, 30, 30));
		panelContenu.setBorder
		(
			BorderFactory.createCompoundBorder
			(
				BorderFactory.createLineBorder (new Color(50, 50, 50), 1),
				BorderFactory.createEmptyBorder(12, 15, 12, 15)
			)
		);

		// Récupération des données du message
		String pseudo  = (String) event.getData().get("pseudo" );
		String contenu = (String) event.getData().get("contenu");
		String date    = (String) event.getData().get("date"   );
		String img     = (String) event.getData().get("img"    );

		// Photo de profil (placeholder)
		lblPhotoProfil = new JLabel("👤");
		lblPhotoProfil.setFont(new Font("Arial", Font.PLAIN, 24));
		lblPhotoProfil.setForeground(new Color(150, 150, 150));
		lblPhotoProfil.setHorizontalAlignment(SwingConstants.CENTER);
		lblPhotoProfil.setPreferredSize(new Dimension(40, 40));
		lblPhotoProfil.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 10));

		// Nom utilisateur
		lblNomUtilisateur = new JLabel(pseudo);
		lblNomUtilisateur.setFont(new Font("Arial", Font.BOLD, 14));
		lblNomUtilisateur.setForeground(new Color(0, 122, 255));

		// Date
		lblDate = new JLabel(date);
		lblDate.setFont(new Font("Arial", Font.PLAIN, 11));
		lblDate.setForeground(new Color(150, 150, 150));

		// Message
		lblMessage = new JLabel("<html><div style='width:300px'>" + contenu + "</div></html>");
		lblMessage.setFont(new Font("Arial", Font.PLAIN, 14));
		lblMessage.setForeground(Color.WHITE);
		lblMessage.setVerticalAlignment(SwingConstants.TOP);

		/*-------------------------------*/
		/* Positionnement des composants */
		/*-------------------------------*/
		JPanel panelHeader = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
		panelHeader.setOpaque(false);
		panelHeader.add( lblNomUtilisateur             );
		panelHeader.add( Box.createHorizontalStrut(10) );
		panelHeader.add( lblDate                       );

		JPanel panelTexte = new JPanel(new BorderLayout());
		panelTexte.setOpaque(false);
		panelTexte.add( panelHeader, BorderLayout.NORTH  );
		panelTexte.add( lblMessage , BorderLayout.CENTER );

		panelContenu.add( lblPhotoProfil, BorderLayout.WEST   );
		panelContenu.add( panelTexte    , BorderLayout.CENTER );

		this.add( panelContenu              , BorderLayout.CENTER );
		this.add( Box.createVerticalStrut(5), BorderLayout.SOUTH  );
	}
}
