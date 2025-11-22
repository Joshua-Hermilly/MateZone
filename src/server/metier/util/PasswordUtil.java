package server.metier.util;

import org.mindrot.jbcrypt.BCrypt;

/**
 * Classe utilitaire pour la gestion sécurisée des mots de passe.
 * Utilise BCrypt pour hasher et vérifier les mots de passe de manière sécurisée.
 * 
 * BCrypt est un algorithme de hashage adaptatif qui :
 * - Génère un salt aléatoire unique pour chaque mot de passe
 * - Est résistant aux attaques par force brute grâce à son coût de calcul
 * - Inclut automatiquement le salt dans le hash généré
 * 
 * @author MateZone Team
 * @version V1
 * @date 21/11/25
 */
public class PasswordUtil 
{
	/**
	 * Nombre de rounds pour l'algorithme BCrypt.
	 * Plus le nombre est élevé, plus le hashage est sécurisé mais lent.
	 * Valeur recommandée : 10-12 (12 = ~250ms par hash)
	 */
	private static final int BCRYPT_ROUNDS = 12;

	/**
	 * Constructeur privé pour empêcher l'instanciation.
	 * Cette classe ne contient que des méthodes statiques utilitaires.
	 */
	private PasswordUtil() 
	{
		throw new UnsupportedOperationException("Classe utilitaire, ne peut pas être instanciée");
	}

	/**
	 * Hash un mot de passe en clair avec BCrypt.
	 * Génère automatiquement un salt aléatoire unique.
	 * 
	 * @param plainPassword le mot de passe en clair à hasher
	 * @return le hash BCrypt du mot de passe (inclut le salt)
	 * @throws IllegalArgumentException si le mot de passe est null ou vide
	 */
	public static String hashPassword(String plainPassword) 
	{
		if (plainPassword == null || plainPassword.trim().isEmpty()) 
		{
			throw new IllegalArgumentException("Le mot de passe ne peut pas être vide");
		}

		return BCrypt.hashpw(plainPassword, BCrypt.gensalt(BCRYPT_ROUNDS));
	}

	/**
	 * Vérifie qu'un mot de passe en clair correspond à un hash BCrypt.
	 * Utilise l'algorithme BCrypt pour comparer de manière sécurisée.
	 * 
	 * @param plainPassword le mot de passe en clair à vérifier
	 * @param hashedPassword le hash BCrypt stocké en base de données
	 * @return true si le mot de passe correspond au hash, false sinon
	 * @throws IllegalArgumentException si l'un des paramètres est null ou vide
	 */
	public static boolean verifyPassword(String plainPassword, String hashedPassword) 
	{
		if (plainPassword == null || plainPassword.trim().isEmpty()) 
		{
			throw new IllegalArgumentException("Le mot de passe ne peut pas être vide");
		}

		if (hashedPassword == null || hashedPassword.trim().isEmpty()) 
		{
			throw new IllegalArgumentException("Le hash ne peut pas être vide");
		}

		try 
		{
			return BCrypt.checkpw(plainPassword, hashedPassword);
		} 
		catch (IllegalArgumentException e) 
		{
			// Hash invalide ou mal formaté
			System.err.println("Erreur lors de la vérification du mot de passe : hash invalide");
			return false;
		}
	}

	/**
	 * Vérifie si un hash donné est un hash BCrypt valide.
	 * Utile pour détecter les anciens mots de passe non hashés en base.
	 * 
	 * @param hash le hash à vérifier
	 * @return true si c'est un hash BCrypt valide, false sinon
	 */
	public static boolean isBCryptHash(String hash) 
	{
		if (hash == null || hash.trim().isEmpty()) 
		{
			return false;
		}

		// Un hash BCrypt commence toujours par $2a$, $2b$ ou $2y$
		return hash.startsWith("$2a$") || hash.startsWith("$2b$") || hash.startsWith("$2y$");
	}
}
