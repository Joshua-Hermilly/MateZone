    using System.IO;
    using System.Net.WebSockets;
    using System.Text;
    using MateZone.common.dto;
    using MateZone.metier.interfaces;

    namespace MateZone.infrastructure.websocket
    {
        /*-------------------------------*/
        /* Class WebSocketClient         */
        /*-------------------------------*/
        public class WebSocketClient : IEnvoyeur
        {
            /*-------------------------------*/
            /* Attribus                      */
            /*-------------------------------*/
            private readonly INotifieur _iNotifieur;
        
            // Pour la gestion du WebSocket
            private readonly ClientWebSocket _clientWS;
            private CancellationTokenSource  _cancellationTokenSource;
            private Task                     _tacheAsync;


            /*-------------------------------*/
            /* Consctructeur                 */
            /*-------------------------------*/
            public WebSocketClient( INotifieur iNotifieur )
            {
                this._iNotifieur = iNotifieur;

                this._cancellationTokenSource = new CancellationTokenSource();
                this._clientWS                = new ClientWebSocket();
            }

            /*-------------------------------*/
            /* Méthodes                      */
            /*-------------------------------*/
            public bool EstConnectee() { return this._clientWS.State == WebSocketState.Open; }

            public async Task  ConnexionServeur( string uriString )
            {
                Uri uri = new Uri( uriString );
                await this._clientWS.ConnectAsync( uri, this._cancellationTokenSource.Token );

                this._tacheAsync = EcouteMessagesAsync();

            }

            public async Task DeconnexionServeur()
            {
                this._cancellationTokenSource.Cancel();
                await this._tacheAsync;
                await this._clientWS.CloseAsync( WebSocketCloseStatus.NormalClosure, null, this._cancellationTokenSource.Token );
            }

            public async Task EnvoyerMessageAsync( ChatEventDTO eventDTO )
            {
                Console.WriteLine( "EnvoyerMessageAsync : " + eventDTO   );    

                byte[]               tabByteMessage = Encoding.UTF8.GetBytes  ( eventDTO.ToJson() );
                ArraySegment< byte > message        = new ArraySegment< byte >( tabByteMessage    );

                await this._clientWS.SendAsync(message, WebSocketMessageType.Text, true, this._cancellationTokenSource.Token );
            }

            public async Task EcouteMessagesAsync()
            {
                byte[] tabByteMessage;
                ArraySegment<byte> message;

                // Boucle sur l'attente de message quand le client est ouvert
                while (this._clientWS.State == WebSocketState.Open &&
                        !this._cancellationTokenSource.IsCancellationRequested)
                {
                    tabByteMessage = new byte[4096];
                    message = new ArraySegment<byte>(tabByteMessage);

                    // Récup du message
                    WebSocketReceiveResult resultat = await _clientWS.ReceiveAsync(message, this._cancellationTokenSource.Token); // on récup le message et la taille de fin avec resultat.Count

                    // Construction du message ( si c'est le resultat est pas fini ) -- Ici, on créer au cas ou pour éviter de le gérer après
                    MemoryStream msMessages = new MemoryStream();
                    msMessages.Write(message.Array, 0, resultat.Count);

                    // Boucler sur l'attente de fin du message
                    while (resultat.EndOfMessage != true)
                    {
                        resultat = await this._clientWS.ReceiveAsync(message, this._cancellationTokenSource.Token);
                        msMessages.Write(message.Array, 0, resultat.Count);
                    }

                    // Construction de l'event
                    byte[] msgComplet     = msMessages.ToArray();
                    string msgJson        = Encoding.UTF8.GetString(msgComplet, 0, msgComplet.Length);
                    ChatEventDTO eventDTO = ChatEventDTO.JsonToEventDTO(msgJson);

                    //DEBUG
                    Console.WriteLine( "EcouteMessagesAsync : " + eventDTO.ToJson() );

                    // Envoie au Controleur
                    this._iNotifieur.NotifierMessage(eventDTO);
                }
            }
        }
    }
