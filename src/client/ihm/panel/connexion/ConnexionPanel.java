package client.ihm.panel.connexion;

import client.controleur.Controleur;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;

/*--------------------------------*/
/* Class ConnexionFrame           */
/*--------------------------------*/
public class ConnexionPanel 
{
	/*-------------------------------*/
	/* Attributs                     */
	/*-------------------------------*/
	private Controleur controleur;

	/*--------------------------*/
	/* Attributs FXML           */
	/*--------------------------*/
	@FXML private TextField     txtPseudo;
	@FXML private PasswordField pwdMdp;
	@FXML private Button        btnConnecter;
	@FXML private Button        btnEnregistrer;

	
	/*-------------------------------*/
	/* Constructeur                  */
	/*-------------------------------*/
	public ConnexionPanel(Controleur controleur) 
	{
		this.controleur = controleur;
	}

	@FXML
	public void initialize() 
	{
		//Ajout des Listener 
		this.btnConnecter  .setOnAction( e -> this.actionConnexion      ( e ) );
		this.btnEnregistrer.setOnAction( e -> this.actionEnregistrement ( e ) );

		// 'Entré' -> action btnConnexion
		this.txtPseudo.setOnAction(  e -> this.actionConnexion ( e ) );
		this.pwdMdp   .setOnAction(  e -> this.actionConnexion ( e ) );
	}



	/*-------------------------------*/
	/* Actions des Bouttons          */
	/*-------------------------------*/
	private void actionConnexion(ActionEvent event) 
	{
		String pseudo = this.txtPseudo.getText().trim();
		String mdp    = this.pwdMdp   .getText().trim();

		if ( pseudo.isEmpty() || mdp.isEmpty() ) 
		{
			this.controleur.notifierErreur("Veuillez saisir un pseudo et un mot de passe.");
			return;
		}

		this.controleur.tenterConnexionClient( pseudo, mdp );
	}

	private void actionEnregistrement(ActionEvent event) 
	{
		String pseudo = this.txtPseudo.getText().trim();
		String mdp    = this.pwdMdp   .getText().trim();

		if ( pseudo.isEmpty() || mdp.isEmpty() ) 
		{
			this.controleur.notifierErreur("Veuillez saisir un pseudo et un mot de passe.");
			return;
		}

		this.controleur.tenterEnregistrement( pseudo, mdp );
	}
}
