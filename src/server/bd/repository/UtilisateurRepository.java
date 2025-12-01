package server.bd.repository;

import java.net.http.WebSocket;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import server.bd.ConnexionBD;
import server.metier.interfaces.IUtilisateurRepository;
import server.metier.model.Client;
import server.metier.util.PasswordUtil;

/*---------------------------------*/
/*  Class UtilisateurRepository    */
/*---------------------------------*/
/**
 * Repository gérant toutes les opérations de base de données liées aux
 * utilisateurs.
 * Implémente les fonctionnalités CRUD pour les clients et gère
 * l'authentification.
 * Cette classe utilise le pattern Repository pour encapsuler l'accès aux
 * données
 * et maintenir la séparation entre la logique métier et la couche de
 * persistance.
 * 
 * @author Prévost Donovan
 * @version V1
 * @date 08/11/25
 */
public class UtilisateurRepository implements IUtilisateurRepository 
{
	/*-------------------------------*/
	/* Attributs                     */
	/*-------------------------------*/

	/**
	 * Instance de connexion à la base de données MySQL.
	 * Utilise le pattern Singleton pour garantir une connexion unique.
	 */
	private ConnexionBD connexionBD;

	/*-------------------------------*/
	/* Constructeur                  */
	/*-------------------------------*/

	/**
	 * Constructeur qui initialise la connexion à la base de données.
	 * Récupère l'instance unique de ConnexionBD via le pattern Singleton.
	 */
	public UtilisateurRepository() 
	{
		this.connexionBD = ConnexionBD.getInstance();
	}

	/*-------------------------------*/
	/* Méthodes de lecture           */
	/*-------------------------------*/

	/**
	 * Récupère l'identifiant unique d'un client à partir de son pseudo.
	 * Effectue une recherche dans la table clients par pseudo.
	 * 
	 * @param pseudo le pseudo du client à rechercher
	 * @return l'ID du client si trouvé, -1 si aucun client ne correspond
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

		} catch (SQLException e) 
		{
			System.err.println("Erreur lors de la récupération de l'ID du client: " + pseudo);
			e.printStackTrace();
		}

		return -1;
	}

	/**
	 * Récupère la liste complète de tous les clients enregistrés.
	 * Les clients sont triés par identifiant croissant pour assurer
	 * un ordre cohérent dans les résultats.
	 * 
	 * @return une liste de tous les clients, ou une liste vide si aucun client
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

		} catch (SQLException e) 
		{
			System.err.println("Erreur lors de la récupération des clients");
			e.printStackTrace();
		}

		return clients;
	}

	/**
	 * Récupère un client spécifique à partir de son identifiant unique.
	 * Utilisé pour les opérations nécessitant un client précis.
	 * 
	 * @param id l'identifiant unique du client
	 * @return l'objet Client correspondant, ou null si aucun client trouvé
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

		} catch (SQLException e) 
		{
			System.err.println("Erreur lors de la recherche du client ID: " + id);
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * Récupère un client à partir de son pseudo unique.
	 * Le pseudo étant un identifiant métier, cette méthode est
	 * fréquemment utilisée lors des connexions et recherches.
	 * 
	 * @param pseudo le pseudo unique du client
	 * @return l'objet Client correspondant, ou null si aucun client trouvé
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

		} catch (SQLException e) 
		{
			System.err.println("Erreur lors de la recherche du client pseudo: " + pseudo);
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * Valide les identifiants de connexion d'un client.
	 * Vérifie que la combinaison pseudo/mot de passe existe dans la base.
	 * Cette méthode est utilisée pour l'authentification des utilisateurs.
	 * Utilise BCrypt pour vérifier le mot de passe de manière sécurisée.
	 * 
	 * @param pseudo le pseudo du client
	 * @param mdp    le mot de passe en clair
	 * @return true si les identifiants sont valides, false s'ils sont incorrects,
	 *         null en cas d'erreur de base de données
	 */
	public Boolean getConnexionValideClient(String pseudo, String mdp) 
	{
		String sql = "SELECT id, pseudo, mdp, created_at FROM clients WHERE pseudo = ?";

		try (PreparedStatement stmt = this.connexionBD.getConnection().prepareStatement(sql)) 
		{
			stmt.setString(1, pseudo);
			ResultSet rs = stmt.executeQuery();

			if (rs.next()) 
			{
				String hashedPassword = rs.getString("mdp");
				
				// Vérification sécurisée du mot de passe avec BCrypt
				if (PasswordUtil.verifyPassword(mdp, hashedPassword))
				{
					Client client = mapRowToClient(rs);
					System.out.println("Client authentifié: " + client.getPseudo());
					return true;
				}
				else
				{
					System.out.println("Mot de passe incorrect pour: " + pseudo);
					return false;
				}
			} 
			else
			{
				System.out.println("Aucun client trouvé avec le pseudo: " + pseudo);
				return false;
			}

		} catch (SQLException e) 
		{
			System.err.println("Erreur lors de la recherche du client: " + pseudo);
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * Alias de la méthode getClients() pour compatibilité d'interface.
	 * Retourne la liste complète des clients enregistrés.
	 * 
	 * @return une liste de tous les clients
	 */
	public List<Client> listClients() 
	{
		return this.getClients();
	}

	/*-----------------------------------*/
	/* Méthodes de création/modification */
	/*-----------------------------------*/

	/**
	 * Crée un nouveau client dans la base de données.
	 * Vérifie l'unicité du pseudo avant l'insertion et génère
	 * automatiquement l'identifiant et la date de création.
	 * 
	 * @param client l'objet Client à créer (doit contenir pseudo et mot de passe)
	 * @return l'ID généré du nouveau client si création réussie, -1 en cas d'échec
	 */
	public int createClient(Client client) 
	{
		if (client == null)
			return -1;
		
		if (this.getClientByPseudo(client.getPseudo()) != null) 
			return -1;

		String sql = "INSERT INTO clients (pseudo, mdp) VALUES (?, ?)";

		try (PreparedStatement stmt = this.connexionBD.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) 
		{
			// Hash du mot de passe avant stockage en base de données
			String hashedPassword = PasswordUtil.hashPassword(client.getMdp());
			
			stmt.setString(1, client.getPseudo());
			stmt.setString(2, hashedPassword);

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

		} catch (SQLException e)
		{
			System.err.println("Erreur lors de la création du client: " + client.getPseudo());
			e.printStackTrace();
		}

		return -1;
	}

	/**
	 * Met à jour les informations d'un client existant.
	 * Modifie le pseudo et/ou le mot de passe d'un client identifié par son ID.
	 * 
	 * @param client l'objet Client contenant les nouvelles données et l'ID existant
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
			// Hash du mot de passe avant mise à jour en base de données
			String hashedPassword = PasswordUtil.hashPassword(client.getMdp());
			
			stmt.setString(1, client.getPseudo());
			stmt.setString(2, hashedPassword);
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

		} catch (SQLException e) 
		{
			System.err.println("Erreur lors de la mise à jour du client: " + client.getId());
			e.printStackTrace();
		}

		return false;
	}

	/*-------------------------------*/
	/* Méthodes d'authentification   */
	/*-------------------------------*/

	/**
	 * Authentifie un client avec ses identifiants de connexion.
	 * Combine la recherche par pseudo et la vérification du mot de passe
	 * pour valider l'identité d'un utilisateur.
	 * Utilise BCrypt pour vérifier le mot de passe de manière sécurisée.
	 * 
	 * @param pseudo le pseudo du client
	 * @param mdp    le mot de passe en clair
	 * @return l'ID du client si authentification réussie, -1 si échec
	 */
	public int authenticate(String pseudo, String mdp) 
	{
		Client client = getClientByPseudo(pseudo);

		if (client == null) 
			return -1;

		// Vérification sécurisée du mot de passe avec BCrypt
		if (client.getMdp() != null && PasswordUtil.verifyPassword(mdp, client.getMdp())) 
			return getClientId(pseudo);
		

		return -1;
	}


	public HashMap<Integer, String> permChannel(int idClient)
	{
		HashMap<Integer, String> permChannel = new HashMap<>();

		String sql = "SELECT mg.groupe_id,g.nom FROM membres_groupes mg JOIN groupes g ON mg.groupe_id = g.id WHERE mg.client_id = ? ;";

		try (PreparedStatement stmt = this.connexionBD.getConnection().prepareStatement(sql)) 
		{
			stmt.setInt(1, idClient);

			ResultSet rs = stmt.executeQuery();
			System.out.println(rs);

			while (rs.next()) 
			{
				int    idchannel  = rs.getInt   ("groupe_id");
				String nomChannel = rs.getString("nom"      );

				permChannel.put(idchannel, nomChannel);
			}
			
			if (permChannel.isEmpty())
			{
				System.out.println("Aucun groupe trouvé pour le client id: " + idClient);
			}

		} catch (SQLException e) 
		{
			System.err.println("Erreur lors de la reche des permission de idclient: " + idClient);
			e.printStackTrace();
		}

		return permChannel;
	}

	/*-------------------------------*/
	/* Méthodes utilitaires          */
	/*-------------------------------*/

	/**
	 * Convertit une ligne de ResultSet en objet Client.
	 * Méthode utilitaire privée pour mapper les données de la base
	 * vers les objets métier. Gère la conversion des types SQL.
	 * 
	 * @param rs le ResultSet positionné sur la ligne à lire
	 * @return l'objet Client correspondant aux données de la ligne
	 * @throws SQLException en cas d'erreur lors de la lecture des données
	 */
	private Client mapRowToClient(ResultSet rs) throws SQLException 
	{
		int       id        = rs.getInt      ("id"        );
		String    pseudo    = rs.getString   ("pseudo"    );
		String    mdp       = rs.getString   ("mdp"       );
		Timestamp createdAt = rs.getTimestamp("created_at");

		return new Client(id, pseudo, mdp, createdAt);
	}

	/*-------------------------------*/
	/* Gestion des avatars           */
	/*-------------------------------*/

	/**
	 * Récupère l'avatar (image de profil) d'un client par son identifiant.
	 * Retourne les données binaires de l'image stockée en base de données.
	 * 
	 * @param clientId l'identifiant du client
	 * @return les données binaires de l'avatar, ou null si aucun avatar ou erreur
	 */
	public byte[] getAvatarById(int clientId) 
	{
		try 
		{
			// La magie opère ici : 
			// 1. On joint la table 'clients' avec 'default_images'
			// 2. On sélectionne l'image perso, SINON l'image par défaut
			String sql = "SELECT COALESCE(c.custom_img_data, d.img_data) as avatar_final " +
						"FROM clients c " +
						"LEFT JOIN default_images d ON c.default_image_id = d.id " +
						"WHERE c.id = ?";

			PreparedStatement st = this.connexionBD.getConnection().prepareStatement(sql);
			st.setInt(1, clientId);
			
			ResultSet rs = st.executeQuery();

			if (rs.next()) {
				// On récupère la colonne virtuelle qu'on a nommée "avatar_final"
				return rs.getBytes("avatar_final");
			}

		} catch (Exception e) { 
			e.printStackTrace(); 
		}

		return null;
	}
}