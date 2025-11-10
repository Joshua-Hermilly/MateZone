package client.metier.interfaces;

public interface INotificateur 
{
	void notifierMessage          (String  message            ); // Nouveau message reçu
	void notifierLstMessages      (String  lstMessages        ); // Liste des messages reçue
	void notifierConnexionServeur (boolean etat               ); // Connexion au serveur
	void notifierConnexionClient  (boolean etat, String pseudo); // Connexion au client
	void notifierEnregistrement   (boolean etat               ); // Enregistrement du client
	
	//void notifierErreur (String erreur);
}
