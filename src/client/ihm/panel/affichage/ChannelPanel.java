package client.ihm.panel.affichage;

import common.dto.ChatEventDTO;
import client.controleur.Controleur;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;

/*-------------------------------*/
/* Class ChannelPanel            */
/*-------------------------------*/
/**
 * Panneau de message utilisant JavaFX et FXML pour représenter un message
 * individuel dans le chat.
 * Affiche l'avatar circulaire de l'utilisateur, son pseudonyme, la date d'envoi
 * et le contenu du message.
 *
 * @author Joshua Hermilly
 * @version V2 - JavaFX
 * @date 21/11/25
 */
public class ChannelPanel 
{
	/*--------------------------*/
	/* Attributs                */
	/*--------------------------*/
	private Controleur   controleur;
	private ChatEventDTO event;
	private Parent       parent;
	private int          idChannel;
	private String       nmChannel;

	/*--------------------------*/
	/* Attributs FXML           */
	/*--------------------------*/
	@FXML private Button  btnChanel;


	/*--------------------------*/
	/* Constructeur             */
	/*--------------------------*/

	public ChannelPanel(Controleur controleur, ChatEventDTO event) throws Exception
	{
		this.controleur = controleur;
		this.event      = event;


		if ( event != null )
		{
			try 
			{
				this.idChannel = (int) Double.parseDouble( event.getData().get("idChannel").toString() );
				this.nmChannel = event.getData().get( "nomChannel").toString();

			} catch (NumberFormatException e) {	this.idChannel = -1; e.printStackTrace(); }
		}
		// Chargement du FXML
		FXMLLoader loader = new FXMLLoader(getClass().getResource("ChannelPanel.fxml"));
		loader.setController(this);
		this.parent = loader.load();
	}

	/*--------------------------*/
	/* Initialisation FXML      */
	/*--------------------------*/
	/**
	 * Méthode appelée automatiquement après le chargement du FXML.
	 * Initialise les composants avec les données du message.
	 */
	@FXML
	public void initialize() 
	{
		if (event != null)
		{
			this.btnChanel.setText( this.nmChannel );
			this.btnChanel.setOnAction( e -> this.changerChannel() );
		}
	}

	/*--------------------------*/
	/* Méthodes privées         */
	/*--------------------------*/
	/**
	 * 
	 * 
	 */
	private void changerChannel() { this.controleur.changerChannel( this.idChannel, this.nmChannel ); }

	/*--------------------------*/
	/* Getters                  */
	/*--------------------------*/
	/**
	 * Retourne le nœud racine du panneau de message.
	 * 
	 * @return le nœud Parent racine
	 */
	public Parent getParent() { return this.parent; }
}
