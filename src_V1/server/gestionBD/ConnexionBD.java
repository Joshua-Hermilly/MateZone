package server.gestionBD;

import java.sql.Connection;         // Pour la connexion à la BD
import java.sql.DriverManager;      // Pour le driver JDBC ( dans /lib )
import java.sql.SQLException;       // Pour les exceptions SQL
import java.sql.PreparedStatement;  // Pour exécuter des requêtes
import java.sql.ResultSet;          // Pour les résultats des requêtes

/*---------------------------------*/
/*  Class ConnexionBD              */
/*---------------------------------*/
/**
 * Classe responsable de la gestion des connexions à la base de données PostgreSQL
 * Cette classe est en singleton.
 */
public class ConnexionBD 
{
	/*-------------------------------*/
	/* Attributs                     */
	/*-------------------------------*/

	/** Instance unique de la classe */
		private static ConnexionBD instance;
	
	/** Paramètres de connexion à la base de données */
	/** Configuration pour la base MySQL hébergée sur AlwaysData */
		private static final String URL       = "jdbc:mysql://mysql-dono0530.alwaysdata.net/***REMOVED***zone";
		private static final String USERNAME  = "***REMOVED***";
		private static final String PASSWORD  = "***REMOVED***";

		/** Objet Connection de JDBC pour la connexion à la base de données */
		private Connection connection;
	
		
	/*-------------------------------*/
	/* Constructeurs                 */
	/*-------------------------------*/
	/**
	 * Constructeur privé pour empêcher l'instanciation directe
	 * Force l'utilisation de la méthode getInstance()
	 */
	private ConnexionBD() 
	{
		try 
		{
			// Chargement explicite du driver MySQL JDBC
			// Le fichier mysql-connector-java-x.x.x.jar doit être dans le classpath
			Class.forName( "com.mysql.cj.jdbc.Driver" );
			
			// Connexion à la BD
			this.connection = DriverManager.getConnection( ConnexionBD.URL, ConnexionBD.USERNAME, ConnexionBD.PASSWORD );
			
			System.out.println( "Connexion à la base de données MySQL établie avec succès !" );
			
		} 
		catch (ClassNotFoundException e) // Erreur du driver /lib
		{
			System.out.println( "Erreur : Driver MySQL introuvable !"                            );
			System.out.println( "Vérifiez que le fichier mysql-connector-java-x.x.x.jar est dans le classpath" );
			e.printStackTrace();
		} 
		catch (SQLException e) // Erreur connexion
		{
			System.out.println( "Erreur lors de la connexion à la base de données MySQL !"                     );
			System.out.println( "Vérifiez la connectivité réseau et les paramètres de connexion" );
			e.printStackTrace();
		}
	}
	

	/*-------------------------------*/
	/* Accesseurs                    */
	/*-------------------------------*/
	/**
	 * Méthode pour obtenir l'instance unique de ConnexionBD (Singleton)
	 * @return l'instance de ConnexionBD
	 */
	public static ConnexionBD getInstance() 
	{
		// Instance créer qu'1 fois
		if ( ConnexionBD.instance == null )  ConnexionBD.instance = new ConnexionBD();

		return ConnexionBD.instance;
	}
	
	/**
	 * Méthode pour obtenir l'objet Connection JDBC
	 * @return la connexion à la base de données
	 */
	public Connection getConnection() 
	{
		try 
		{
			// Connextion active ?
			if ( this.connection == null || this.connection.isClosed()) 
			{
				System.out.println("Reconnexion à la base de données...");

				this.connection = DriverManager.getConnection( ConnexionBD.URL, ConnexionBD.USERNAME, ConnexionBD.PASSWORD );
			}
		} 
		catch (SQLException e) 
		{
			System.err.println( "Erreur lors de la vérification de la connexion !" );
			e.printStackTrace();
		}
		return this.connection;
	}
	


	/*-------------------------------*/
	/* Gestion de la connexion       */
	/*-------------------------------*/
	/**
	 * Méthode pour tester la connexion à la base de données
	 * @return true si la connexion fonctionne, false sinon
	 */
	public boolean testerConnexion() 
	{
		try 
		{
			// Teste avec rq 
			PreparedStatement stmt = getConnection().prepareStatement( "SELECT 1" );
			ResultSet         rs   = stmt.executeQuery();
			
			if ( rs.next() ) 
			{
				System.out.println( "Test de connexion réussi !" );
				return true;
			}
			
		} 
		catch (SQLException e) 
		{
			System.err.println( "Test de connexion échoué !" );
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * Méthode pour fermer la connexion 
	 */
	public void fermerConnexion() 
	{
		try 
		{
			if ( this.connection != null && !this.connection.isClosed()) 
			{
				this.connection.close();
				System.out.println( "Connexion fermée" );
			}
		} 
		catch (SQLException e) 
		{
			System.err.println( "Erreur lors de la fermeture de la connexion !" );
			e.printStackTrace();
		}
	}
}
