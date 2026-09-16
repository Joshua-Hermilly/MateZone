using System;
using System.Collections.Generic;
using System.Linq;

namespace MateZone.common.protocol
{
    public sealed class EventType
    {
        /*--------------------------*/
        /* Attributs/Propriétées    */
        /*--------------------------*/
        public string Nom { get; }
        private        readonly List<string>    _lstClefs;
        private static readonly List<EventType> _lstEvent = new();

        /*--------------------------*/
        /* "ENUM"/"TYPE" d'EVENT    */
        /*--------------------------*/
        public static readonly EventType LOGIN           = new( "LOGIN"          , new() { "pseudo"   , "mdp" }                       );
        public static readonly EventType SIGNUP          = new( "SIGNUP"         , new() { "pseudo"   , "mdp" }                       );
        public static readonly EventType SUCCESS_LOGIN   = new( "SUCCESS_LOGIN"  , new() { "id"       , "pseudo" }                    );
        public static readonly EventType SUCCESS_SIGNUP  = new( "SUCCESS_SIGNUP" , new() { "id"       , "pseudo" }                    );
        public static readonly EventType NEW_CHANNEL     = new( "NEW_CHANNEL"    , new() { "idChannel" }                              );
        public static readonly EventType MESSAGE         = new( "MESSAGE"        , new() { "idClient" , "pseudo", "contenu", "date" } );
        public static readonly EventType MESSAGE_LIST    = new( "MESSAGE_LIST"   , new List<string>()                                 );
        public static readonly EventType NEW_MESSAGE     = new( "NEW_MESSAGE"    , new() { "idClient" , "idChannel", "contenu" }      );
        public static readonly EventType NEW_MESSAGE_IMG = new( "NEW_MESSAGE_IMG", new() { "idGroupe", "idClient", "byte" }           );
        public static readonly EventType SUCCESS         = new( "SUCCESS"        , new() { "message" }                                );
        public static readonly EventType ERROR           = new( "ERROR"          , new() { "message" }                                );
        public static readonly EventType PERMS_CHANNELS  = new( "PERMS_CHANNELS" , new List<string>()                                 );
        public static readonly EventType PERM_CHANNEL    = new( "PERM_CHANNEL"   , new() { "idChannel", "nomChannel" }                );
        public static readonly EventType CHANGER_CHANNEL = new( "CHANGER_CHANNEL", new() { "idChannel" }                              );
        
        /*--------------------------*/
        /* Constructeur             */
        /*--------------------------*/
        private EventType( string nom , List<string> lstClefs )
        {
            this.Nom       = nom;
            this._lstClefs = lstClefs;
            EventType._lstEvent.Add( this );
        }

        /*--------------------------*/
        /* Accesseurs               */
        /*--------------------------*/
        public IReadOnlyList<string> GetLstClefs() => this._lstClefs;

        public string GetClefIndex( int index )
        {
            if ( index < 0 || index >= this._lstClefs.Count ) { return null; }
            return this._lstClefs[ index ];
        }

        public static EventType EventTypeDepuisNom( string nom )
        {
            if ( nom == null ) { return null; }

            foreach ( EventType eventType in EventType._lstEvent )
            {
                if ( eventType.Nom.Equals( nom ) ) { return eventType; }
            }

            return null;
        }
    }
}
