package server.metier.interfaces;


/*-----------------------------------*/
/* Intrerface IUtilisateurRepository */
/*-----------------------------------*/
/**
 * Intefarce qui fait office de passerelle entre le metier et le Repository clienr (table message dans le bd).
 * serveur/metier  <->  serveur/BD/UtilisateurRepository
 */
public interface IUtilisateurRepository 
{
	int    authenticate (String pseudo, String mdp);
	byte[] getAvatarById(int    clientId          );
}
