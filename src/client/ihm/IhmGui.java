package client.ihm;

import client.controleur.Controleur;
import client.ihm.frame.affichage.MateZoneFrame;
import common.dto.ChatEventDTO;
import javafx.application.Platform;
import javafx.scene.control.Alert;

/*-------------------------------*/
/* Class IhmGui                  */
/*-------------------------------*/
public class IhmGui
{

	/*-------------------------------*/
	/* Attributs                     */
	/*-------------------------------*/
	private Controleur               controleur;
	private MateZoneFrame            mateZoneFrame;
	private JavaFXApplicationManager javaFXManager;

	/*-------------------------------*/
	/* Constructeur */
	/*-------------------------------*/
	public IhmGui(Controleur controleur) 
	{
		this.controleur    = controleur;
		this.javaFXManager = new JavaFXApplicationManager();
	}

	/*-------------------------------*/
	/* Lancement des fenêtres        */
	/*-------------------------------*/
	public void lancerConnexionFrame() 
	{
		this.javaFXManager.creerEtAfficherConnexion(this.controleur);
	}

	public void lancerMateZoneFrame(String pseudo) 
	{
		Platform.runLater( () -> 
		{
			try 
			{
				this.javaFXManager.fermerConnexion();

				// Créer et afficher la fenêtre principale
				this.mateZoneFrame = new MateZoneFrame(this.controleur);
				this.mateZoneFrame.afficherFrame();

			} catch (Exception e) { e.printStackTrace(); }
		} );
	}

	public void afficherErreur(String message) 
	{
		Platform.runLater( () -> 
		{
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle      ( "Erreur" );
			alert.setHeaderText ( null     );
			alert.setContentText( message  );
			alert.showAndWait   (          );
		});
	}


	/*-------------------------------*/
	/* Affciher les messages         */
	/*-------------------------------*/
	public void afficherListMessage(ChatEventDTO eventDTO) 
	{
		if (this.mateZoneFrame != null) 
			Platform.runLater( () -> this.mateZoneFrame.afficherListMessage(eventDTO) );
	}

	public void afficherNvMessage(ChatEventDTO eventDTO) 
	{
		if (this.mateZoneFrame != null)
			Platform.runLater( () -> this.mateZoneFrame.afficherNvMessage(eventDTO) );
	}
}
