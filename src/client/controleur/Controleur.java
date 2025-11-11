package client.controleur;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import client.ihm.IhmGui;
import client.infrastructure.websocket.WebSocketChatAdapter;
import client.metier.Metier;
import client.metier.interfaces.IEnvoyeur;
import client.metier.interfaces.INotifieur;
import common.dto.ChatEventDTO;

/*-------------------------------*/
/* Class Controleur              */
/*-------------------------------*/
/**
 * Classe principale de contrôle de l'application MateZone côté Client.
 * Gère la logique de connexion et le lancement des différentes interfaces
 * utilisateur.
 *
 * @author Joshua Hermilly
 * @version V1.1
 * @date 08/11/25
 */
public class Controleur implements INotifieur 
{
	/*--------------------------*/
	/* Attributs                */
	/*--------------------------*/
	private static final String ADRESSE_SERVEUR = "ws://localhost:8080";

	private IhmGui ihmGui;
	private Metier metier;

	/*--------------------------*/
	/* Constructeur             */
	/*--------------------------*/
	public Controleur() throws Exception 
	{
		this.ihmGui = new IhmGui(this);

		INotifieur iNotifieur = this;
		IEnvoyeur iEnvoyeur   = new WebSocketChatAdapter(Controleur.ADRESSE_SERVEUR, iNotifieur);
		iEnvoyeur.connecter();

		this.metier = new Metier(iEnvoyeur, iNotifieur);
	}

	public void lancerApp() { this.ihmGui.lancerConnexionFrame(); }

	/*-----------------------------------*/
	/* METIER                            */
	/*-----------------------------------*/
	/*--------------------------*/
	/* Client                   */
	/*--------------------------*/
	public void tenterConnexionClient(String pseudo, String mdp)
	 {
		// Format valide ?
		if ( !this.validerIdentifiants(pseudo, mdp) ) return;

		this.metier.connecterAuClient( pseudo, mdp );
	}

	public void tenterEnregistrement(String pseudo, String mdp) 
	{
		// Format valide ?
		if ( !this.validerIdentifiants(pseudo, mdp) ) return;

		this.metier.enregistrerUtilisateur( pseudo, mdp );
	}

	public void envoyerMessage( String message ) 
	{
		this.metier.envoyerMessage(message);
	}

	public void envoyerPieceJoint(String cheminFic) 
	{
		byte[] bytes = this.extractBytes(cheminFic);

		if ( bytes == null ) 
		{
			this.ihmGui.afficherErreur( "Envoie de piece jointe impossible: " + cheminFic );
			return;
		}

		this.metier.envoyerPieceJoint( bytes );
	}


	private byte[] extractBytes( String ImageName ) 
	{
		try 
		{
			return Files.readAllBytes(Paths.get(ImageName));

		} catch (IOException e1) { e1.printStackTrace(); }

		return null;
	}

	/*--------------------------*/
	/* Serveur                  */
	/*--------------------------*/

	/*---------------------------*/
	/* Notifications (Interface) */
	/*---------------------------*/
	public void notifierMessage     (ChatEventDTO event       ) { this.metier.notifierMessage    ( event       ); }
	public void succesLogin         (String       pseudo      ) { this.ihmGui.lancerMateZoneFrame( pseudo      ); }
	public void notifierErreur      (String       erreur      ) { this.ihmGui.afficherErreur     ( erreur      ); }
	public void afficherListMessage (ChatEventDTO lstEventDTO ) { this.ihmGui.afficherListMessage( lstEventDTO ); }
	public void afficherNvMessage   (ChatEventDTO eventDTO    ) { this.ihmGui.afficherNvMessage  ( eventDTO    ); }

	/*---------------------------*/
	/* Validations               */
	/*---------------------------*/
	private boolean validerIdentifiants(String pseudo, String mdp) 
	{
		// pseudo valide ?
		if (pseudo.isEmpty() || pseudo.length() < 3) 
		{
			this.ihmGui.afficherErreur("Le pseudo doit contenir au moins 3 caractères.");
			return false;
		}

		// mdp valide ?
		if (mdp.isEmpty() || mdp.length() < 6) 
		{
			this.ihmGui.afficherErreur("Le mot de passe doit contenir au moins 6 caractères.");
			return false;
		}

		return true;
	}

}
