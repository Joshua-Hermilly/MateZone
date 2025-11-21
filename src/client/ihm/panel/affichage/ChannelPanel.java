package client.ihm.panel.affichage;

import common.dto.ChatEventDTO;
import client.controleur.Controleur;

import java.lang.ModuleLayer.Controller;
import java.util.ResourceBundle.Control;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Circle;

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

		if ( event != null && event.getData().get("idChannel") != null )
		{
			try 
			{
				this.idChannel = Integer.parseInt( event.getData().get("idChannel").toString() );
			
			} catch (NumberFormatException e) {	this.idChannel = -1; }
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
			this.btnChanel.setText( (String) event.getData().get( "nomChannel" ) );
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
	private void changerChannel() { this.controleur.changerChannel( this.idChannel ); }

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
