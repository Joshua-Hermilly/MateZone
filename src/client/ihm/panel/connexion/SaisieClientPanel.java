package client.ihm.panel.connexion;

import client.ihm.IhmGui;
import client.ihm.frame.connexion.ConnectionFrame;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

/**
 * Panel réutilisable pour la saisie nom/mdp et le bouton de connexion.
 * 	
*/
public class SaisieClientPanel extends JPanel implements ActionListener
{
	/*--------------------------*/
	/*  Composants              */
	/*--------------------------*/
	private ConnectionFrame  frame;

	private JTextField     txtNom;
	private JTextField txtMdp;
	private JButton        btnConnect;
	private JButton        btnEnregister;
	
	/*--------------------------*/
	/*     Constructeur         */
	/*--------------------------*/
	public SaisieClientPanel( ConnectionFrame frame ) 
	{
		this.setLayout(new GridLayout(4, 2));

		this.frame = frame;

		/*-------------------------------*/
		/* Création des composants       */
		/*-------------------------------*/
		this.txtNom        = new JTextField();
		this.txtMdp        = new JTextField();
		this.btnConnect    = new JButton("Se connecter");
		this.btnEnregister = new JButton("S'enregistrer");

		/*-------------------------------*/
		/* Ajout des composants          */
		/*-------------------------------*/
		this.add(new JLabel("Pseudo:"));
		this.add(this.txtNom);

		this.add(new JLabel("Mdp:"));
		this.add(this.txtMdp);

		// case vide pour alignement + bouton
		this.add(this.btnConnect);
		this.add(this.btnEnregister);

		/*-------------------------------*/
		/* Activation des composants     */
		/*-------------------------------*/
		this.btnConnect.addActionListener(this);
		this.btnEnregister.addActionListener(this);

	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		String nom = this.txtNom.getText();
		String mdp = this.txtMdp.getText();

		// Vérification des informations de connexion
		if ( nom.isEmpty() || mdp.isEmpty() )
		{
			this.frame.afficherMessage( "Veuillez remplir tous les champs.\n" );
			return;
		}

		if      ( e.getSource() == this.btnConnect    ) { this.frame.connexionAuClient( nom, mdp); }
		else if ( e.getSource() == this.btnEnregister ) { this.frame.enregistrerClient( nom, mdp); }

		System.out.println("Action performed: " + e.getActionCommand() );
	}
}
