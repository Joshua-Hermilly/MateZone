package server.bd.repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import server.bd.ConnexionBD;
import server.metier.interfaces.IChannelRepository;


/**
 * Classe responsable de toutes les opérations de base de données sur la tables Message
 * Gère les clients, les messages et l'authentification
 * 
 * @author Prévost Donovan
 * @version V1
 * @date 08/11/25
 */
public class ChannelRepository implements IChannelRepository 
{
	// =========================================================================
	// ATTRIBUTS
	// =========================================================================
	
	private static final Logger logger = LoggerFactory.getLogger(ChannelRepository.class);
	
	/** Instance de connexion à la base de données */
	private ConnexionBD connexionBD;
	
	// =========================================================================
	// CONSTRUCTEUR
	// =========================================================================
	
	/**
	 * Constructeur qui initialise la connexion à la base de données MySQL
	 */
	public ChannelRepository()
	{
		this.connexionBD = ConnexionBD.getInstance();
	}

	public boolean CreerMp(int id1, int id2, String nom)
	{
		   if (!this.CheckChannelExiste(nom)) {
			   String sql = "INSERT INTO groupes (nom,type,cree_par) VALUES (?, ?, ?)";
			   try (PreparedStatement stmt = this.connexionBD.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
				   stmt.setString(1, nom);
				   stmt.setString(2, "prive");
				   stmt.setInt(3, id1);
				   int lignesRetour = stmt.executeUpdate();
				   if (lignesRetour > 0) {
					   try (ResultSet keys = stmt.getGeneratedKeys()) {
						   if (keys.next()) {
							   int idChannel = keys.getInt(1);
							   this.ajouterChannelCLient(id1, idChannel, true);
							   this.ajouterChannelCLient(id2, idChannel, true);
							   logger.info("Channel privé '{}' créé entre {} et {} (id={})", nom, id1, id2, idChannel);
							   return true;
						   }
					   }
				   } else {
					   logger.warn("Aucune ligne insérée lors de la création du channel privé '{}'.", nom);
				   }
			   } catch (SQLException e) {
				   logger.error("Erreur lors de la création d'un channel privé '{}': {}", nom, e.getMessage(), e);
			   }
		   } else {
			   logger.info("Channel '{}' existe déjà.", nom);
		   }
		   return false;
	}

	public boolean CheckChannelExiste(String nom)
	{
		   String sql;
		   boolean isPrive = nom.startsWith("prive_");
		   try {
			   if (isPrive) {
				   String[] parts = nom.substring(6).split("_", 2);
				   if (parts.length == 2) {
					   String nom1 = "prive_" + parts[0] + "_" + parts[1];
					   String nom2 = "prive_" + parts[1] + "_" + parts[0];
					   sql = "SELECT COUNT(*) FROM groupes WHERE nom = ? OR nom = ?";
					   try (PreparedStatement stmt = this.connexionBD.getConnection().prepareStatement(sql)) {
						   stmt.setString(1, nom1);
						   stmt.setString(2, nom2);
						   try (ResultSet rs = stmt.executeQuery()) {
							   if (rs.next()) {
								   boolean exists = rs.getInt(1) > 0;
								   logger.debug("Vérification existence channel privé '{}': {}", nom, exists);
								   return exists;
							   }
						   }
					   }
				   }
			   } else {
				   sql = "SELECT COUNT(*) FROM groupes WHERE nom = ?";
				   try (PreparedStatement stmt = this.connexionBD.getConnection().prepareStatement(sql)) {
					   stmt.setString(1, nom);
					   try (ResultSet rs = stmt.executeQuery()) {
						   if (rs.next()) {
							   boolean exists = rs.getInt(1) > 0;
							   logger.debug("Vérification existence channel '{}': {}", nom, exists);
							   return exists;
						   }
					   }
				   }
			   }
		   } catch (SQLException e) {
			   logger.error("Erreur lors de la vérification de l'existence du channel '{}': {}", nom, e.getMessage(), e);
		   }
		   return false;
	}

	public void ajouterChannelCLient(int idClient, int idChannel)
	{
		this.ajouterChannelCLient(idClient, idChannel, false);
	}

	public void ajouterChannelCLient(int idClient, int idChannel, boolean prorpio)
	{
		String sql = "INSERT INTO membres_groupes (groupe_id, client_id, role) VALUES (?, ?, ?)";

		   try (PreparedStatement stmt = this.connexionBD.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
			   stmt.setInt(1, idChannel);
			   stmt.setInt(2, idClient);
			   stmt.setString(3, prorpio ? "proprietaire" : "membre");
			   int rows = stmt.executeUpdate();
			   if (rows > 0) {
				   logger.info("Ajout du client {} au channel {} (role: {})", idClient, idChannel, prorpio ? "proprietaire" : "membre");
			   } else {
				   logger.warn("Aucune ligne insérée lors de l'ajout du client {} au channel {}.", idClient, idChannel);
			   }
		   } catch (SQLException e) {
			   logger.error("Erreur lors de l'ajout d'un client {} au channel {}: {}", idClient, idChannel, e.getMessage(), e);
		   }
	}
}
