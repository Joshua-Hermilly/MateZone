package server.gestionBD;

import server.Client;

import java.sql.Connection;          // Pour la connexion à la BD
import java.sql.PreparedStatement;   // Pour le driver JDBC ( dans /lib )
import java.sql.ResultSet;           // Pour exécuter des requêtes
import java.sql.SQLException;        //Pour les résultats des requêtes
import java.sql.Statement;
import java.sql.Timestamp;

import java.util.ArrayList;
import java.util.List;

/*---------------------------------*/
/*  Class GestionBD                */
/*---------------------------------*/
/**
 *
 * Cette classe gère toutes les opérations de base de données
 */
public class Request 
{
	/*-------------------------------*/
	/* Attributs                     */
	/*-------------------------------*/

	/** Instance de connexion à la base de données */
	private ConnexionBD connexionBD;
	
	/*-------------------------------*/
	/* Constructeurs                 */
	/*-------------------------------*/
	/**
	 * Constructeur qui initialise la connexion à la base de données MySQL
	 */
	public Request()
	{
		this.connexionBD = ConnexionBD.getInstance();
	}

	/*-------------------------------*/
	/* Accesseurs                    */
	/*-------------------------------*/

	public int getClientId(String pseudo)
	{
		String sql = "SELECT id FROM clients where pseudo = ?";

		try 
		{
			Connection        conn = this.connexionBD.getConnection();
			PreparedStatement stmt = conn.prepareStatement( sql );
			
			stmt.setString( 1, pseudo ); 

			ResultSet rs = stmt.executeQuery();

			if (rs.next()) return rs.getInt("id");

		} 
		catch (SQLException e) 
		{
			System.err.println( "Erreur lors de la récupération de l'id du client" );
			e.printStackTrace();
		}
		
		return -1;
	}




	/**
	 * Méthode pour récupérer tous les clients de la base de données
	 * @return liste de tous les clients
	 */
	
	public List<Client> getClients() 
	{
		List<Client> clients = new ArrayList<>();
		String sql = "SELECT id, pseudo, mdp, created_at FROM clients ORDER BY id";
		
		try 
		{
			Connection        conn = this.connexionBD.getConnection();
			PreparedStatement stmt = conn.prepareStatement( sql );
			ResultSet           rs = stmt.executeQuery();
			
			// Parcours de tous les résultats
			while ( rs.next() ) 
			{
				Client client = mapRowToClient( rs );
				clients.add( client );
			}

			System.out.println( "Nb clients récup : " + clients.size() );		
		} 
		catch (SQLException e) 
		{
			System.err.println( "Erreur lors de la récupération des clients" );
			e.printStackTrace();
		}
		
		return clients;
	}
	
	/**
	 * Méthode pour récupérer un client par son ID
	 * @param id l'identifiant du client
	 * @return le client trouvé ou null si non trouvé
	 */
	public Client getClientById( int id ) 
	{
		String sql = "SELECT id, pseudo, mdp, created_at FROM clients WHERE id = ?";
		
		try 
		{
			Connection        conn = this.connexionBD.getConnection();
			PreparedStatement stmt = conn.prepareStatement( sql );
			
			// Ajout de l'ID
			stmt.setInt( 1, id ); 

			ResultSet rs = stmt.executeQuery();
			
			if ( rs.next() ) 
			{
				Client client = mapRowToClient( rs );

				System.out.println( "Client trouvé : " + client );
				return client;
			} 
			else 
			{
				System.out.println( "Aucun client trouvé avec l'ID : " + id );
			}
			
		} 
		catch (SQLException e) 
		{
			System.err.println( "Erreur lors de la recherche du client ID : " + id );
			e.printStackTrace();
		}
		
		return null;
	}

	/** 
	 * Méthode pour récupérer un client par son pseudo
	 * @param pseudo le pseudo du client
	 * @return le client trouvé ou null si non trouvé
	 */
	public Client getClientByPseudo( String pseudo )
	{
		String sql = "SELECT id, pseudo, mdp, created_at FROM clients WHERE pseudo = ?";
		
		try 
		{
			Connection        conn = this.connexionBD.getConnection();
			PreparedStatement stmt = conn.prepareStatement( sql );
			
			// Ajout des paramètres
			stmt.setString ( 1, pseudo );

			ResultSet rs = stmt.executeQuery();
			
			if ( rs.next() ) 
			{
				Client client = mapRowToClient( rs );

				System.out.println( "Client trouvé : " + client );
				return client;
			} 
			else 
			{
				System.out.println( "Aucun client trouvé avec le pseudo : " + pseudo );
			}
			
		} 
		catch (SQLException e) 
		{
			System.err.println( "Erreur lors de la recherche du client pseudo : " + pseudo );
			e.printStackTrace();
		}
		
		return null;
	}



		/** 
	 * Méthode pour connaitre si 
	 * @param pseudo le pseudo du client
	 * @return le client trouvé ou null si non trouvé
	 */
	public Boolean getConnexionValideClient( String pseudo, String mdp )
	{
		String sql = "SELECT id, pseudo, mdp, created_at FROM clients WHERE pseudo = ? and mdp = ?";
		
		try 
		{
			Connection        conn = this.connexionBD.getConnection();
			PreparedStatement stmt = conn.prepareStatement( sql );
			
			// Ajout des paramètres
			stmt.setString ( 1, pseudo );
			stmt.setString ( 2, mdp );

			ResultSet rs = stmt.executeQuery();
			
			if ( rs.next() ) 
			{
				Client client = mapRowToClient( rs );

				System.out.println( "Client trouvé : " + client );
				return true;
			} 
			else 
			{
				System.out.println( "Aucun client trouvé avec le pseudo et le mdp : " + pseudo );
				return false;
			}
			
		} 
		catch (SQLException e) 
		{
			System.err.println( "Erreur lors de la recherche du client (pseudo+mdp) : " + pseudo );
			e.printStackTrace();
		}
		
		return null;
	}
	
	/* ------------------------------ */
	/*  Ajout/Suppression             */
	/* ------------------------------ */
	/**
	 * Méthode pour créer un nouveau client dans la base de données
	 * @param client le client à créer
	 * @return true si la création a réussi, false sinon
	 */
	public boolean createClient(Client client) 
	{
		if ( client == null                                       ) return false;
		if ( this.getClientByPseudo( client.getPseudo() ) != null ) return false;

		String sql = "INSERT INTO clients (pseudo, mdp) VALUES (?, ?)";
		
		try 
		{
			Connection        conn = this.connexionBD.getConnection();
			PreparedStatement stmt = conn.prepareStatement( sql, Statement.RETURN_GENERATED_KEYS );
			
			// Ajout des paramètres
			stmt.setString ( 1, client.getPseudo() );
			stmt.setString ( 2, client.getMdp()    );

			// Exécution de la requête
			int lignesRetour = stmt.executeUpdate();
			
			if ( lignesRetour > 0 ) 
			{
				// Récupération de l'ID généré
				try ( ResultSet keys = stmt.getGeneratedKeys() ) 
				{
					if ( keys.next() ) client.setId( keys.getInt(1) );
				}
				System.out.println( "Client créé avec succès : " + client.getPseudo() );
				return true;
			}
			
		} 
		catch (SQLException e) 
		{
			System.err.println( "Erreur lors de la création du client : " + client.getPseudo() );
			e.printStackTrace();
		}
		
		return false;
	}
	
	/* -------------------------------- */
	/*               MAJ                */
	/* -------------------------------- */
	/**
	 * Méthode pour mettre à jour un client existant
	 * @param client le client avec les nouvelles informations
	 * @return true si la mise à jour a réussi, false sinon
	 */
	public boolean majClient( Client client ) 
	{
		if ( client == null || client.getId() <= 0 ) return false;
		
		String sql = "UPDATE clients SET pseudo = ?, mdp = ? WHERE id = ?";
		
		try 
		{
			Connection        conn = this.connexionBD.getConnection();
			PreparedStatement stmt = conn.prepareStatement( sql );
			
			stmt.setString ( 1, client.getPseudo () );
			stmt.setString ( 2, client.getMdp    () );
			stmt.setInt    ( 3, client.getId     () );
			
			int lignesRetour = stmt.executeUpdate();
			
			if ( lignesRetour > 0 ) 
			{
				System.out.println("MAJ client : " + client);
				return true;
			} 
			else 
			{
				System.out.println( "Aucun client trouvé avec l'ID : " + client.getId() );
			}
			
		} 
		catch (SQLException e) 
		{
			System.err.println( "Erreur lors de la mise à jour du client : " + client.getId() );
			e.printStackTrace();
		}
		
		return false;
	}

	/**
	 * Authentifie un client avec pseudo/mdp
	 * @param pseudo le pseudo du client
	 * @param mdp le mot de passe
	 * @return le client si authentifié, null sinon
	 */
	public int authenticate( String pseudo, String mdp ) 
	{
		Client client = getClientByPseudo( pseudo );
		if ( client == null ) return -1;
		if ( client.getMdp() != null && client.getMdp().equals( mdp ) ) return getClientId( pseudo);
		return -1;
	}

	/**
	 * Alias pour getClients() - liste tous les clients
	 * @return liste de tous les clients
	 */
	public List<Client> listClients()
	{
		return this.getClients();
	}

	/**
	 * Méthode utilitaire pour mapper une ligne ResultSet vers un objet Client
	 * @param rs le ResultSet positionné sur la ligne à lire
	 * @return l'objet Client correspondant
	 * @throws SQLException en cas d'erreur SQL
	 */
	private Client mapRowToClient( ResultSet rs ) throws SQLException 
	{
		int       id        = rs.getInt      ( "id"         );
		String    pseudo    = rs.getString   ( "pseudo"     );
		String    mdp       = rs.getString   ( "mdp"        );
		Timestamp createdAt = rs.getTimestamp( "created_at" );
		
		return new Client( id, pseudo, mdp, createdAt );
	}

	/**
	 * Méthode de test pour vérifier les liaisons et la connexion à la base
	 * @return true si tout fonctionne correctement
	 */
/*	public boolean testerLiaisons()
	{
		try 
		{
			System.out.println( "=== Test des liaisons ClientsBD ===" );
			
			// Test connexion
			ConnexionBD conn = ConnexionBD.getInstance();
			if ( !conn.testerConnexion() ) 
			{
				System.err.println( "Échec : Connexion à la base de données" );
				return false;
			}
			System.out.println( "✓ Connexion à la base MySQL OK" );
			
			// Test récupération clients
			List<Client> clients = this.getClients();
			System.out.println( "✓ Récupération clients OK (" + clients.size() + " clients trouvés)" );
			
			// Test création d'un client de test (puis suppression)
			Client clientTest = new Client( "test_user_" + System.currentTimeMillis(), "test_pwd" );
			if ( this.createClient( clientTest ) ) 
			{
				System.out.println( "✓ Création client OK (ID: " + clientTest.getId() + ")" );
				
				// Test récupération par ID
				Client retrieved = this.getClientById( clientTest.getId() );
				if ( retrieved != null && retrieved.getPseudo().equals( clientTest.getPseudo() ) )
				{
					System.out.println( "✓ Récupération par ID OK" );
				}
				
				// Test authentification
				Boolean auth = this.authenticate( clientTest.getPseudo(), clientTest.getMdp() );
				if ( auth = true )
				{
					System.out.println( "✓ Authentification OK" );
				}
				
				// Nettoyage : suppression du client de test
				if ( this.supprimerClient( clientTest.getId() ) )
				{
					System.out.println( "✓ Suppression client de test OK" );
				}
			}
			
			System.out.println( "=== Toutes les liaisons fonctionnent correctement ===" );
			return true;
			
		} 
		catch (Exception e) 
		{
			System.err.println( "Erreur lors du test des liaisons : " + e.getMessage() );
			e.printStackTrace();
			return false;
		}
	}
*/
/*-------------------------------------------------*/
/*--------------GESTION MESSAGE--------------------*/
/*-------------------------------------------------*/
/*
	public Boolean sendMessage(int idUser,int groupe_id, String message)
	{
				String sql = "INSERT INTO messages ( groupe_id,expediteur_id,mdp ) VALUES (?, ?, ?)";
		
		try 
		{
			Connection        conn = this.connexionBD.getConnection();
			PreparedStatement stmt = conn.prepareStatement( sql );
			
			// Ajout des paramètres
			stmt.setString ( 1, pseudo );

			ResultSet rs = stmt.executeQuery();
			
			if ( rs.next() ) 
			{
				Client client = mapRowToClient( rs );

				System.out.println( "Client trouvé : " + client );
				return client;
			} 
			else 
			{
				System.out.println( "Aucun client trouvé avec le pseudo : " + pseudo );
			}
			
		} 
		catch (SQLException e) 
		{
			System.err.println( "Erreur lors de la recherche du client pseudo : " + pseudo );
			e.printStackTrace();
		}
		
		return null;
	}
	}


*/

}
