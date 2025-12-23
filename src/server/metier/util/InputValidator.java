package server.metier.util;

/**
 * Utilitaire pour la validation et la sanitation des entrées utilisateur.
 */
public class InputValidator {
	/**
	 * Valide un pseudo : 3-20 caractères, alphanumérique ou underscore.
	 */
	public static boolean isValidPseudo(String pseudo) 
	{
		return pseudo != null && pseudo.matches("^[a-zA-Z0-9_]{3,20}$");
	}


	/**
	 * Valide un message : non nul, non vide, longueur max 500 caractères.
	 */
	public static boolean isValidMessage(String message) 
	{
		return message != null && !message.trim().isEmpty() && message.length() <= 500;
	}

	/**
	 * Valide un mot de passe fort :
	 * - 8 caractères minimum
	 * - 1 majuscule
	 * - 1 minuscule
	 * - 1 chiffre
	 * - 1 caractère spécial
	 */
	public static boolean isStrongPassword(String password) {
		if (password == null) return false;
		// Au moins 8 caractères, 1 majuscule, 1 minuscule, 1 chiffre, 1 spécial
		return password.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^a-zA-Z0-9]).{8,}$");
	}

	/**
	 * Échappe les caractères spéciaux pour éviter le XSS côté client.
	 */
	public static String escapeHtml(String input) 
	{
		if (input == null) return null;
		return input.replace("&", "&amp;")
					.replace("<", "&lt;")
					.replace(">", "&gt;")
					.replace("\"", "&quot;")
					.replace("'", "&#x27;");
	}
}
