package client.ihm.frame.connexion;

import client.controleur.Controleur;
import client.ihm.panel.connexion.*;

import java.io.IOException;
import java.net.URL;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/*--------------------------------*/
/* Class ConnexionFrame           */
/*--------------------------------*/
public class ConnexionFrame extends Application 
{
	/*-------------------------------*/
	/* Attributs                     */
	/*-------------------------------*/
	private Controleur controleur;

	/*-------------------------------*/
	/* Constructeur                  */
	/*-------------------------------*/
	public ConnexionFrame(Controleur controleur) 
	{
		this.controleur = controleur;
	}

	/*-------------------------------*/
	/* Lancment de la frame          */
	/*-------------------------------*/
	@Override
	public void start(Stage scenePrincipale) throws IOException
	{
		// Charger l'interface utilisateur via FXML
		String     lienFic   = "/client/ihm/panel/connexion/ConnexionPanel.fxml";             // Adresse du fichier FXML
		URL        urlFic    = getClass().getResource( lienFic);                              // URL du fichier FXML pour FXMLLoader
		FXMLLoader loader    = new FXMLLoader( urlFic );                                      // Création du loader FXML
		loader.setControllerFactory( c -> new ConnexionPanel(controleur) );                   // Passer controleur au panel

		scenePrincipale.setTitle( "Connexion" );                                              // Titre
		scenePrincipale.getIcons().add( new Image("file:./image/image_1762797388911.png") );  // Îcone
		scenePrincipale.setResizable(false);                                                  // Redimensionnement desactivé
		scenePrincipale.setScene( new Scene(loader.load()) );                                 // Créer et définir la scène

		scenePrincipale.centerOnScreen();                                                     // Centrer
		scenePrincipale.show();                                                               // Afficher
	}
}
