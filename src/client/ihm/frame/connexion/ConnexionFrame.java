package client.ihm.frame.connexion;

import java.awt.Toolkit;
import javax.swing.JFrame;

import client.controleur.Controleur;
import client.ihm.panel.connexion.ConnexionPanel;

/*-------------------------------*/
/* Class ConnexionFrame          */
/*-------------------------------*/
/**
 * Fenêtre de connexion qui étend JFrame et sert d'interface principale pour
 * l'authentification.
 * Cette fenêtre contient le panneau ConnexionPanel et configure l'apparence
 * générale de la fenêtre.
 * Elle est affichée au lancement de l'application pour permettre à
 * l'utilisateur de se connecter ou s'enregistrer.
 *
 * @author Joshua Hermilly
 * @version V1
 * @date 08/11/25
 */
public class ConnexionFrame extends JFrame
{
	/*--------------------------*/
	/* Attributs                */
	/*--------------------------*/
	/**
	 * Panneau de connexion contenant les éléments d'interface utilisateur.
	 * Gère la saisie des identifiants et les boutons d'action.
	 */
	private ConnexionPanel panelConnexion;

	/*--------------------------*/
	/* Constructeur             */
	/*--------------------------*/
	/**
	 * Constructeur de la fenêtre de connexion.
	 * Configure les propriétés de la fenêtre (titre, icône, taille) et initialise
	 * le panneau de connexion.
	 * La fenêtre est centrée sur l'écran et non redimensionnable.
	 * 
	 * @param controleur le contrôleur principal de l'application
	 */
	public ConnexionFrame(Controleur controleur) 
	{
		this.setTitle("Connexion");
		this.setResizable(false);
		this.setIconImage(Toolkit.getDefaultToolkit().getImage("src/client/img/MateZone.png"));

		/*-------------------------------*/
		/* Création des composants       */
		/*-------------------------------*/
		this.panelConnexion = new ConnexionPanel(controleur);

		/*-------------------------------*/
		/* Positionnement des composants */
		/*-------------------------------*/
		this.add(this.panelConnexion);

		this.pack();
		this.setLocationRelativeTo(null);
	}
}
