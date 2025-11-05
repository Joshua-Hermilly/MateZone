package client.ihm.panel.connexion;

import client.ihm.IhmGui;
import client.ihm.frame.connexion.ConnectionFrame;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

/**
 * Panel réutilisable pour la saisie host/port et le bouton de connexion.
 */
public class SaisiePanel extends JPanel implements ActionListener
{
	/*--------------------------*/
	/*  Composants              */
	/*--------------------------*/
	private ConnectionFrame  frame;

    private JTextField       txtHost;
    private JTextField       txtPort;
    private JButton          btnConnect;
	
	/*--------------------------*/
	/*     Constructeur         */
	/*--------------------------*/
    public SaisiePanel( ConnectionFrame frame ) 
	{
        this.setLayout(new GridLayout(3, 2));

		this.frame = frame;

		/*-------------------------------*/
		/* Création des composants       */
		/*-------------------------------*/
        this.txtHost    = new JTextField();
        this.txtPort    = new JTextField();
        this.btnConnect = new JButton("Se connecter");

        // Valeurs par défaut utiles
        this.txtHost.setText("localhost");
        this.txtPort.setText("8080");

		/*-------------------------------*/
		/* Ajout des composants          */
		/*-------------------------------*/
        this.add(new JLabel("Host:"));
        this.add(this.txtHost);

        this.add(new JLabel("Port:"));
        this.add(this.txtPort);

        // case vide pour alignement + bouton
        this.add(new JLabel());
        this.add(this.btnConnect);

		/*-------------------------------*/
		/* Activation des composants     */
		/*-------------------------------*/
		this.btnConnect.addActionListener(this);

	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		String host = this.txtHost.getText();
		String port = this.txtPort.getText();

		try {
			int portNum = Integer.parseInt(port);

			boolean success = this.frame.testerConnexionAuServeur(host, portNum);

			if (success) { this.frame.afficherMessage( "Connexion réussie !\n"   ); }
			else         { this.frame.afficherMessage( "Échec de la connexion.\n"); }

		} catch (Exception ex) { this.frame.afficherMessage("Erreur : " + ex.getMessage() + "\n"); }
	}
}
