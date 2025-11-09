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
	public MessagePanel( ChatEventDTO message )
	{
		this.setLayout( new BorderLayout() );

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
		
		
	}

}

//pseudo, date, img, contenue, 