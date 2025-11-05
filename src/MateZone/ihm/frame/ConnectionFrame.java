package MateZone.ihm.frame;

import javax.swing.*;

import MateZone.Controleur;

import MateZone.metier.serveur.TestConnexion;

import java.awt.*;
import java.awt.event.*;

/*-------------------------------*/
/* ConnectionFrame               */
/*-------------------------------*/
/**
 * Fenêtre de connexion en package par défaut pour permettre l'accès au
 * contrôleur qui se trouve lui aussi dans le package par défaut.
 */
public class ConnectionFrame extends JFrame implements ActionListener
{
	private Controleur controleur;

	private JTextField txtHost;
	private JTextField txtPort;
	private JButton    btnConnect;
	private JTextArea  txtOutput;

	public ConnectionFrame(Controleur controleur)
	{
		this.controleur = controleur;

		setTitle("Connection Frame");
		setSize(400, 300);
		setLayout(new BorderLayout());

		JPanel panelSaisie = new JPanel();
		panelSaisie.setLayout(new GridLayout(3, 2));

		this.txtHost = new JTextField();
		this.txtPort = new JTextField();

		// Valeurs par défaut pour aider l'utilisateur
		this.txtHost.setText("localhost");
		this.txtPort.setText("8080");
		this.btnConnect = new JButton("Se connecter");
		this.txtOutput = new JTextArea();
		this.txtOutput.setEditable(false);

		panelSaisie.add(new JLabel("Host:"));
		panelSaisie.add(this.txtHost);

		panelSaisie.add(new JLabel("Port:"));
		panelSaisie.add(this.txtPort);

		panelSaisie.add(this.btnConnect);

		this.add(panelSaisie, BorderLayout.NORTH);
		this.add(new JScrollPane(this.txtOutput), BorderLayout.CENTER);

		this.btnConnect.addActionListener(this);

		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);

		this.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		String host = this.txtHost.getText();
		String port = this.txtPort.getText();

		try
		{
			TestConnexion test = new TestConnexion();
			if (test.tentativeConnexion(host, Integer.parseInt(port)))
			{
				this.txtOutput.append("Connexion réussie !\n");

				this.controleur = new Controleur(host, Integer.parseInt(port));
				// Lancer une tentative de connexion via le contrôleur (évite la variable inutilisée)
				boolean ok = this.controleur.ConnexionServeur(host, Integer.parseInt(port));
				if (ok) { this.txtOutput.append("Session client initialisée par le contrôleur.\n"); }

				this.dispose();
			}
			else { this.txtOutput.append("Échec de la connexion.\n"); }
		}
		catch (Exception ex) { this.txtOutput.append("Erreur : " + ex.getMessage() + "\n"); }
	}
}
