package server.metier.model;

import java.sql.Timestamp;

/*---------------------------------*/
/*  Class Client                   */
/*---------------------------------*/
/**
 * Représente un client (table `clients`).
 */
public class Client
{
    /*-------------------------------*/
    /* Attributs                     */
    /*-------------------------------*/

    /** Identifiant unique du client */
    private int id;

    /** Pseudo du client (unique) */
    private String pseudo;

    /** Mot de passe (stocké en clair pour l'instant) */
    private String mdp;

    /** Date de création */
    private Timestamp createdAt;

    /*-------------------------------*/
    /* Constructeurs                 */
    /*-------------------------------*/

    /** Constructeur vide */
    public Client() { }

    /** Constructeur complet (avec id) */
    public Client(int id, String pseudo, String mdp, Timestamp createdAt)
    {
        this.id = id;
        this.pseudo = pseudo;
        this.mdp = mdp;
        this.createdAt = createdAt;
    }

    /** Constructeur sans id (avant insertion) */
    public Client(String pseudo, String mdp)
    {
        this(0, pseudo, mdp, null);
    }

    /*-------------------------------*/
    /* Accesseurs                    */
    /*-------------------------------*/

    public int       getId          () { return this.id; }
    public String    getPseudo      () { return this.pseudo; }
    public String    getMdp         () { return this.mdp; }
    public Timestamp getCreatedAt   () { return this.createdAt; }

    /*-------------------------------*/
    /* Modificateurs                 */
    /*-------------------------------*/

    public void setId(int id) { this.id = id; }
    public void setPseudo(String pseudo) { this.pseudo = pseudo; }
    public void setMdp(String mdp) { this.mdp = mdp; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }

    /**
     * toString affichant les informations utiles du client.
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
