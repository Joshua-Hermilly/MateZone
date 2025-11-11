package server.bd.repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import server.bd.ConnexionBD;
import server.metier.interfaces.IMessageRepository;
import server.metier.interfaces.IUtilisateurRepository;
import server.metier.model.Client;

/**
 * Classe responsable de toutes les opérations de base de données
 * Gère les clients, les messages et l'authentification
 * 
 * @author MateZone Team
 * @version 1.0
 */
public class UtilisateurRepository implements IUtilisateurRepository
{
	// =========================================================================
	// ATTRIBUTS
	// =========================================================================
	
	/** Instance de connexion à la base de données */
	private ConnexionBD connexionBD;
	
	// =========================================================================
	// CONSTRUCTEUR
	// =========================================================================
	
	/**
	 * Constructeur qui initialise la connexion à la base de données MySQL
	 */
	public UtilisateurRepository()
	{
		this.connexionBD = ConnexionBD.getInstance();
	}

	// =========================================================================
	// MÉTHODES DE RÉCUPÉRATION - CLIENTS
	// =========================================================================
	
	/**
	 * Récupère l'ID d'un client à partir de son pseudo
	 * 
	 * @param pseudo le pseudo du client
	 * @return l'ID du client, ou -1 si non trouvé
	 */
	public int getClientId(String pseudo)
	{
		String sql = "SELECT id FROM clients WHERE pseudo = ?";

		try (PreparedStatement stmt = this.connexionBD.getConnection().prepareStatement(sql))
		{
			stmt.setString(1, pseudo);
			ResultSet rs = stmt.executeQuery();

			if (rs.next()) 
			{
				return rs.getInt("id");
			}
		} 
		catch (SQLException e) 
		{
			System.err.println("Erreur lors de la récupération de l'ID du client: " + pseudo);
			e.printStackTrace();
		}
		
		return -1;
	}

	/**
	 * Récupère tous les clients de la base de données
	 * 
	 * @return liste de tous les clients, triés par ID
	 */
	public List<Client> getClients() 
	{
		List<Client> clients = new ArrayList<>();
		String sql = "SELECT id, pseudo, mdp, created_at FROM clients ORDER BY id";
		
		try (PreparedStatement stmt = this.connexionBD.getConnection().prepareStatement(sql);
		     ResultSet rs = stmt.executeQuery())
		{
			while (rs.next()) 
			{
				Client client = mapRowToClient(rs);
				clients.add(client);
			}

			System.out.println("Nombre de clients récupérés: " + clients.size());		
		} 
		catch (SQLException e) 
		{
			System.err.println("Erreur lors de la récupération des clients");
			e.printStackTrace();
		}
		
		return clients;
	}
	
	/**
	 * Récupère un client à partir de son ID
	 * 
	 * @param id l'identifiant du client
	 * @return le client trouvé, ou null si non trouvé
	 */
	public Client getClientById(int id) 
	{
		String sql = "SELECT id, pseudo, mdp, created_at FROM clients WHERE id = ?";
		
		try (PreparedStatement stmt = this.connexionBD.getConnection().prepareStatement(sql))
		{
			stmt.setInt(1, id);
			ResultSet rs = stmt.executeQuery();
			
			if (rs.next()) 
			{
				Client client = mapRowToClient(rs);
				System.out.println("Client trouvé: " + client);
				return client;
			} 
			else 
			{
				System.out.println("Aucun client trouvé avec l'ID: " + id);
			}
		} 
		catch (SQLException e) 
		{
			System.err.println("Erreur lors de la recherche du client ID: " + id);
			e.printStackTrace();
		}
		
		return null;
	}

	/**
	 * Récupère un client à partir de son pseudo
	 * 
	 * @param pseudo le pseudo du client
	 * @return le client trouvé, ou null si non trouvé
	 */
	public Client getClientByPseudo(String pseudo)
	{
		String sql = "SELECT id, pseudo, mdp, created_at FROM clients WHERE pseudo = ?";
		
		try (PreparedStatement stmt = this.connexionBD.getConnection().prepareStatement(sql))
		{
			stmt.setString(1, pseudo);
			ResultSet rs = stmt.executeQuery();
			
			if (rs.next()) 
			{
				Client client = mapRowToClient(rs);
				System.out.println("Client trouvé: " + client);
				return client;
			} 
			else 
			{
				System.out.println("Aucun client trouvé avec le pseudo: " + pseudo);
			}
		} 
		catch (SQLException e) 
		{
			System.err.println("Erreur lors de la recherche du client pseudo: " + pseudo);
			e.printStackTrace();
		}
		
		return null;
	}

	/**
	 * Vérifie si les identifiants de connexion d'un client sont valides
	 * 
	 * @param pseudo le pseudo du client
	 * @param mdp le mot de passe du client
	 * @return true si les identifiants sont valides, false sinon, null en cas d'erreur
	 */
	public Boolean getConnexionValideClient(String pseudo, String mdp)
	{
		String sql = "SELECT id, pseudo, mdp, created_at FROM clients WHERE pseudo = ? AND mdp = ?";
		
		try (PreparedStatement stmt = this.connexionBD.getConnection().prepareStatement(sql))
		{
			stmt.setString(1, pseudo);
			stmt.setString(2, mdp);
			ResultSet rs = stmt.executeQuery();
			
			if (rs.next()) 
			{
				Client client = mapRowToClient(rs);
				System.out.println("Client trouvé: " + client);
				return true;
			} 
			else 
			{
				System.out.println("Aucun client trouvé avec le pseudo et le mdp: " + pseudo);
				return false;
			}
		} 
		catch (SQLException e) 
		{
			System.err.println("Erreur lors de la recherche du client (pseudo+mdp): " + pseudo);
			e.printStackTrace();
		}
		
		return null;
	}
	
	/**
	 * Retourne la liste complète des clients (alias de getClients)
	 * 
	 * @return liste de tous les clients
	 */
	public List<Client> listClients()
	{
		return this.getClients();
	}

	// =========================================================================
	// MÉTHODES DE CRÉATION ET MODIFICATION - CLIENTS
	// =========================================================================
	
	/**
	 * Crée un nouveau client dans la base de données
	 * 
	 * @param client le client à créer
	 * @return true si la création a réussi, false sinon
	 */
	public int createClient(Client client) 
	{
		if (client == null) 
		{
			return -1;
		}
		
		if (this.getClientByPseudo(client.getPseudo()) != null) 
		{
			return -1;
		}

		String sql = "INSERT INTO clients (pseudo, mdp) VALUES (?, ?)";
		
		try (PreparedStatement stmt = this.connexionBD.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS))
		{
			stmt.setString(1, client.getPseudo());
			stmt.setString(2, client.getMdp());

			int lignesRetour = stmt.executeUpdate();
			
			if (lignesRetour > 0) 
			{
				// Récupération de l'ID généré
				try (ResultSet keys = stmt.getGeneratedKeys()) 
				{
					if (keys.next()) 
					{
						client.setId(keys.getInt(1));
					}
				}
				System.out.println("Client créé avec succès: " + client.getPseudo());
				return client.getId();
			}
		} 
		catch (SQLException e) 
		{
			System.err.println("Erreur lors de la création du client: " + client.getPseudo());
			e.printStackTrace();
		}
		
		return -1;
	}
	
	/**
	 * Met à jour les informations d'un client existant
	 * 
	 * @param client le client avec les nouvelles informations
	 * @return true si la mise à jour a réussi, false sinon
	 */
	public boolean majClient(Client client) 
	{
		if (client == null || client.getId() <= 0) 
		{
			return false;
		}
		
		String sql = "UPDATE clients SET pseudo = ?, mdp = ? WHERE id = ?";
		
		try (PreparedStatement stmt = this.connexionBD.getConnection().prepareStatement(sql))
		{
			stmt.setString(1, client.getPseudo());
			stmt.setString(2, client.getMdp());
			stmt.setInt(3, client.getId());
			
			int lignesRetour = stmt.executeUpdate();
			
			if (lignesRetour > 0) 
			{
				System.out.println("Mise à jour du client: " + client);
				return true;
			} 
			else 
			{
				System.out.println("Aucun client trouvé avec l'ID: " + client.getId());
			}
		} 
		catch (SQLException e) 
		{
			System.err.println("Erreur lors de la mise à jour du client: " + client.getId());
			e.printStackTrace();
		}
		
		return false;
	}

	// =========================================================================
	// MÉTHODES D'AUTHENTIFICATION
	// =========================================================================
	
	/**
	 * Authentifie un client avec son pseudo et mot de passe
	 * 
	 * @param pseudo le pseudo du client
	 * @param mdp le mot de passe
	 * @return l'ID du client si authentifié, -1 sinon
	 */
	public int authenticate(String pseudo, String mdp) 
	{
		Client client = getClientByPseudo(pseudo);
		
		if (client == null) 
		{
			return -1;
		}
		
		if (client.getMdp() != null && client.getMdp().equals(mdp)) 
		{
			return getClientId(pseudo);
		}
		
		return -1;
	}

	// =========================================================================
	// MÉTHODES UTILITAIRES - CLIENTS
	// =========================================================================
	
	/**
	 * Convertit une ligne de ResultSet en objet Client
	 * 
	 * @param rs le ResultSet positionné sur la ligne à lire
	 * @return l'objet Client correspondant
	 * @throws SQLException en cas d'erreur SQL
	 */
	private Client mapRowToClient(ResultSet rs) throws SQLException 
	{
		int id = rs.getInt("id");
		String pseudo = rs.getString("pseudo");
		String mdp = rs.getString("mdp");
		Timestamp createdAt = rs.getTimestamp("created_at");
		
		return new Client(id, pseudo, mdp, createdAt);
	}

	// =========================================================================
	// MÉTHODES GESTION DES IMAGES - CLIENTS
	// =========================================================================

	    public byte[] getAvatarById(int clientId) 
    {
        try 
        {
            String sql = "SELECT img_data FROM clients WHERE id = ?";
            PreparedStatement st = this.connexionBD.getConnection().prepareStatement(sql);

            st.setInt(1, clientId);
            ResultSet rs = st.executeQuery();

            if (rs.next()) { return rs.getBytes("img_data");  }

        } catch (Exception e) { e.printStackTrace(); }
        
        return null;
    }
}