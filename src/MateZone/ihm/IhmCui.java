package src.ihm;

import src.Controleur;
import src.metier.Joueur;

import java.util.List;
import java.util.Scanner;

/*---------------------------------*/
/*  Class IhmCui                   */
/*---------------------------------*/
/**
 * Interface utilisateur en mode console
 * Responsable uniquement de l'affichage et de la saisie utilisateur
 */
public class IhmCui 
{
	/*-------------------------------*/
	/* Attributs                     */
	/*-------------------------------*/
	/** Controleur */
		private Controleur ctrl;

	/** Scanner pour la saisie utilisateur */
		private Scanner scanner;

	/*-------------------------------*/
	/* Constructeur                  */
	/*-------------------------------*/
	public IhmCui( Controleur ctrl )
	{
		this.ctrl = ctrl;
		this.scanner = new Scanner( System.in );
	}

	/*-------------------------------*/
	/* Méthodes d'affichage          */
	/*-------------------------------*/
	
	/**
	 * Affiche le menu principal et retourne le choix de l'utilisateur
	 * @return Le choix de l'utilisateur
	 */
	public int menu()
	{
		int choix;

		System.out.println ();
		System.out.println ( "      -----------------------" );
		System.out.println ( "                MENU"          );
		System.out.println ( "      -----------------------" );

		System.out.println ();

		System.out.println ( "1. Afficher tous les joueurs"              );
		System.out.println ( "2. Rechercher un joueur par ID"            );
		System.out.println ( "3. Ajouter un nouveau joueur"              );
		System.out.println ( "4. Supprimer un joueur"                    );
		System.out.println ( "5. Lancer le jeu 2D"                       );
		System.out.println ( "0. Quitter"                                );

		System.out.print   ( "      votre choix : " );
		choix = this.scanner.nextInt();
		this.scanner.nextLine(); 

		// Gestion de choix impossibles
		while ( choix < 0 || choix > 5 ) 
		{
			System.out.println  ( "      Erreur de saisie" );
			
			System.out.print   ( "      votre choix : " );
			choix = this.scanner.nextInt();
			this.scanner.nextLine(); 
		}

		System.out.println ();

		return choix;
	}

	
	/**
	 * Affiche la liste des joueurs sous forme de tableau
	 * @param joueurs La liste des joueurs à afficher
	 */
	public void afficherListeJoueurs()
	{
		List<Joueur> lstJoueurs = this.ctrl.getCopieJoueurs();

		System.out.println (  "      -----------------------"  );
		System.out.println (  "       Liste des Joueurs     "  );
		System.out.println (  "      -----------------------"  );
		System.out.println ();

		if ( lstJoueurs == null || lstJoueurs.isEmpty() ) 
		{
			System.out.println("Aucun joueur trouvé.");
			return;
		}

		String sRet = "+----+----------------------+-----+--------+-----+--------+--------+" + "\n" +
		              "| ID | Nom                  | Age | Niveau | Vie | Pos X  | Pos Y  |" + "\n" +
		              "+----+----------------------+-----+--------+-----+--------+--------+" + "\n";

		for ( Joueur joueur : lstJoueurs ) 
		{
			sRet += "| " +
			        String.format( "%-2d" , joueur.getId     () ) + " | "  +
			        String.format( "%-20s", joueur.getNom    () ) + " | "  +
			        String.format( "%3d"  , joueur.getAge    () ) + " | "  +
			        String.format( "%6d"  , joueur.getNiveau () ) + " | "  +
			        String.format( "%3d"  , joueur.getVie    () ) + " | "  +
			        String.format( "%6d"  , joueur.getPosX   () ) + " | "  +
			        String.format( "%6d"  , joueur.getPosY   () ) + " |\n" ;
		}
		
		sRet += "+----+----------------------+-----+--------+-----+--------+--------+";

		System.out.println ( sRet );
	}

	/**
	 * Affiche les détails d'un joueur
	 * @param joueur Le joueur à afficher
	 */
	public void afficherJoueur()
	{
		System.out.println( "      -----------------------" );
		System.out.println( "       Détails du Joueur     " );
		System.out.println( "      -----------------------" );
		System.out.println();

		System.out.print( "ID du joueur à afficher : " );
		int id = this.scanner.nextInt();
		this.scanner.nextLine(); 

		System.out.println();


		Joueur joueur = this.ctrl.getJoueurId( id );

		if (joueur == null) 
		{
			System.out.println("Aucun joueur à afficher.");
			return;
		}

		System.out.println( "Détails du joueur :"                                                  );
		System.out.println( "ID            : " + joueur.getId     ()                               );
		System.out.println( "Nom           : " + joueur.getNom    ()                               );
		System.out.println( "Âge           : " + joueur.getAge    ()                               );
		System.out.println( "Niveau        : " + joueur.getNiveau ()                               );
		System.out.println( "Points de vie : " + joueur.getVie    ()                               );
		System.out.println( "Position      : (" + joueur.getPosX() + ", " + joueur.getPosY() + ")" );
	}


	/**
	 * Affiche l'ajout d'un joueur
	 * @return Le joueur ajouté
	 */
	public Joueur ajouterJoueur()
	{
		System.out.println( "      -----------------------" );
		System.out.println( "       Ajout d'un Joueur     " );
		System.out.println( "      -----------------------" );

		System.out.print( "Nom : " );
		String nom = this.scanner.nextLine();

		System.out.print( "Âge : " );
		int age;
		try { age = this.scanner.nextInt();}
		catch (Exception e) 
		{
			System.out.println("Âge invalide, veuillez entrer un nombre entier.");
			this.scanner.nextLine();
			
			return null;
		}

		this.scanner.nextLine(); 

		Joueur joueur = new Joueur( nom, age );
		this.ctrl.ajouterJoueur( joueur );

		return joueur;
	}


	/**
	 * Affiche la suppression d'un joueur
	 * @param id L'ID du joueur à supprimer
	 */
	public void supprimerJoueur()
	{
		System.out.println( "      ------------------------" );
		System.out.println( "       Suppression d'un Joueur" );
		System.out.println( "      ------------------------" );

		System.out.print( "ID du joueur à supprimer : " );
		int id = this.scanner.nextInt();
		this.scanner.nextLine();

		Joueur joueur = this.ctrl.getJoueurId( id );
		if (joueur == null) 
		{
			System.out.println("Aucun joueur.");
			return;
		}
		System.out.println( "Voulez-vous supprimer le joueur : " + joueur.getNom() + " ? (O/N)" );
		String confirmation = this.scanner.nextLine().trim().toUpperCase();
		if ( !confirmation.equals( "O" )) 
		{
			System.out.println( "Suppression annulée." );
			return;
		}
		System.out.println( "Suppression du joueur : " + joueur.getNom() );

		this.ctrl.supprimerJoueur( id );
	}
}