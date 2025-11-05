package MateZone.metier;

import MateZone.metier.gestionBD.ConnexionBD;
import MateZone.metier.gestionBD.ClientsBD;
import MateZone.metier.client.Client;

import java.util.List;

/*---------------------------------*/
/*  Class Metier                   */
/*---------------------------------*/
/**
 * Classe métier qui gère la logique applicative etle lien avec la BD
 */
public class Metier 
{
	/*-------------------------------*/
	/* Attributs                     */
	/*-------------------------------*/
	
	/** DAO pour les opérations sur les clients */
		private ClientsBD clientsBD;
	
	/** Instance de connexion à la base de données */
		private ConnexionBD connexionBD;
	
	/*-------------------------------*/
	/* Constructeur                  */
	/*-------------------------------*/
	/**
	 * Constructeur qui initialise les composants de la couche métier
	 */
	public Metier() 
	{
		this.clientsBD   = new ClientsBD();
		this.connexionBD = ConnexionBD.getInstance();
	}
	
	/* ---------------------------------- */
	/*  Méthodes de gestion des clients   */
	/* ---------------------------------- */
	
	/**
	 * Récupère tous les clients de la base de données
	 * @return Liste de tous les clients
	 */
	public List<Client> getClients() 
	{
		return clientsBD.getClients();
	}
	
	/**
	 * Recherche un client par son ID
	 * @param id L'ID du client à rechercher
	 * @return Le client trouvé ou null si non trouvé
	 */
	public Client getClientById( int id ) 
	{
		return clientsBD.getClientById( id );
	}
	
	/**
	 * Ajoute un nouveau client
	 * @param client Le client à ajouter
	 * @return true si l'ajout a réussi, false sinon
	 */
	public boolean ajouterClient( Client client ) 
	{
		// Validation métier
		if ( client.getPseudo() == null || client.getPseudo().trim().isEmpty() )  return false;
		if ( client.getMdp() == null || client.getMdp().trim().isEmpty() )  return false;

		return clientsBD.createClient( client );
	}
	
	/**
	 * Modifie un client existant
	 * @param client Le client avec les nouvelles valeurs
	 * @return true si la modification a réussi, false sinon
	 */
	public boolean modifierClient( Client client ) 
	{
		// Validation métier
		if ( client == null )  return false;
		if ( client.getPseudo() == null || client.getPseudo().trim().isEmpty() )  return false;
		if ( client.getMdp() == null || client.getMdp().trim().isEmpty() )  return false;

		return clientsBD.majClient( client );
	}
	
	/**
	 * Supprime un client
	 * @param id L'ID du client à supprimer
	 * @return true si la suppression a réussi, false sinon
	 */
	public boolean supprimerClient( int id ) 
	{
		Client client = clientsBD.getClientById( id );
		
		if ( client == null )  return false;

		return clientsBD.supprimerClient( id );
	}

	/**
	 * Recherche un client par son nom
	 * @param nom Le nom du client à rechercher
	 * @return Le client trouvé ou null si non trouvé
	 */
	public Client getClientByNom( String nom ) 
	{
		return clientsBD.getClientByPseudo( nom );
	}

	/**
	 * Authentifie un client
	 * @param nom Le nom du client
	 * @param mdp Le mot de passe
	 * @return Le client authentifié ou null si échec
	 */
	public Client authentifierClient( String nom, String mdp ) 
	{
		// Validation métier
		if ( nom == null || nom.trim().isEmpty() )  return null;
		if ( mdp == null || mdp.trim().isEmpty() )  return null;

		return clientsBD.authenticate( nom, mdp );
	}


	/* --------------------------------- */
	/*  Méthodes de gestion de la base   */
	/* --------------------------------- */
	
	/**
	 * Teste la connexion à la base de données
	 * @return true si la connexion fonctionne, false sinon
	 */
	public boolean testerConnexion() 
	{
		return connexionBD.testerConnexion();
	}
		
	/**
	 * Ferme la connexion à la base de données
	 */
	public void fermerConnexion() 
	{
		if (connexionBD != null) 
		{
			connexionBD.fermerConnexion();
		}
	}
}
