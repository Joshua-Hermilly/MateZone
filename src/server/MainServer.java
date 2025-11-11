package server;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpExchange;
import java.io.OutputStream;
import java.net.InetSocketAddress;

import server.bd.ConnexionBD;
import server.bd.repository.*;

import server.Protocol.webSocket.WebSocketMateZone;

import server.metier.interfaces.*;
import server.metier.service.*;

/**
 * Point d'entrée du serveur MateZone.
 * Initialise la BD, les services, le WebSocket et le serveur HTTP avatar.
 */
public class MainServer 
{
	public static void main(String[] args) throws Exception 
	{
		/*--------------------------------------------*/
		/* 1) Connexion BD                            */
		/*--------------------------------------------*/
		ConnexionBD bd = ConnexionBD.getInstance();
		System.out.println("✅ Connexion BD établie.");

		/*--------------------------------------------*/
		/* 2) Repository                              */
		/*--------------------------------------------*/
		IMessageRepository     messageRepo = new MessageRepository();
		IUtilisateurRepository userRepo    = new UtilisateurRepository();

		/*--------------------------------------------*/
		/* 3) Services Métier                         */
		/*--------------------------------------------*/
		ClientService userService = new ClientService(userRepo, messageRepo);
		//MessageService messageService = new MessageService(messageRepo); // si besoin

		/*--------------------------------------------*/
		/* 4) Serveur WebSocket                       */
		/*--------------------------------------------*/
		int websocketPort = 8080;
		WebSocketMateZone wsServer = new WebSocketMateZone(websocketPort, userService);
		wsServer.start();

		System.out.println("Serveur WebSocket démarré sur ws://127.0.0.1:" + websocketPort);

		/*--------------------------------------------*/
		/* 5) Serveur HTTP avatar                     */
		/*--------------------------------------------*/
		MainServer.startAvatarServer(userRepo); // Port 8081
	}


	/**
	 * Démarre un serveur HTTP minimaliste servant les avatars stockés en BD.
	 */
	public static void startAvatarServer(IUtilisateurRepository repo) throws Exception 
	{

		int httpPort = 8081;
		HttpServer http = HttpServer.create(new InetSocketAddress(httpPort), 0);

		http.createContext("/avatar", (HttpExchange exchange) -> {
			try 
			{
				String query = exchange.getRequestURI().getQuery(); // ex: id=3
				int id = Integer.parseInt(query.split("=")[1]);

				byte[] img = repo.getAvatarById(id);

				exchange.getResponseHeaders().set("Content-Type", "image/png");

				if (img == null) {
					exchange.sendResponseHeaders(404, -1);
					return;
				}

				exchange.sendResponseHeaders(200, img.length);
				try (OutputStream os = exchange.getResponseBody()) {
					os.write(img);
				}

			} catch (Exception e) {  exchange.sendResponseHeaders(500, -1);  }
		});

		http.start();
		System.out.println("Serveur HTTP Avatar prêt sur http://127.0.0.1:" + httpPort + "/avatar?id=1");
	}
}
