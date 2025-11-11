package server.metier.model;

import java.sql.Timestamp;

/*---------------------------------*/
/*  Class Client                   */
/*---------------------------------*/
/**
 * Classe modèle représentant un client de l'application MateZone.
 * Correspond à la table `clients` en base de données et encapsule
 * les informations d'un utilisateur : identifiant, pseudo, mot de passe
 * et date de création du compte.
 * 
 * Cette classe suit le pattern Entity/POJO pour la persistance des données.
 *
 * @author Prévost Donovan
 * @version V1
 * @date 08/11/25
 */
public class Client 
{
    /*-------------------------------*/
    /* Attributs */
    /*-------------------------------*/

    /**
     * Identifiant unique du client en base de données.
     * Clé primaire auto-générée.
     */
    private int id;

    /**
     * Pseudonyme unique du client utilisé pour la connexion.
     * Doit être unique dans toute l'application.
     */
    private String pseudo;

    /**
     * Mot de passe du client.
     * Note : stocké en clair pour cette version de développement.
     */
    private String mdp;

    /**
     * Date et heure de création du compte client.
     * Généré automatiquement lors de l'insertion en base.
     */
    private Timestamp createdAt;

    /*-------------------------------*/
    /* Constructeurs */
    /*-------------------------------*/

    /**
     * Constructeur par défaut.
     * Utilisé notamment pour la désérialisation depuis la base de données.
     */
    public Client() {
    }

    /**
     * Constructeur complet avec tous les attributs.
     * Utilisé lors de la récupération d'un client existant depuis la base de
     * données.
     * 
     * @param id        identifiant unique du client
     * @param pseudo    pseudonyme du client
     * @param mdp       mot de passe du client
     * @param createdAt date de création du compte
     */
    public Client(int id, String pseudo, String mdp, Timestamp createdAt) {
        this.id = id;
        this.pseudo = pseudo;
        this.mdp = mdp;
        this.createdAt = createdAt;
    }

    /**
     * Constructeur pour la création d'un nouveau client.
     * L'identifiant et la date seront générés automatiquement lors de l'insertion.
     * 
     * @param pseudo pseudonyme souhaité pour le nouveau client
     * @param mdp    mot de passe du nouveau client
     */
    public Client(String pseudo, String mdp) {
        this(0, pseudo, mdp, null);
    }

    /*-------------------------------*/
    /* Accesseurs */
    /*-------------------------------*/

    /**
     * Récupère l'identifiant unique du client.
     * 
     * @return l'identifiant du client
     */
    public int getId() {
        return this.id;
    }

    /**
     * Récupère le pseudonyme du client.
     * 
     * @return le pseudonyme du client
     */
    public String getPseudo() {
        return this.pseudo;
    }

    /**
     * Récupère le mot de passe du client.
     * 
     * @return le mot de passe du client
     */
    public String getMdp() {
        return this.mdp;
    }

    /**
     * Récupère la date de création du compte client.
     * 
     * @return la date de création du compte
     */
    public Timestamp getCreatedAt() {
        return this.createdAt;
    }

    /*-------------------------------*/
    /* Modificateurs */
    /*-------------------------------*/

    /**
     * Définit l'identifiant du client.
     * 
     * @param id le nouvel identifiant du client
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Définit le pseudonyme du client.
     * 
     * @param pseudo le nouveau pseudonyme du client
     */
    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }

    /**
     * Définit le mot de passe du client.
     * 
     * @param mdp le nouveau mot de passe du client
     */
    public void setMdp(String mdp) {
        this.mdp = mdp;
    }

    /**
     * Définit la date de création du compte.
     * 
     * @param createdAt la nouvelle date de création
     */
    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * Représentation textuelle du client pour le débogage et les logs.
     * Affiche toutes les informations du client : identifiant, pseudo, mot de passe
     * et date de création.
     * 
     * @return une chaîne formatée contenant les informations complètes du client
     */
    @Override
    public String toString() 
    {
        return "Client{" +
                "id=" + id +
                ", pseudo='" + pseudo + '\'' +
                ", mdp='" + mdp + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}
