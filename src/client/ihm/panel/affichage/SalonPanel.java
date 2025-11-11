package client.ihm.panel.affichage;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import common.dto.ChatEventDTO;

/*-------------------------------*/
/* Class SalonPanel              */
/*-------------------------------*/
/**
 * Panneau de salon qui étend JPanel et gère l'affichage des messages du chat.
 * Cette classe crée et gère les MessagePanel pour afficher les messages reçus.
 * Le panneau inclut un système de défilement automatique et un en-tête avec le
 * nom du canal.
 * Il est utilisé dans la MateZoneFrame pour afficher la conversation.
 *
 * @author Joshua Hermilly
 * @version V1
 * @date 08/11/25
 */
public class SalonPanel extends JPanel 
{
	/*--------------------------*/
	/* Composants               */
	/*--------------------------*/
	/**
	 * Nom du canal de chat affiché dans l'en-tête du panneau.
	 */
	private String channelName;

	/**
	 * Liste des panneaux de messages affichés dans le salon.
	 * Maintient l'ordre d'affichage des messages.
	 */
	private List<MessagePanel> lstMsgPanel;

	/**
	 * Conteneur principal qui organise verticalement tous les messages.
	 */
	private JPanel messagesContainer;

	/**
	 * Panneau de défilement qui encapsule le conteneur de messages.
	 * Permet de naviguer dans l'historique des messages.
	 */
	private JScrollPane scrollPane;

	/**
	 * Étiquette affichant le nom du canal de chat dans l'en-tête.
	 */
	private JLabel lblChannelName;

	/*--------------------------*/
	/* Constructeur             */
	/*--------------------------*/
	/**
	 * Constructeur du panneau de salon.
	 * Initialise l'interface avec un thème sombre, configure le conteneur de
	 * messages
	 * avec défilement automatique et affiche le nom du canal.
	 * 
	 * @param channelName le nom du canal de chat à afficher
	 */
	public SalonPanel(String channelName) 
	{
		this.channelName = channelName;
		this.lstMsgPanel = new ArrayList<MessagePanel>();

		this.setLayout(new BorderLayout());
		this.setBackground(new Color(18, 18, 18));

		/*-------------------------------*/
		/* Création des composants       */
		/*-------------------------------*/
		// Nom du channel
		this.lblChannelName = new JLabel("# " + this.channelName);
		this.lblChannelName.setFont(new Font("Arial", Font.BOLD, 16));
		this.lblChannelName.setForeground(Color.WHITE);
		this.lblChannelName.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

		// Container pour les messages
		this.messagesContainer = new JPanel();
		this.messagesContainer.setLayout(new BoxLayout(this.messagesContainer, BoxLayout.Y_AXIS));
		this.messagesContainer.setBackground(new Color(18, 18, 18));

		// ScrollPane pour les messages
		this.scrollPane = new JScrollPane(this.messagesContainer);
		this.scrollPane.setVerticalScrollBarPolicy  (JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		this.scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER  );
		this.scrollPane.setBorder(null);
		this.scrollPane.getViewport().setBackground(new Color(18, 18, 18));

		/*-------------------------------*/
		/* Positionnement des composants */
		/*-------------------------------*/
		this.add(this.lblChannelName, BorderLayout.NORTH );
		this.add(this.scrollPane    , BorderLayout.CENTER);
	}

	/*----------------------------------*/
	/* Gestion Ajout et Suppresion Msg  */
	/*----------------------------------*/
	/**
	 * Ajoute une liste de messages au salon de chat.
	 * Parcourt la liste des événements et ajoute chaque message individuellement.
	 * 
	 * @param event l'événement contenant la liste des messages à ajouter
	 */
	public void addLstMessage(ChatEventDTO event) 
	{
		List<ChatEventDTO> lstEvent = event.getLstEventDTO();

		for (ChatEventDTO chatEventDTO : lstEvent)
			this.addMessage(chatEventDTO);
	}

	/**
	 * Ajoute un nouveau message au salon de chat.
	 * Crée un MessagePanel pour le message, l'ajoute à l'interface et effectue
	 * un défilement automatique vers le bas pour afficher le nouveau message.
	 * 
	 * @param event l'événement contenant les données du message à ajouter
	 */
	public void addMessage(ChatEventDTO event)
	{
		MessagePanel messagePanel = new MessagePanel(event);

		// Aligneme gauche
		messagePanel.setAlignmentX(Component.LEFT_ALIGNMENT);
		
		// Configuration de la taille du panneau
		Dimension pref = messagePanel.getPreferredSize();
		if (pref != null) 
		{
			// Permet au panneau de s'étendre horizontalement tout en gardant sa hauteur préférée
			messagePanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, pref.height));
		}

		// Ajoute à la lst
		this.lstMsgPanel      .add(messagePanel);
		this.messagesContainer.add(messagePanel);

		// Auto-scroll vers le bas pour afficher automatiquement le nouveau message
		SwingUtilities.invokeLater(() -> {
			JScrollBar vertical = this.scrollPane.getVerticalScrollBar();
			vertical.setValue(vertical.getMaximum());
		});

		// Mise à jour de l'affichage
		this.revalidate();
		this.repaint();
	}

	/**
	 * Supprime tous les messages du salon de chat.
	 * Vide la liste des panneaux de messages et rafraîchit l'interface.
	 */
	public void clearMessages() 
	{
		this.lstMsgPanel.clear();
		this.messagesContainer.removeAll();
		this.revalidate();
		this.repaint();
	}

	/*----------------------------------*/
	/* Getters                          */
	/*----------------------------------*/
	// public String getChannelName() { return this.channelName; }
	// public List<MessagePanel> getlstMsgPanel() { return new
	// ArrayList<>(this.lstMsgPanel); }
}
