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

	// CONNEXION :
	LOGIN          ( List.of( "pseudo", "mdp"      ) ),
	SIGNUP         ( List.of( "pseudo", "mdp"      ) ),
	SUCCESS_LOGIN  ( List.of( "id"    ,"pseudo"    ) ),
	SUCCESS_SIGNUP ( List.of( "id"    , "pseudo"   ) ),


	//CONNEXION A UN NOUVEAU CHANNEL
	NEW_CHANNEL    ( List.of("idChannel"        ) ),

	// MESSAGE
	MESSAGE        ( List.of( "idClient", "pseudo", "contenu", "date") ),
	MESSAGE_LIST   ( List.of( ) ),
	NEW_MESSAGE    ( List.of( "idClient", "idChannel", "contenu" ) ),
	NEW_MESSAGE_IMG ( List.of( "IdGroupe" , "idClient", "byte" ) ),

	// Autres
	SUCCESS        ( List.of( "message"            ) ),
	ERROR          ( List.of( "message"            ) );

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
	
	public String getKeyIndex ( int index )
	{
		if ( index >= this.getRequiredKeys().size() ) return null;
		return this.getRequiredKeys().get(index);
	}
}
