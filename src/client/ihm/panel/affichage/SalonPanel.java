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
 * Classe extend JPanel qui fait office de panel salon qui créer les MessagePanel.
 * La frame est faite avec le client/ihm/frame/affichage/MateZoneFrame
 */
public class SalonPanel extends JPanel 
{
	/*--------------------------*/
	/*       Composants         */
	/*--------------------------*/
	private String             channelName;
	private List<MessagePanel> lstMsgPanel;
	private JPanel             messagesContainer;
	private JScrollPane        scrollPane;
	private JLabel             lblChannelName;
	
	/*--------------------------*/
	/*       Constructeur       */
	/*--------------------------*/
	public SalonPanel( String channelName ) 
	{
		this.channelName  = channelName;
		this.lstMsgPanel = new ArrayList<>();
		
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
		this.add( this.lblChannelName, BorderLayout.NORTH  );
		this.add( this.scrollPane    , BorderLayout.CENTER );
	}


	/*----------------------------------*/
	/* Gestion Ajout et Suppresion Msg  */
	/*----------------------------------*/
	public void addMessage( ChatEventDTO event ) 
	{
		MessagePanel messagePanel = new MessagePanel( event );
		this.lstMsgPanel      .add( messagePanel );
		this.messagesContainer.add( messagePanel );
		
		// Auto-scroll vers le bas
		SwingUtilities.invokeLater(() -> {
			JScrollBar vertical = this.scrollPane.getVerticalScrollBar();
			vertical.setValue(vertical.getMaximum());
		});
		
		this.revalidate();
		this.repaint();
	}
	
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
	//public String             getChannelName() { return this.channelName;                  }
	//public List<MessagePanel> getlstMsgPanel() { return new ArrayList<>(this.lstMsgPanel); }
}
