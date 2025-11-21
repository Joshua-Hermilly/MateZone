package client.ihm.panel.affichage;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.net.URL;
import common.dto.ChatEventDTO;

/*-------------------------------*/
/* Class MessagePanel            */
/*-------------------------------*/
/**
 * Panneau de message qui étend JPanel et représente un message individuel dans
 * le chat.
 * Affiche l'avatar circulaire de l'utilisateur, son pseudonyme, la date d'envoi
 * et le contenu du message.
 * Le panneau s'adapte automatiquement à la largeur disponible et gère le retour
 * à la ligne du texte.
 * Il utilise un thème sombre cohérent avec le reste de l'application.
 *
 * @author Joshua Hermilly
 * @version V1
 * @date 08/11/25
 */
public class MessagePanel extends JPanel
{
	/*--------------------------*/
	/* Composants               */
	/*--------------------------*/
	/**
	 * Événement de chat contenant les données du message à afficher.
	 */
	private ChatEventDTO event;

	/**
	 * Adresse pour les images
	 */
	private String adresse;

	/**
	 * Panneau principal contenant tous les éléments du message.
	 */
	private JPanel panelContenu;

	/**
	 * Étiquette affichant l'avatar circulaire de l'utilisateur.
	 */
	private JLabel lblPhotoProfil;

	/**
	 * Étiquette affichant le pseudonyme de l'utilisateur.
	 */
	private JLabel lblNomUtilisateur;

	/**
	 * Étiquette affichant la date et l'heure d'envoi du message.
	 */
	private JLabel lblDate;

	/**
	 * Panneau d'en-tête contenant l'avatar, le pseudo et la date.
	 */
	private JPanel panelHeader;

	/**
	 * Zone de texte affichant le contenu du message avec retour à la ligne
	 * automatique.
	 */
	private JTextArea txtMessage;

	/**
	 * Panneau principal organisant l'en-tête et le contenu du message.
	 */
	private JPanel panelPrincipal;

	/*--------------------------*/
	/* Constructeur             */
	/*--------------------------*/
	/**
	 * Constructeur du panneau de message.
	 * Initialise l'interface du message avec les données fournies dans l'événement
	 * de chat.
	 * Configure le thème sombre et ajoute un écouteur pour le redimensionnement
	 * automatique.
	 * 
	 * @param event l'événement de chat contenant les données du message à afficher
	 */
	public MessagePanel(ChatEventDTO event, String adrresse )
	{
		this.event   = event;
		this.adresse = adrresse;

		this.setLayout(new BorderLayout());
		this.setBackground(new Color(18, 18, 18));

		/*-------------------------------*/
		/* Écouteur de redimensionnement */
		/*-------------------------------*/
		// Ajoute un écouteur pour gérer le redimensionnement automatique du panneau
		// Permet de recalculer et redessiner les composants lors d'un changement de taille
		this.addComponentListener(new java.awt.event.ComponentAdapter()
		{
			/**
			 * Méthode appelée automatiquement lors du redimensionnement du composant.
			 * Relance la validation et le repaint des composants de manière asynchrone
			 * pour éviter les conflits avec le thread de l'interface graphique.
			 * 
			 * @param e l'événement de redimensionnement
			 */
			@Override
			public void componentResized(java.awt.event.ComponentEvent e) 
			{
				// Exécution asynchrone pour éviter les problèmes de thread EDT
				SwingUtilities.invokeLater(() -> {
					revalidate(); // Recalcule la disposition des composants
					repaint();    // Redessine le panneau avec les nouvelles dimensions
				});
			}
		});

		/*-------------------------------*/
		/* Création des composants       */
		/*-------------------------------*/
		this.creerComposants();

		/*-------------------------------*/
		/* Positionnement des composants */
		/*-------------------------------*/
		this.positionnerComposants();
	}

	/*--------------------------*/
	/* Création des composants  */
	/*--------------------------*/
	/**
	 * Crée et configure tous les composants graphiques du message.
	 * Extrait les données de l'événement de chat et initialise l'avatar, les
	 * étiquettes et la zone de texte.
	 */
	private void creerComposants() 
	{
		// Récupération des données
		String pseudo  = (String) this.event.getData().get("pseudo");
		String contenu = (String) this.event.getData().get("contenu");
		String date    = (String) this.event.getData().get("date");

		// Conteneur principal
		this.panelContenu = new JPanel(new BorderLayout());
		this.panelContenu.setBackground(new Color(30, 30, 30));
		this.panelContenu.setBorder
		(
			BorderFactory.createCompoundBorder
			(
				BorderFactory.createLineBorder (new Color(50, 50, 50), 1),
				BorderFactory.createEmptyBorder(12, 15, 12, 15          )
			)
		);

		// Avatar
		this.creerAvatar();

		/// Nom et date
		this.lblNomUtilisateur = new JLabel(pseudo);
		this.lblNomUtilisateur.setFont(new Font("Arial", Font.BOLD, 14));
		this.lblNomUtilisateur.setForeground(new Color(0, 122, 255));

		this.lblDate = new JLabel(date);
		this.lblDate.setFont(new Font("Arial", Font.PLAIN, 11));
		this.lblDate.setForeground(new Color(150, 150, 150));

		this.panelHeader = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
		this.panelHeader.setOpaque(false);
		this.panelHeader.add(this.lblPhotoProfil   );
		this.panelHeader.add(this.lblNomUtilisateur);
		this.panelHeader.add(this.lblDate          );

		// Message
		this.txtMessage = new JTextArea(contenu) 
		{
			@Override
			public Dimension getPreferredSize() 
			{
				int width = getParent() != null ? getParent().getWidth() - 60 : 300;
				setSize(width, Short.MAX_VALUE);
				return super.getPreferredSize();
			}
		};
		this.txtMessage.setWrapStyleWord(true);
		this.txtMessage.setLineWrap(true);
		this.txtMessage.setOpaque(false);
		this.txtMessage.setEditable(false);
		this.txtMessage.setFont(new Font("Arial", Font.PLAIN, 14));
		this.txtMessage.setForeground(Color.WHITE);
		this.txtMessage.setBorder(BorderFactory.createEmptyBorder(5, 50, 0, 0));

		// Panel principal
		this.panelPrincipal = new JPanel(new BorderLayout());
		this.panelPrincipal.setOpaque(false);
	}

	/**
	 * Crée l'avatar circulaire de l'utilisateur.
	 * Charge l'image depuis une URL et la transforme en forme circulaire avec
	 * anti-aliasing.
	 * En cas d'échec de chargement, l'avatar reste vide.
	 */
	private void creerAvatar()
	{
		this.lblPhotoProfil = new JLabel();
		Image avatarImage   = null;
		String id    = (String)this.event.getData().get("idClient");

		try 
		{
			avatarImage = ImageIO.read(new URL("http://"+this.adresse+"/avatar?id="+id));

		} catch (Exception e) { System.out.println("Erreur chargement image panel Message"); }

		if (avatarImage != null) {
			Image         scaledImage   = avatarImage.getScaledInstance(40, 40, Image.SCALE_SMOOTH);
			BufferedImage circularImage = new BufferedImage(40, 40, BufferedImage.TYPE_INT_ARGB);
			Graphics2D    g2d           = circularImage.createGraphics();
			g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g2d.setClip(new Ellipse2D.Float(0, 0, 40, 40));
			g2d.drawImage(scaledImage, 0, 0, null);
			g2d.dispose();
			this.lblPhotoProfil.setIcon(new ImageIcon(circularImage));
		}
		this.lblPhotoProfil.setPreferredSize(new Dimension(40, 40));
	}

	/*----------------------------------*/
	/* Positionnement des composants   */
	/*----------------------------------*/
	/**
	 * Positionne et organise tous les composants dans le panneau.
	 * Configure la hiérarchie des conteneurs et ajoute des espacements appropriés.
	 */
	private void positionnerComposants()
	{
		this.panelPrincipal.add(this.panelHeader  , BorderLayout.NORTH );
		this.panelPrincipal.add(this.txtMessage   , BorderLayout.CENTER);

		this.panelContenu.add(this.panelPrincipal , BorderLayout.CENTER);
		this.add(this.panelContenu                , BorderLayout.CENTER);
		this.add(Box.createVerticalStrut(5), BorderLayout.SOUTH);
	}

	/*----------------------------------*/
	/* Override */
	/*----------------------------------*/
	/**
	 * Redéfinit la taille maximale du panneau pour s'adapter à la largeur du
	 * parent.
	 * Permet un redimensionnement dynamique tout en conservant la hauteur préférée.
	 * 
	 * @return la dimension maximale calculée selon la largeur du parent
	 */
	@Override
	public Dimension getMaximumSize() 
	{
		int width = getParent() != null ? getParent().getWidth() : 600;
		return new Dimension(width, super.getPreferredSize().height);
	}
}
