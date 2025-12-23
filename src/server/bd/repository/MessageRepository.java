package server.bd.repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import server.bd.ConnexionBD;
import server.metier.interfaces.IMessageRepository;


/**
 * Classe responsable de toutes les opérations de base de données sur la tables Message
 * Gère les clients, les messages et l'authentification
 * 
 * @author Prévost Donovan
 * @version V1
 * @date 08/11/25
 */
public class MessageRepository implements IMessageRepository 
{
	private static final Logger logger = LoggerFactory.getLogger(MessageRepository.class);
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
	public MessageRepository()
	{
		this.connexionBD = ConnexionBD.getInstance();
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
	public int sendMessage( int groupe_id, int idUser, String message)
	{
		   String sql = "INSERT INTO messages (groupe_id, expediteur_id, contenu) VALUES (?, ?, ?)";
		   try (PreparedStatement stmt = this.connexionBD.getConnection().prepareStatement(sql)) {
			   stmt.setInt(1, groupe_id);
			   stmt.setInt(2, idUser);
			   stmt.setString(3, message);
			   int lignesAffectees = stmt.executeUpdate();
			   if (lignesAffectees > 0) {
				   try (PreparedStatement stmt2 = this.connexionBD.getConnection().prepareStatement("Select id from messages where groupe_id = ? AND expediteur_id = ? AND contenu = ?")) {
					   stmt2.setInt(1, groupe_id);
					   stmt2.setInt(2, idUser);
					   stmt2.setString(3, message);
					   ResultSet rs = stmt2.executeQuery();
					   if (rs.next()) {
						   int idMsg = rs.getInt("id");
						   logger.info("Message envoyé (id={}) dans le groupe {} par {}", idMsg, groupe_id, idUser);
						   return idMsg;
					   }
					   logger.warn("Message inséré mais non retrouvé dans la base (groupe {}, user {})", groupe_id, idUser);
					   return -1;
				   }
			   } else {
				   logger.warn("Aucune ligne insérée lors de l'envoi du message dans le groupe {} par {}", groupe_id, idUser);
				   return -1;
			   }
		   } catch (SQLException e) {
			   logger.error("Erreur lors de l'envoi du message dans le groupe {} par {}: {}", groupe_id, idUser, e.getMessage(), e);
		   }
		   return -1;
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
		String sql = "SELECT s.id,c.id, c.pseudo, s.contenu, s.envoye_le " +
		             "FROM messages s " +
		             "INNER JOIN clients c ON c.id = s.expediteur_id " +
		             "WHERE groupe_id = ? " +
		             "ORDER BY s.id DESC";
		
		   try (PreparedStatement stmt = this.connexionBD.getConnection().prepareStatement(sql)) {
			   stmt.setInt(1, idchannel);
			   ResultSet rs = stmt.executeQuery();
			   int count = 0;
			   while (rs.next()) {
				   int id = rs.getInt("s.id");
				   String idClient = rs.getString("c.id");
				   String pseudo = rs.getString("pseudo");
				   String contenu = rs.getString("contenu");
				   String date = rs.getTimestamp("envoye_le").toString();
				   String[] messageData = {idClient, pseudo, contenu, date};
				   hsMapMessage.put(id, messageData);
				   count++;
			   }
			   if (count > 0) {
				   logger.info("{} messages récupérés pour le channel {}", count, idchannel);
			   } else {
				   logger.info("Aucun message trouvé pour le channel {}", idchannel);
			   }
			   return hsMapMessage;
		   } catch (SQLException e) {
			   logger.error("Erreur lors de la récupération des messages pour le channel: {}: {}", idchannel, e.getMessage(), e);
		   }
		   return null;
	}

	/**
	 * Récupère un message spécifique par son ID
	 * 
	 * @param idMessage l'ID du message à récupérer
	 * @return un tableau [idClient, pseudo, contenu, date] contenant les informations du message
	 *         null si le message n'existe pas ou en cas d'erreur
	 */
	public String[] getMessage(int idMessage)
	{
		String sql = "SELECT expediteur_id, c.pseudo, contenu, envoye_le FROM messages s INNER JOIN clients c on s.expediteur_id = c.id WHERE s.id = ?;";
		
		   try (PreparedStatement stmt = this.connexionBD.getConnection().prepareStatement(sql)) {
			   stmt.setInt(1, idMessage);
			   ResultSet rs = stmt.executeQuery();
			   if (rs.next()) {
				   String idClient = rs.getString("expediteur_id");
				   String pseudo = rs.getString("c.pseudo");
				   String contenu = rs.getString("contenu");
				   String date = rs.getTimestamp("envoye_le").toString();
				   String[] messageData = {idClient, pseudo, contenu, date};
				   logger.debug("Message récupéré: ID {} ({} : ...)", idMessage, pseudo);
				   return messageData;
			   } else {
				   logger.info("Aucun message trouvé avec l'ID: {}", idMessage);
				   return null;
			   }
		   } catch (SQLException e) {
			   logger.error("Erreur lors de la récupération du message avec l'ID: {}: {}", idMessage, e.getMessage(), e);
		   }
		   return null;
	}
}
