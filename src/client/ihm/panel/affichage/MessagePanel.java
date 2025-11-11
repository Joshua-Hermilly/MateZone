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
 * Classe extend JPanel qui représente un message dans le chat.
 * Affiche l'avatar, le pseudo, la date et le contenu du message.
 */
public class MessagePanel extends JPanel 
{
	/*--------------------------*/
	/* Composants               */
	/*--------------------------*/
	private ChatEventDTO event;
	private JPanel       panelContenu;
	private JLabel       lblPhotoProfil;
	private JLabel       lblNomUtilisateur;
	private JLabel       lblDate;
	private JPanel       panelHeader;
	private JTextArea    txtMessage;
	private JPanel       panelPrincipal;

	/*--------------------------*/
	/* Constructeur             */
	/*--------------------------*/
	public MessagePanel(ChatEventDTO event) 
	{
		this.event = event;
		
		this.setLayout(new BorderLayout());
		this.setBackground(new Color(18, 18, 18));

		// Repaint et revalidate au resize
		this.addComponentListener(new java.awt.event.ComponentAdapter() {
			@Override
			public void componentResized(java.awt.event.ComponentEvent e) {
				SwingUtilities.invokeLater(() -> {
					revalidate();
					repaint();
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
	private void creerComposants()
	{
		// Récupération des données
		String pseudo = (String) this.event.getDataIndex(1);
		String contenu = (String) this.event.getDataIndex(2);
		String date = (String) this.event.getDataIndex(3);

		// Conteneur principal
		this.panelContenu = new JPanel(new BorderLayout());
		this.panelContenu.setBackground(new Color(30, 30, 30));
		this.panelContenu.setBorder
		(
			BorderFactory.createCompoundBorder
			(
				BorderFactory.createLineBorder(new Color(50, 50, 50), 1),
				BorderFactory.createEmptyBorder(12, 15, 12, 15)
		));

		// Avatar
		this.creerAvatar();

		// Nom et date
		this.lblNomUtilisateur = new JLabel(pseudo);
		this.lblNomUtilisateur.setFont(new Font("Arial", Font.BOLD, 14));
		this.lblNomUtilisateur.setForeground(new Color(0, 122, 255));

		this.lblDate = new JLabel(date);
		this.lblDate.setFont(new Font("Arial", Font.PLAIN, 11));
		this.lblDate.setForeground(new Color(150, 150, 150));

		this.panelHeader = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
		this.panelHeader.setOpaque(false);
		this.panelHeader.add(this.lblPhotoProfil);
		this.panelHeader.add(this.lblNomUtilisateur);
		this.panelHeader.add(this.lblDate);

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

	private void creerAvatar()
	{
		this.lblPhotoProfil = new JLabel();
		Image avatarImage = null;
		
		try {
			avatarImage = ImageIO.read(new URL("http://localhost:8081/avatar?id=3"));
		} catch (Exception e) {
			System.out.println("Erreur chargement image panel Message");
		}

		if (avatarImage != null) {
			Image scaledImage = avatarImage.getScaledInstance(40, 40, Image.SCALE_SMOOTH);
			BufferedImage circularImage = new BufferedImage(40, 40, BufferedImage.TYPE_INT_ARGB);
			Graphics2D g2d = circularImage.createGraphics();
			g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g2d.setClip(new Ellipse2D.Float(0, 0, 40, 40));
			g2d.drawImage(scaledImage, 0, 0, null);
			g2d.dispose();
			this.lblPhotoProfil.setIcon(new ImageIcon(circularImage));
		}
		this.lblPhotoProfil.setPreferredSize(new Dimension(40, 40));
	}

	/*----------------------------------*/
	/* Positionnement des composants    */
	/*----------------------------------*/
	private void positionnerComposants()
	{
		this.panelPrincipal.add(this.panelHeader, BorderLayout.NORTH);
		this.panelPrincipal.add(this.txtMessage, BorderLayout.CENTER);

		this.panelContenu.add(this.panelPrincipal, BorderLayout.CENTER);
		this.add(this.panelContenu, BorderLayout.CENTER);
		this.add(Box.createVerticalStrut(5), BorderLayout.SOUTH);
	}

	/*----------------------------------*/
	/* Override                         */
	/*----------------------------------*/
	@Override
	public Dimension getMaximumSize() 
	{
		int width = getParent() != null ? getParent().getWidth() : 600;
		return new Dimension(width, super.getPreferredSize().height);
	}
}
