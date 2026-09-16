using System;
using System.Collections.Generic;
using System.Text.Json;
using System.Text.Json.Serialization;
using MateZone.common.protocol;

namespace MateZone.common.dto
{
    public class ChatEventDTO
    {
        /*--------------------------*/
        /* Attributs                */
        /*--------------------------*/

        //Ici on est obligfé si on veut respecter les convention de nommage C# de forcer la récriture de nos Attributs.
        [JsonPropertyName( "type" )]         public string                     Type { get; set; }
        [JsonPropertyName( "data" )]         public Dictionary<string, object> Data { get; set; }
        [JsonPropertyName( "lstEventDTO" )]  public List<ChatEventDTO>         LstEventDTO { get; set; }

        /*--------------------------*/
        /* Constructeurs            */
        /*--------------------------*/
        public ChatEventDTO()
        {
            this.Data = new Dictionary<string, object>();
        }

        public ChatEventDTO(EventType type) : this()
        {
            this.Type = type.Nom;
        }

        public ChatEventDTO(EventType type, List<ChatEventDTO> lstEventDTO)
        {
            this.LstEventDTO = lstEventDTO;
            this.Type        = type.Nom;
        }

        /*--------------------------*/
        /* Getters et Setters       */
        /*--------------------------*/
        public object GetDataIndex(int index)
        {
            EventType eventType = EventType.EventTypeDepuisNom(this.Type);
            if ( eventType == null ) { return null; }

            IReadOnlyList<string> lstClefs = eventType.GetLstClefs();
            if ( index >= lstClefs.Count ) { return null; }

            string clef = lstClefs[index];
            return this.Data.ContainsKey( clef ) ? this.Data[ clef ] : null;
        }

        public T GetData<T>(int index)
        {
            object valeur = this.GetDataIndex(index);
            if (valeur == null) { return default; }

            if (valeur is JsonElement jsonElement)
            {
                return jsonElement.Deserialize<T>();
            }

            return (T)Convert.ChangeType(valeur, typeof(T));
        }

        public ChatEventDTO Add(string clef, object val)
        {
            this.Data[clef] = val;
            return this;
        }

        /*--------------------------*/
        /* Transformer Json         */
        /*--------------------------*/

        public static ChatEventDTO JsonToEventDTO(string json)
        {
            return JsonSerializer.Deserialize<ChatEventDTO>(json);
        }

        public static ChatEventDTO JsonToLstEventDTO(string json)
        {
            ChatEventDTO mainEvent = JsonSerializer.Deserialize<ChatEventDTO>(json);
            EventType    eventType = EventType.EventTypeDepuisNom(mainEvent.Type);
            return new ChatEventDTO(eventType, mainEvent.LstEventDTO );
        }

        public string ToJson()
        {
            return JsonSerializer.Serialize(this);
        }

        /*--------------------------*/
        /* toString                 */
        /*--------------------------*/

        public override string ToString()
        {
            return $"ChatEventDTO{{type='{this.Type}', data={this.Data}}}";
        }
    }
}