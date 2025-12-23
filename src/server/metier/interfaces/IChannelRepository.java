package server.metier.interfaces;

import java.util.HashMap;

/*-------------------------------*/
/* Interface IChannelRepository  */
/*-------------------------------*/
/**
 * Interface définissant les opérations de persistance pour les messages de
 * chat.
 * Cette interface fait partie de l'architecture hexagonale côté serveur et
 * définit
 * le contrat pour l'accès aux données de messages (table messages en base de
 * données).
 * Elle permet de découpler la logique métier de l'implémentation de
 * persistance.
 * 
 * Couche métier ↔ Repository messages (Base de données)
 *
 * @author Prévost Donovan
 * @version V1
 * @date 08/11/25
 */
public interface IChannelRepository 
{
	boolean CreerMp(int id1, int id2, String nom); 

	void    ajouterChannelCLient(int idClient, int idChannel);
}
