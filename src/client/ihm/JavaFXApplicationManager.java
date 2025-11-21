package client.ihm;

import javafx.application.Platform;
import javafx.stage.Stage;
import client.controleur.Controleur;
import client.ihm.frame.connexion.ConnexionFrame;

/*--------------------------------*/
/* Class JavaFXApplicationManager */
/*--------------------------------*/
/**
 * Gestionnaire pour l'application JavaFX.
 * Centralise la gestion du cycle de vie JavaFX et la création des fenêtres.
 * Élimine les variables statiques et améliore l'architecture.
 *
 * @author Joshua Hermilly
 * @version V1
 * @date 21/11/25
 */
public class JavaFXApplicationManager 
{
	/*-------------------------------*/
	/* Attributs                     */
	/*-------------------------------*/
	private static boolean estLancé  = false;
	private        Stage   connexionStage;

	/*-------------------------------*/
	/* Initialisation                */
	/*-------------------------------*/
	/**
	 * Initialise JavaFX si ce n'est pas déjà fait.
	 * Cette méthode est thread-safe et ne peut initialiser qu'une seule fois.
	 */
	public synchronized void initializeJavaFXIfNeeded() 
	{
		if ( !estLancé ) 
		{
			try 
			{
				// Initialisation de la plateforme JavaFX
				Platform.startup( () -> {} );
				JavaFXApplicationManager.estLancé = true;
			
			} catch (IllegalStateException e) {  JavaFXApplicationManager.estLancé = true; } //déja init
		}
	}


	/*-------------------------------*/
	/* Gérer les Frames              */
	/*-------------------------------*/
	/**
	 * Crée et affiche la fenêtre de connexion.
	 * 
	 * @param controleur le contrôleur de l'application
	 */
	public void creerEtAfficherConnexion( Controleur controleur )
	{
		this.initializeJavaFXIfNeeded();

		Platform.runLater(() -> 
		{
			try 
			{
				this.connexionStage           = new Stage();
				ConnexionFrame connexionFrame = new ConnexionFrame( controleur );
				connexionFrame.start( connexionStage );

			} catch (Exception e) { e.printStackTrace(); }
		});
	}

	/**
	 * Ferme la fenêtre de connexion si elle existe.
	 */
	public void fermerConnexion() 
	{
		if ( this.connexionStage != null ) 
		{
			Platform.runLater(() -> 
			{
				this.connexionStage.close();
				this.connexionStage = null;
			});
		}
	}
}