package server.metier.interfaces;

import server.metier.model.Client;

/*-----------------------------------*/
/* Intrerface IUtilisateurRepository */
/*-----------------------------------*/
/**
 * Intefarce qui fait office de passerelle entre le metier et le Repository clienr (table message dans le bd).
 * serveur/metier  <->  serveur/BD/UtilisateurRepository
 */
public interface IUtilisateurRepository 
{
	int authenticate(String pseudo, String mdp);

	int createClient(Client client            );
}
