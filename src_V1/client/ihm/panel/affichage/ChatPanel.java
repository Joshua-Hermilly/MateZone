package client.ihm.panel.affichage;

import javax.swing.*;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.awt.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Panel central de l'application MateZone pour l'affichage des messages.
 * Contient une collection de MessagePanel pour afficher les messages
 * individuels.
 */
public class ChatPanel extends JPanel 
{
	/*--------------------------*/
	/* Composants               */
	/*--------------------------*/
	private JPanel                  panelMessages;
	private JScrollPane             scrollMessages;
	private JLabel                  lblTitreChannel;
	private ArrayList<MessagePanel> lstMessagePanels;

	/*--------------------------*/
	/* Constructeur             */
	/*--------------------------*/
	public ChatPanel()
	{
		this.setLayout(new BorderLayout());

		/*-------------------------------*/
		/* Création des composants       */
		/*-------------------------------*/

		// Liste des panels de messages
		this.lstMessagePanels = new ArrayList<MessagePanel>();

		// Titre du channel actuel
		this.lblTitreChannel = new JLabel("# Général", SwingConstants.CENTER);
		this.lblTitreChannel.setFont(new Font("Arial", Font.BOLD, 16));
		this.lblTitreChannel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		this.lblTitreChannel.setOpaque(true);
		this.lblTitreChannel.setBackground(new Color(64, 68, 75));
		this.lblTitreChannel.setForeground(Color.WHITE);

		// Panel contenant tous les messages
		this.panelMessages = new JPanel();
		this.panelMessages.setLayout(new BoxLayout(this.panelMessages, BoxLayout.Y_AXIS));
		this.panelMessages.setBackground(new Color(54, 57, 63));

		// Scroll pour la zone de messages
		this.scrollMessages = new JScrollPane(this.panelMessages);
		this.scrollMessages.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		this.scrollMessages.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		this.scrollMessages.setBorder(BorderFactory.createEmptyBorder());
		this.scrollMessages.getVerticalScrollBar().setUnitIncrement(16);

		/*-------------------------------*/
		/* Positionnement des composants */
		/*-------------------------------*/
		this.add(this.lblTitreChannel, BorderLayout.NORTH);
		this.add(this.scrollMessages, BorderLayout.CENTER);

		// Style général du panel
		this.setBackground(new Color(54, 57, 63));
		this.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
	}

	/*--------------------------*/
	/* Méthodes                 */
	/*--------------------------*/

	private void ajouterMessage(String nomUtilisateur, String heure, String message) 
	{
		MessagePanel messagePanel = new MessagePanel(nomUtilisateur, heure, message);
		
		this.lstMessagePanels.add(messagePanel);
		this.panelMessages   .add(messagePanel);
	}


	public void afficherLstMessages(String messages) 
	{
		// Vider les messages existants
		this.viderMessages();

		if (messages == null || messages.trim().isEmpty()) 
		{
			// Ajouter un message par défaut si vide
			this.ajouterMessage("Système", "Maintenant", "Aucun message dans ce channel");
		} 
		
		else 
		{
			try 
			{
				messages = messages.substring("MESSAGES_LIST:".length());

				System.out.println("Messages reçus : " + messages);

				// JSON message -> HashMap< Integer, String >
				Gson gson                                = new Gson();
				Type type                                = new TypeToken<HashMap<Integer, String[]>>(){}.getType();
				HashMap< Integer, String[] > mapMessages = gson.fromJson(messages, type);

				for (String[] messageData : mapMessages.values()) 
				{
					if (messageData.length >= 3) { this.ajouterMessage(messageData[0], messageData[2], messageData[1]); } 
					else                         { this.ajouterMessage(messageData[0], "MTN", messageData[1]); }
				}

			} 
			catch (Exception e) 
			{ 
				System.err.println("Erreur lors du parsing JSON: " + e.getMessage()); 
				this.ajouterMessage("Système", "Maintenant", "Erreur lors du chargement des messages"); 
			}
		}

		this.maj();
	}

	/*--------------------------*/
	/* Méthodes                 */
	/*--------------------------*/
	public void ajouterNvMessage(String message) 
	{
		System.out.println("Nouveau message reçu : " + message);
		message = message.substring("NEWMESSAGE:".length());

		if (message != null && !message.trim().isEmpty()) 
		{
			String[] parties = message.split("\\|", 3);

			if (parties.length >= 3) { this.ajouterMessage( parties[0], parties[1], parties[2] );                   }
			else                     { this.ajouterMessage("Système", "Maintenant", message); }

			this.maj();
		}
	}

	private void maj()
	{
		// Actualiser l'affichage
		this.panelMessages.revalidate();
		this.panelMessages.repaint();

		// Scroll automatique vers le bas
		SwingUtilities.invokeLater(() -> {
			JScrollBar vertical = this.scrollMessages.getVerticalScrollBar();
			vertical.setValue(vertical.getMaximum());
		});
	}

	public void viderMessages() 
	{
		this.lstMessagePanels.clear();
		this.panelMessages.removeAll();
		this.panelMessages.revalidate();
		this.panelMessages.repaint();
	}

	public void changerTitreChannel(String nomChannel) 
	{
		if (nomChannel != null && !nomChannel.trim().isEmpty())  { this.lblTitreChannel.setText("# " + nomChannel); } 
		else                                                     { this.lblTitreChannel.setText("# Channel"); }
	}
}