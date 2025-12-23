package server;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpExchange;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import server.bd.ConnexionBD;
import server.bd.repository.*;

import server.Protocol.webSocket.WebSocketMateZone;

import server.metier.interfaces.*;
import server.metier.service.*;

/**
 * Point d'entrée principal du serveur MateZone.
 * Cette classe initialise et orchestre tous les composants serveur de
 * l'application :
 * - Connexion à la base de données
 * - Initialisation des repositories et services métier
 * - Démarrage du serveur WebSocket pour le chat temps réel
 * - Démarrage du serveur HTTP pour servir les avatars utilisateur
 * 
 * Le serveur suit une architecture en couches avec séparation des
 * responsabilités.
 * 
 * @author MateZone Team
 * @author Joshua Hermilly
 * @author Prévost Donovan
 * @version V1
 * @date 08/11/25
 */
public class MainServer
{
	private static final Logger logger = LoggerFactory.getLogger(MainServer.class);

	/** Paramètres des Serveurs */
	private static int PORTMESS;
	private static int PORTWEB;

	/** Chargement statique de la configuration */
	static 
	{
		try 
		{
			Properties props = new Properties();
			FileInputStream fis = new FileInputStream("src/server/config.properties");
			props.load(fis);
			fis.close();
			PORTMESS = Integer.parseInt(props.getProperty("server.port"));
			PORTWEB  = Integer.parseInt(props.getProperty("web.port"   ));
			logger.info("Configuration de la base de données chargée avec succès !");
		} 
		catch (IOException e) 
		{
			logger.error("ERREUR : Impossible de charger le fichier config.properties du serveur!", e);
			System.exit(1);
		}
	}

	/**
	 * Méthode principale du serveur MateZone.
	 * Initialise dans l'ordre : base de données, repositories, services, serveurs
	 * WebSocket et HTTP.
	 * 
	 * @param args arguments de ligne de commande (non utilisés)
	 * @throws Exception si une erreur survient lors de l'initialisation des
	 *                   serveurs
	 */
	public static void main(String[] args) throws Exception 
	{
		/*--------------------------------------------*/
		/* 1) Connexion BD                            */
		/*--------------------------------------------*/
		ConnexionBD bd = ConnexionBD.getInstance();
		logger.info("Connexion BD établie.");

		/*--------------------------------------------*/
		/* 2) Repository                              */
		/*--------------------------------------------*/
		IMessageRepository     messageRepo = new MessageRepository    ();
		IUtilisateurRepository userRepo    = new UtilisateurRepository();
		IChannelRepository     ChanRepo    = new ChannelRepository    ();

		/*--------------------------------------------*/
		/* 3) Services Métier                         */
		/*--------------------------------------------*/
		ClientService userService = new ClientService(userRepo, messageRepo, ChanRepo);
		// MessageService messageService = new MessageService(messageRepo); // si besoin

		/*--------------------------------------------*/
		/* 4) Serveur WebSocket                       */
		/*--------------------------------------------*/
		WebSocketMateZone wsServer = new WebSocketMateZone(MainServer.PORTMESS, userService);
		wsServer.start();
		IWebSocketMateZone iWebSocketMateZone = wsServer;
		userService.setIWebSocketMateZone(iWebSocketMateZone);

		/*--------------------------------------------*/
		/* 5) Serveur HTTP avatar                     */
		/*--------------------------------------------*/
		MainServer.startAvatarServer(userRepo);

		logger.info("Serveur WebSocket démarré sur ws://127.0.0.1:{} et WebServer démarré sur http://127.0.0.1:{}", MainServer.PORTMESS, MainServer.PORTWEB);
	}

	/**
	 * Démarre un serveur HTTP minimaliste pour servir les avatars utilisateur.
	 * Le serveur écoute sur le port 8081 et répond aux requêtes GET /avatar?id=X
	 * en récupérant l'image d'avatar depuis la base de données.
	 * 
	 * @param repo le repository utilisateur pour accéder aux avatars en base de
	 *             données
	 * @throws Exception si une erreur survient lors du démarrage du serveur HTTP
	 */
	public static void startAvatarServer(IUtilisateurRepository repo) throws Exception 
	{
		HttpServer http = HttpServer.create(new InetSocketAddress(MainServer.PORTWEB), 0);

		http.createContext("/avatar", (HttpExchange exchange) -> 
		{
			try 
			{
				String query = exchange.getRequestURI().getQuery(); // ex: id=3
				int id = Integer.parseInt(query.split("=")[1]);

				byte[] img = repo.getAvatarById(id);

				exchange.getResponseHeaders().set("Content-Type", "image/png");

				if (img == null) 
				{
					exchange.sendResponseHeaders(404, -1);
					logger.warn("Avatar non trouvé pour l'id {}", id);
					return;
				}

				exchange.sendResponseHeaders(200, img.length);
				try (OutputStream os = exchange.getResponseBody()) 
				{
					os.write(img);
				}
			} catch (Exception e) {
				exchange.sendResponseHeaders(500, -1);
				logger.error("Erreur lors de la récupération de l'avatar.", e);
			}
		});

		http.start();
		logger.info("Serveur HTTP d'avatar démarré sur le port {}", MainServer.PORTWEB);
	}
}