package client.metier.connexionServeur;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;

import client.metier.interfaces.INotificateur;

/*---------------------------------*/
/*  Class ClientConnexion          */
/*---------------------------------*/

public class ClientConnexion extends WebSocketClient 
{
	/*-------------------------------*/
	/* Attributs                     */
	/*-------------------------------*/
	private boolean connectee;
	private int     idClient;
	private String  pseudoClient;

	private INotificateur notificateur;

	/*-------------------------------*/
	/* Constructeur                  */
	/*-------------------------------*/
	public ClientConnexion(String host, int port) throws Exception 
	{
		super(new URI("ws://" + host + ":" + port));

		this.connectee    = false;
		this.notificateur = null;
		this.pseudoClient = null;
	}

	/*-------------------------------*/
	/* Notifications                 */
	/*-------------------------------*/

	public void setNotificateur(INotificateur notificateur) { this.notificateur = notificateur; }

	private void notifierMessage(String message) 
	{
		if (this.notificateur != null) { this.notificateur.notifierMessage(message); }
	}

	private void notifierLstMessages(String lstMessages) 
	{
		if (this.notificateur != null) { this.notificateur.notifierLstMessages(lstMessages); }
	}

	private void notifierConnexionServeur(boolean etat) 
	{
		if (this.notificateur != null) { this.notificateur.notifierConnexionServeur(etat); }
	}	

	private void notifierConnexionClient(boolean etat) 
	{
		if (this.notificateur != null) { this.notificateur.notifierConnexionClient(etat, this.pseudoClient); }
	}

	private void notifierEnregistrement(boolean etat) 
	{
		if (this.notificateur != null) { this.notificateur.notifierEnregistrement(etat); }
	}


	/*-------------------------------*/
	/* Envoie message                */
	/*-------------------------------*/
	public void envoyerMessage(String message) 
	{
		if (this.isOpen()) { this.send(message);                                        } 
		else               { System.out.println("Impossible d'envoyer le message."); }

		System.out.println("Message envoyé : " + message);
		this.notifierMessage(message);
	}

	/*-------------------------------*/
	/* Message reçu                  */
	/*-------------------------------*/
	public void traiterMessage(String message) 
	{
		if ( message.startsWith("CONNECT:"       ) ) { this.estConnectee  (message); return; }
		if ( message.startsWith("REGISTERED:"    ) ) { this.estEnregistre (message); return; }
		if ( message.startsWith("MESSAGES_LIST:" ) ) { this.estMessageList(message); return; }
		if ( message.startsWith("NEWMESSAGE:"    ) ) { this.estMessage    (message); return; }


		//System.out.println("Message reçu du serveur : " + message);
	}

	/*-------------------------------*/
	/* Traitement message reçu       */
	/*-------------------------------*/
	private void estConnectee(String message) 
	{
		if (!message.equals("CONNECT:-1")) 
		{
			this.connectee = true;
		
			try
			{
				this.idClient     = Integer.parseInt(message.split(":")[1]);
				this.pseudoClient = message.split(":")[2];

				System.out.println("ID Client : " + this.idClient + " | Pseudo : " + this.pseudoClient);

			} catch (Exception e) { System.err.println("Erreur l178 : " + e.getMessage()); }
		}

		System.out.println(this.connectee ? "Connexion réussie !" : "Échec de la connexion.");

		// Notifi inteface
		this.notifierConnexionClient(this.connectee);
	}

	private void estEnregistre(String message) 
	{
		boolean succes = message.equals("REGISTERED:true");

		if (succes) { System.out.println("Enregistrement réussi !"   ); } 
		else        { System.out.println("Échec de l'enregistrement."); }

		// Notifier le contrôleur du résultat de l'enregistrement
		this.notifierEnregistrement(succes);
	}

	private void estMessageList(String message) 
	{
		this.notifierLstMessages(message); 
	}

	private void estMessage(String message) 
	{
		String nvMessage = message.substring("NEW_MESSAGE:".length());

		this.notifierMessage(nvMessage);
	}

	/*-------------------------------*/
	/* Getters                       */
	/*-------------------------------*/
	public boolean estConnectee() { return connectee;    }
	public int     getIdClient () { return idClient;     }
	public String  getPseudo   () { return pseudoClient; }

	/*-------------------------------*/
	/* Méthodes Override             */
	/*-------------------------------*/

	// Connexion au Serveur
	@Override
	public void onOpen(ServerHandshake handshake) 
	{
		System.out.println("Connecté au serveur !");
		send("CONNECT:");

		// Notifier connexion au serveur Ok
		this.notifierConnexionServeur(true);
	}

	// Message Reçu
	@Override
	public void onMessage(String message) 
	{
		this.traiterMessage(message);
	}

	// Connexion arrétée
	@Override
	public void onClose(int code, String reason, boolean remote) 
	{
		System.out.println("Déconnecté du serveur.");

		// Notifier connexion au serveur fermée
		this.notifierConnexionServeur(false);
		this.notifierConnexionClient(false);
	}

	// Erreur Connexion/Communication
	@Override
	public void onError(Exception ex) 
	{
		System.out.println("Erreur : " + ex.getMessage());
	}
}