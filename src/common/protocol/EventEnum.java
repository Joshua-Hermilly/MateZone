package common.protocol;

import java.util.List;

/*-------------------------------*/
/* Enum EventEnum                */
/*-------------------------------*/
/**
 * Enum responable des clées et paramètres des ChatEventDTO.
 * Elle permet d'avoir des clèes et paramètres centralisé entre le Client et le Serveur.
 */
public enum EventEnum 
{
	/*--------------------------*/
	/*       Constantes         */
	/*--------------------------*/
	LOGIN   ( List.of( "pseudo", "mdp"     ) ),
    SIGNUP  ( List.of( "pseudo", "mdp"     ) ),
    MESSAGE ( List.of( "pseudo", "contenu" ) ),
    ERROR   ( List.of("message"        ) ),
    SUCCESS ( List.of("message"        ) );

	/*--------------------------*/
	/*        Attribut          */
	/*--------------------------*/
	private final List<String> composants;

	/*--------------------------*/
	/*      Constructeur        */
	/*--------------------------*/
    EventEnum( List<String> composants ) 
	{
        this.composants = composants;
    }

	/*--------------------------*/
	/*        Getter            */
	/*--------------------------*/
    public List<String> getRequiredKeys() { return this.composants; }
}
