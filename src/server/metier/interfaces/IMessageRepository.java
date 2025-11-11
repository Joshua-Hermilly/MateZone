package server.metier.interfaces;

import java.util.HashMap;

/*-------------------------------*/
/* Intrerface IMessageRepository */
/*-------------------------------*/
/**
 * Intefarce qui fait office de passerelle entre le metier et le Repository Message (table message dans le bd).
 * serveur/metier  <->  serveur/BD/MessageRepository
 */
public interface IMessageRepository 
{
	HashMap<Integer, String[]> getMessages(int idchannel);
	String[] getMessage(int idchannel);

	int sendMessage( int groupe_id, int idUser, String message);
}
