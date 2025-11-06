package server.gestionBD;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import server.Client;

/**
 * Classe responsable de toutes les opérations de base de données
 * Gère les clients, les messages et l'authentification
 * 
 * @author MateZone Team
 * @version 1.0
 */
public class Request 
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
	public Request()
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
	public boolean createClient(Client client) 
	{
		if (client == null) 
		{
			return false;
		}
		
		if (this.getClientByPseudo(client.getPseudo()) != null) 
		{
			return false;
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
				return true;
			}
		} 
		catch (SQLException e) 
		{
			System.err.println("Erreur lors de la création du client: " + client.getPseudo());
			e.printStackTrace();
		}
		
		return false;
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
	// MÉTHODES DE GESTION DES MESSAGES
	// =========================================================================
	
	/**
	 * Envoie un message dans un groupe
	 * 
	 * @param idUser l'ID de l'utilisateur expéditeur
	 * @param groupe_id l'ID du groupe destinataire
	 * @param message le contenu du message
	 * @return true si l'envoi a réussi, false sinon, null en cas d'erreur
	 */
	public Boolean sendMessage(int groupe_id, int idUser, String message)
	{
		String sql = "INSERT INTO messages (groupe_id, expediteur_id, contenu) VALUES (?, ?, ?)";
		
		try (PreparedStatement stmt = this.connexionBD.getConnection().prepareStatement(sql))
		{
			stmt.setInt(1, groupe_id);
			stmt.setInt(2, idUser);
			stmt.setString(3, message);

			int lignesAffectees = stmt.executeUpdate();

			if (lignesAffectees > 0) 
			{
				System.out.println("Message envoyé avec succès");
				return true;
			}
			else
			{
				System.out.println("Échec de l'envoi du message");
				return false;
			}
		} 
		catch (SQLException e) 
		{
			System.err.println("Erreur lors de l'envoi du message: " + message);
			e.printStackTrace();
		}
		
		return null;
	}

	/**
	 * Récupère tous les messages d'un canal/groupe
	 * 
	 * @param idchannel l'ID du canal/groupe
	 * @return HashMap avec l'ID du message comme clé et un tableau [pseudo, contenu, date] comme valeur
	 *         null en cas d'erreur
	 */
	public HashMap<Integer, String[]> getMessages(int idchannel)
	{
		HashMap<Integer, String[]> hsMapMessage = new HashMap<>();
		String sql = "SELECT s.id, c.pseudo, s.contenu, s.envoye_le " +
		             "FROM messages s " +
		             "INNER JOIN clients c ON c.id = s.expediteur_id " +
		             "WHERE groupe_id = ? " +
		             "ORDER BY s.id ASC";
		
		try (PreparedStatement stmt = this.connexionBD.getConnection().prepareStatement(sql))
		{
			stmt.setInt(1, idchannel);
			ResultSet rs = stmt.executeQuery();
			
			while (rs.next()) 
			{
				int id = rs.getInt("id");
				String pseudo = rs.getString("pseudo");
				String contenu = rs.getString("contenu");
				String date = rs.getTimestamp("envoye_le").toString();
				
				String[] messageData = {pseudo, contenu, date};
				hsMapMessage.put(id, messageData);
			}
			
			System.out.println("Nombre de messages récupérés: " + hsMapMessage.size());
			return hsMapMessage;
		} 
		catch (SQLException e) 
		{
			System.err.println("Erreur lors de la récupération des messages pour le channel: " + idchannel);
			e.printStackTrace();
		}
		
		return null;
	}
}
