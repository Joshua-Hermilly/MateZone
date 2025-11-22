package client.ihm.frame.affichage;

import java.io.File;
import java.util.List;

import client.controleur.Controleur;
import client.ihm.panel.affichage.*;
import common.dto.ChatEventDTO;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/*-------------------------------*/
/* Class MateZoneFrame           */
/*-------------------------------*/
/**
 * Fenêtre principale de MateZone utilisant JavaFX et FXML.
 * Cette fenêtre contient l'affichage des messages et la zone de saisie.
 * Elle est affichée après une connexion réussie de l'utilisateur.
 *
 * @author Joshua Hermilly
 * @version V2 - JavaFX
 * @date 21/11/25
 */
public class MateZoneFrame 
{
	/*-------------------------------*/
	/* Attributs                     */
	/*-------------------------------*/
	private Controleur controleur;
	private Stage      stage;
	private String     channelName;

	/*-------------------------------*/
	/* Attributs FXML                */
	/*-------------------------------*/
	@FXML private Label      lblChannelName;
	@FXML private ScrollPane scrollPaneMessages;
	@FXML private ScrollPane scrollPaneChannel;
	@FXML private VBox       messagesContainer;
	@FXML private VBox       channelContainer;
	@FXML private TextField  txtMessage;
	@FXML private Button     btnEnvoyer;
	@FXML private Button     btnPieceJointe;
	@FXML private Button     btnParametre;

	/*-------------------------------*/
	/* Constructeur                  */
	/*-------------------------------*/
	/**
	 * Constructeur de la fenêtre principale MateZone.
	 * 
	 * @param controleur le contrôleur principal de l'application
	 */
	public MateZoneFrame(Controleur controleur) 
	{
		this.controleur   = controleur;
		this.channelName  = "Général";
	}

	/*-------------------------------*/
	/* Initialisation FXML           */
	/*-------------------------------*/
	/**
	 * Méthode appelée automatiquement après le chargement du FXML.
	 * Configure les événements des boutons et du champ de texte.
	 */
	@FXML
	public void initialize() 
	{
		// Configuration du nom du canal
		this.lblChannelName.setText( this.channelName );

		// Configuration des événements
		this.btnEnvoyer    .setOnAction( e -> this.envoyerMessage    () );
		this.btnPieceJointe.setOnAction( e -> this.envoyerPieceJointe() );
		this.btnParametre  .setOnAction( e -> this.ouvrirParametre   () );
		this.txtMessage    .setOnAction( e -> this.envoyerMessage    () );

		// Configuration du scroll automatique
		this.messagesContainer.heightProperty().addListener( (obs, oldVal, newVal) -> 
		{
			Platform.runLater( () -> this.scrollPaneMessages.setVvalue(1.0)) ;
		});
		this.scrollPaneMessages.setVvalue( this.scrollPaneMessages.getVmax() );

		this.channelContainer.heightProperty().addListener( (obs, oldVal, newVal) -> 
		{
			Platform.runLater( () -> this.scrollPaneChannel.setVvalue(1.0)) ;
		});
	}

	/*-------------------------------*/
	/* Méthodes publiques            */
	/*-------------------------------*/
	/**
	 * Affiche la fenêtre MateZone.
	 */
	public void afficherFrame() throws Exception 
	{
		FXMLLoader loader = new FXMLLoader(getClass().getResource("MateZoneFrame.fxml"));
		loader.setController(this);

		this.stage = new Stage();
		this.stage.setTitle("MateZone");                                   // Titre
		this.stage.getIcons().add(new Image("file:./logo/MateZone.png"));  // Îcone
		this.stage.setScene(new Scene(loader.load(), 800, 600));           // Créer et définir la scène

		this.stage.setOnCloseRequest(event -> { System.exit(0); }  );     //Fermer  X

		this.stage.show();                                                 // Afficher
	}


	/*-------------------------------*/
	/* Messages                     */
	/*-------------------------------*/
	/**
	 * Affiche une liste de messages dans le salon de chat.
	 * 
	 * @param eventDTO l'événement contenant la liste des messages à afficher
	 */
	public void afficherListMessage(ChatEventDTO eventDTO) 
	{
		List<ChatEventDTO> lstEvent = eventDTO.getLstEventDTO();

		this.messagesContainer.getChildren().clear();

		for (ChatEventDTO chatEventDTO : lstEvent)
			this.afficherNvMessage(chatEventDTO);
	}

	/**
	 * Affiche un nouveau message dans le salon de chat.
	 * 
	 * @param eventDTO l'événement contenant le nouveau message à afficher
	 */
	public void afficherNvMessage(ChatEventDTO eventDTO) 
	{
		Platform.runLater(() -> 
		{
			try 
			{
				MessagePanel messagePanel = new MessagePanel(eventDTO, this.controleur.getAdrServImg());
				messagesContainer.getChildren().add(messagePanel.getParent());

			}  catch (Exception e) { e.printStackTrace(); }
		});
	}

	/*-------------------------------*/
	/* Channels                      */
	/*-------------------------------*/
	/**
	 * Affiche une liste de channels.
	 * 
	 * @param eventDTO l'événement contenant la liste des channels à afficher
	 */
	public void afficherListChannel(ChatEventDTO eventDTO) 
	{
		List<ChatEventDTO> lstEvent = eventDTO.getLstEventDTO();

		for (ChatEventDTO chatEventDTO : lstEvent)
			this.afficherNvChannel(chatEventDTO);
	}

	/**
	 * Affiche un nouveau channels.
	 * 
	 * @param eventDTO l'événement contenant le nouveau channels à afficher
	 */
	public void afficherNvChannel(ChatEventDTO eventDTO) 
	{
		Platform.runLater(() -> 
		{
			try 
			{
				ChannelPanel channelPanel = new ChannelPanel( this.controleur, eventDTO );
				this.channelContainer.getChildren().add( channelPanel.getParent()) ;

			}  catch (Exception e) { e.printStackTrace(); }
		});
	}

	public void setNomCannel( String nom )
	{ 
	Platform.runLater(() -> 
		{
			try 
			{
				this.lblChannelName.setText( nom );

			}  catch (Exception e) { e.printStackTrace(); }
		});
		
	}

	/*-------------------------------*/
	/* Envoyer                       */
	/*-------------------------------*/
	/**
	 * Envoie le message saisi dans le champ de texte.
	 */
	private void envoyerMessage() 
	{
		String message = this.txtMessage.getText().trim();

		if (!message.isEmpty()) 
		{
			this.controleur.envoyerMessage(message );
			this.txtMessage.setText("");
		}
	}

	/**
	 * Ouvre un sélecteur de fichier pour envoyer une pièce jointe.
	 */
	private void envoyerPieceJointe() 
	{
		FileChooser chooser = new FileChooser();
		chooser.setTitle("Choisir une image");
		chooser.getExtensionFilters().add( new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg", "*.gif") );

		File selectedFile = chooser.showOpenDialog(this.stage);
		if (selectedFile != null) 
		{
			this.controleur.envoyerPieceJoint(selectedFile.getAbsolutePath());
		}
	}


	/*-------------------------------*/
	/* Paramètre                     */
	/*-------------------------------*/
	public void ouvrirParametre ()
	{
		System.out.println("hey");
		//this.controleur.ouvrirParametre();
	}
}
