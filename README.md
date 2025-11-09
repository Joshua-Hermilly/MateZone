# MateMone


# 1.1 Architecture 
```
MateZone/
│
├── common/                               ← Code partagé entre client et serveur
│   ├── dto/
│   │   └── MessageDTO.java               ← Objet message (expéditeur, texte, date)
│   └── protocol/
│       └── ChatEventType.java            ← Enum pour types d’événements (MESSAGE, LOGIN, etc.)
│
├── server/                               ← Module serveur
│   ├── MainServeur.java                  ← Point d’entrée serveur
│   │
│   ├── websocket/                        ← Couche réseau serveur
│   │   └── ChatServerEndpoint.java       ← @ServerEndpoint (WebSocket)
│   │
│   ├── service/                          ← Logique métier serveur
│   │   └── ChatService.java              ← Gestion messages, utilisateurs
│   │
│   └── repository/                       ← Accès données
│       ├── MessageRepository.java        ← CRUD sur messages
│       └── ConnexionBD.java              ← Connexion base de données
│
└── client/                               ← Module client
    ├── MainClient.java                   ← Point d’entrée client
    │
    ├── controleur/                       ← MVC Controller
    │   └── Controleur.java               ← Gestion événements GUI → Modèle
    │
    ├── metier/                           ← Modèle (logique métier côté client)
    │   ├── Metier.java                   ← Gestion état, messages reçus
    │   └── ports/
    │       └── ChatPort.java             ← Interface abstraite vers communication
    │
    ├── comm/                             ← Couche “communication réseau” (propre)
    │   └── WebSocketChatAdapter.java     ← Implémentation ChatPort via WebSocket
    │
    └── ihm/                              ← Vue (GUI)
        ├── IhmGui.java
        ├── frame/
        │   ├── affichage/
        │   │   └── HomeFrame.java
        │   └── connexion/
        │       └── ConnectionFrame.java
        └── panel/
            ├── affichage/
            │   ├── ChatPanel.java
            │   ├── EnvoyerPanel.java
            │   └── MessagePanel.java
            └── connexion/
                ├── SaisieClientPanel.java
                ├── SaisieServeurPanel.java
                └── SortiePanel.java
```

# Développeurs :
## Serveur
-	DNB_DONO0530
## Client
- Yuriko