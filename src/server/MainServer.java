package server;

import server.bd.ConnexionBD;
import server.bd.repository.*;

import server.Protocol.webSocket.WebSocketMateZone;

import server.metier.interfaces.*;
import server.metier.service.*;


public class MainServer 
{
	public static void main(String[] args) 
	{

		ConnexionBD            bd           = ConnexionBD.getInstance();

		IMessageRepository     iMesRepo     = new MessageRepository();
		IUtilisateurRepository iUserRepo    = new UtilisateurRepository();

		ClientService         UserService   = new ClientService (iUserRepo, iMesRepo);
		//MessageService         MesService   = new MessageService(iMesRepo);

		int port = 8080;

		WebSocketMateZone WebSocketMateZone = new WebSocketMateZone( port, UserService);

		WebSocketMateZone.start(); // Création et démarrage du serveur
		System.out.println("Serveur démarré sur le port " + port);
	}
}
