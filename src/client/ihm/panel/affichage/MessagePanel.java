package client.ihm.panel.affichage;

import common.dto.ChatEventDTO;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Circle;

/*-------------------------------*/
/* Class MessagePanel            */
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
public class MessagePanel 
{
	/*--------------------------*/
	/* Attributs                */
	/*--------------------------*/
	private ChatEventDTO event;
	private String       adresse;
	private Parent       parent;

	/*--------------------------*/
	/* Attributs FXML           */
	/*--------------------------*/
	@FXML private ImageView imgAvatar;
	@FXML private Label     lblPseudo;
	@FXML private Label     lblDate;
	@FXML private Label     lblMessage;


	/*--------------------------*/
	/* Constructeur             */
	/*--------------------------*/
	/**
	 * Constructeur du panneau de message.
	 * Charge le FXML et initialise les données du message.
	 * 
	 * @param event   l'événement de chat contenant les données du message à afficher
	 * @param adresse l'adresse du serveur d'images
	 */
	public MessagePanel(ChatEventDTO event, String adresse) throws Exception 
	{
		this.event   = event;
		this.adresse = adresse;

		// Chargement du FXML
		FXMLLoader loader = new FXMLLoader(getClass().getResource("MessagePanel.fxml"));
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
			// Récupération des données
			String pseudo   = (String) event.getData().get( "pseudo"   );
			String contenu  = (String) event.getData().get( "contenu"  );
			String date     = (String) event.getData().get( "date"     );
			String idClient = (String) event.getData().get( "idClient" );

			// Configuration des labels
			this.lblPseudo .setText( pseudo  );
			this.lblDate   .setText( date    );
			this.lblMessage.setText( contenu );

			// Configuration de l'avatar
			this.configurerAvatar( idClient );
		}
	}

	/*--------------------------*/
	/* Méthodes privées         */
	/*--------------------------*/
	/**
	 * Configure l'avatar de l'utilisateur avec une forme circulaire.
	 * 
	 * @param idClient l'identifiant du client pour charger son avatar
	 */
	private void configurerAvatar(String idClient)
	{
		try 
		{
			// Chargement de l'image depuis l'URL
			String imageUrl     = "http://" + adresse + "/avatar?id=" + idClient;
			Image  avatarImage = new Image( imageUrl, 40, 40, true, true );

			this.imgAvatar.setImage( avatarImage );

			// Création du clip circulaire pour l'avatar
			Circle clip = new Circle(20, 20, 20);
			this.imgAvatar.setClip(clip);

		} catch (Exception e) { this.imgAvatar.setImage(null); }
	}

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
