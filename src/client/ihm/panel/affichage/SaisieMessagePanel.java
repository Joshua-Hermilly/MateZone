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
 * Panel de saisie des messages (champ + bouton envoyer + pièce jointe).
 * Envoie sur Enter ou bouton Envoyer.
 */
public class SaisieMessagePanel extends JPanel implements ActionListener 
{
	/*--------------------------*/
	/* Composants               */
	/*--------------------------*/
	private final Controleur controleur;
	private JTextField txtMessage;
	private JButton    btnEnvoyer;
	private JButton    btnPieceJointe;
	private JPanel     panelPrincipal;
	private JPanel     panelSaisie;
	private JPanel     panelBoutons;

	/*--------------------------*/
	/* Constructeur             */
	/*--------------------------*/
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
	/* Création des composants  */
	/*--------------------------*/
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
	private void positionnerComposants()
	{
		this.panelBoutons.add(this.btnPieceJointe);
		this.panelBoutons.add(this.btnEnvoyer);

		this.panelSaisie.add(this.txtMessage, BorderLayout.CENTER);
		this.panelSaisie.add(this.panelBoutons, BorderLayout.EAST);

		this.panelPrincipal.add(this.panelSaisie, BorderLayout.CENTER);

		this.add(this.panelPrincipal, BorderLayout.CENTER);
	}

	/*----------------------------------*/
	/* Ajout des listeners              */
	/*----------------------------------*/
	private void ajouterListeners()
	{
		this.btnEnvoyer.addActionListener(this);
		this.btnPieceJointe.addActionListener(this);
		this.txtMessage.addActionListener(e -> sendMessage());
	}

	/*----------------------------------*/
	/* Override                         */
	/*----------------------------------*/
	@Override
	public void actionPerformed(ActionEvent e) 
	{
		if (e.getSource() == this.btnEnvoyer) 
		{
			sendMessage();

		} else if (e.getSource() == this.btnPieceJointe) 
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
