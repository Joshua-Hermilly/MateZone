package client.metier.interfaces;

import common.dto.ChatEventDTO;

/*-------------------------------*/
/* Intrerface INotifieur         */
/*-------------------------------*/
/**
 * Intefarce qui fait office de passerelle entre serveur le controleur.
 * client/infrastructure/websocket/WebSocketChatAdapter  -> client/controleur/Controleur
 */
public interface INotifieur
{
	void notifierMessage( ChatEventDTO eventDTO );

	void succesLogin( String pseudo );

	void afficherListMessage( ChatEventDTO eventDTO );
	void afficherNvMessage  ( ChatEventDTO eventDTO );

	void notifierErreur( String erreur );
}
