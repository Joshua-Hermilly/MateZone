package MateZone.metier.serveur;

import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * Petite utilité pour tester rapidement si un port TCP est joignable.
 */
public class TestConnexion
{
	/**
	 * Tente une connexion TCP courte vers host:port.
	 * @param host hôte
	 * @param port port
	 * @return true si le socket s'est connecté dans le timeout
	 */
	public boolean tentativeConnexion(String host, int port)
	{
		try (Socket s = new Socket())
		{
			s.connect(new InetSocketAddress(host, port), 2000);
			return true;
		}
		catch (Exception e)  { return false; }
	}
}
