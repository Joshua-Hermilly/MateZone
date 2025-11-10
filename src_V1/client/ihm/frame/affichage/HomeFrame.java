package client.ihm.frame.affichage;

import javax.swing.*;

import client.ihm.IhmGui;
import client.ihm.panel.affichage.ChatPanel;
import client.ihm.panel.affichage.EnvoyerPanel;

import java.awt.*;

/*-------------------------------*/
/* HomeFrame                     */
/*-------------------------------*/
/**
 * Fenêtre principale de l'application (écran d'accueil) similaire à Discord.
 * Point de départ après la connexion réussie.
 */
public class HomeFrame extends JFrame 
{
	/*--------------------------*/
	/* Attributs                */
	/*--------------------------*/
	private IhmGui ihmGui;
	private String pseudo;

	private ChatPanel    chatPanel;
	private EnvoyerPanel envoyerPanel;

	/*--------------------------*/
	/* Constructeur             */
	/*--------------------------*/
	public HomeFrame(IhmGui ihmGui, String pseudo) 
	{
		this.ihmGui = ihmGui;
		this.pseudo = pseudo;

		this.setTitle("MateZone - " + pseudo);
		this.setSize(800, 600);
		this.setLayout(new BorderLayout());

		/*-------------------------------*/
		/* Création des composants       */
		/*-------------------------------*/
		this.chatPanel    = new ChatPanel   ();
		this.envoyerPanel = new EnvoyerPanel( this );

		/*-------------------------------*/
		/* Positionnement des composants */
		/*-------------------------------*/
		this.add(this.chatPanel   , BorderLayout.CENTER);
		this.add(this.envoyerPanel, BorderLayout.SOUTH );

		/*-------------------------------*/
		/* Configuration de la fenêtre   */
		/*-------------------------------*/
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}

	/*--------------------------*/
	/* Méthodes                 */
	/*--------------------------*/
	public void envoyerMessage          ( String message )  { this.ihmGui.envoyerMessage        ( message ); }

	public void   mettreAJourLstMessages(String messages  ) { this.chatPanel.afficherLstMessages(messages);   }
	public void   ajouterNvMessage      (String message   ) { this.chatPanel.ajouterNvMessage   (message);    }
	public void   changerChannel        (String nomChannel) { this.chatPanel.changerTitreChannel(nomChannel); }
	public String getPseudo             ()                  { return this.pseudo;                             }
}
