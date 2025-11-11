# MateZone 🚀

Application de chat temps réel développée en Java utilisant les WebSockets pour la communication client-serveur.

## 📋 Description

MateZone est une application de messagerie instantanée permettant aux utilisateurs de :
- Se connecter avec un pseudo et mot de passe
- S'inscrire pour créer un nouveau compte
- Échanger des messages en temps réel
- Gérer différents canaux de discussion
- Visualiser l'historique des messages

## 🏗️ Architecture

L'application suit une architecture hexagonale (ports/adapters) avec séparation claire entre :

### Client (Architecture MVC)
- **Contrôleur** : Gestion de la logique de présentation
- **IHM** : Interface utilisateur Swing/AWT
- **Métier** : Logique business côté client
- **Infrastructure** : Adaptateurs WebSocket

### Serveur (Architecture en couches)
- **Protocole** : Serveur WebSocket pour communication temps réel
- **Service** : Logique métier et orchestration
- **Repository** : Accès aux données (pattern Repository)
- **Base de données** : Persistance MySQL

### Common
- **DTO** : Objets de transfert de données
- **Protocol** : Énumérations et contrats partagés

```
└───src
    ├───client
    │   │   MainClient.java
    │   │
    │   ├───controleur
    │   │       Controleur.java
    │   │
    │   ├───ihm
    │   │   │   IhmGui.java
    │   │   │
    │   │   ├───frame
    │   │   │   ├───affichage
    │   │   │   │       MateZoneFrame.java
    │   │   │   │
    │   │   │   └───connexion
    │   │   │           ConnexionFrame.java
    │   │   │
    │   │   └───panel
    │   │       ├───affichage
    │   │       │       MessagePanel.java
    │   │       │       SaisieMessagePanel.java
    │   │       │       SalonPanel.java
    │   │       │
    │   │       └───connexion
    │   │               ConnexionPanel.java
    │   │
    │   ├───infrastructure
    │   │   └───websocket
    │   │           WebSocketChatAdapter.java
    │   │
    │   └───metier
    │       │   Metier.java
    │       │
    │       └───interfaces
    │               IEnvoyeur.java
    │               INotifieur.java
    │
    ├───common
    │   ├───dto
    │   │       ChatEventDTO.java
    │   │
    │   └───protocol
    │           EventEnum.java
    │
    └───server
        │   MainServer.java
        │
        ├───bd
        │   │   ConnexionBD.java
        │   │
        │   ├───repository
        │   │       MessageRepository.java
        │   │       UtilisateurRepository.java
        │   │
        │   └───SQL
        │           MateZone.sql
        │
        ├───metier
        │   ├───interfaces
        │   │       IMessageRepository.java
        │   │       IUtilisateurRepository.java
        │   │       IWebSocketMateZone.java
        │   │
        │   ├───model
        │   │       Client.java
        │   │       Message.java
        │   │
        │   └───service
        │           ClientService.java
        │
        └───Protocol
            └───webSocket
                    WebSocketMateZone.java
```

## 🔧 Prérequis

- **Java** : JDK 11 ou supérieur ici JDK 25.
- **Base de données** : MySQL 8.0+
- **Bibliothèques** :
  - Java WebSocket API
  - Gson (sérialisation JSON)
  - MySQL Connector/J

## 📦 Installation

### 1. Cloner le repository
```bash
git clone https://github.com/Joshua-Hermilly/MateZone.git
cd MateZone
```

### 2. Configuration de la base de données
```bash
# Créer la base de données MySQL
mysql -u root -p < src/server/bd/SQL/MateZone.sql
```

### 3. Configuration
Créer un fichier `config.properties` à la racine :
```properties
db.url=jdbc:mysql://localhost:3306/votre_nom_bd
db.username=votre_username
db.password=votre_password
```

### 4. Compilation
```bash
# Compiler le projet
./run.bat
```

## 🚀 Utilisation

### Démarrer le serveur
```bash
java -cp "bin:lib/*" server.MainServer
```

### Démarrer le client
```bash
java -cp "bin:lib/*" client.MainClient
```

## 🔌 Protocole de communication

L'application utilise des WebSockets avec des messages JSON structurés :

### Types d'événements
- `LOGIN` : Authentification utilisateur
- `SIGNUP` : Inscription nouvel utilisateur
- `NEW_MESSAGE` : Envoi de message
- `NEW_CHANNEL` : Création de canal

### Format des messages
```json
{
  "type": "MESSAGE",
  "pseudo": "utilisateur",
  "contenu": "Contenu du message",
  "channel": 1,
  "timestamp": "2024-11-11T10:30:00"
}
```

## 📚 Documentation

La documentation complète est disponible dans le dossier `docs/` :

### Générer la Javadoc
```bash
javadoc -d docs -sourcepath src -subpackages client:server:common \
        -encoding UTF-8 -charset UTF-8 -docencoding UTF-8 \
        -author -version -use -splitindex \
        -windowtitle "MateZone Documentation" \
        -doctitle "Documentation MateZone Chat Application" \
        -cp "lib/*"
```

### Accéder à la documentation
Ouvrir `docs/index.html` dans votre navigateur.

## 🛠️ Technologies utilisées

- **Java Swing/AWT** : Interface utilisateur
- **WebSockets** : Communication temps réel
- **MySQL** : Base de données
- **Gson** : Sérialisation JSON
- **Architecture hexagonale** : Découplage des couches
- **Pattern Repository** : Accès aux données
- **Pattern MVC** : Organisation côté client

## 🔒 Sécurité

- Authentification par pseudo/mot de passe
- Validation des entrées utilisateur
- Gestion des erreurs de connexion
- Isolation des canaux de discussion

<!-- ## 🧪 Tests

```bash
# Lancer les tests unitaires (si disponibles)
java -cp "bin:lib/*:test" org.junit.runner.JUnitCore TestSuite
```

## 📈 Améliorations futures

- [ ] Chiffrement des communications
- [ ] Système de permissions avancées
- [ ] Interface web responsive
- [ ] API REST complémentaire
- [ ] Notifications push
- [ ] Partage de fichiers
- [ ] Historique de messages persistant

## 🤝 Contribution

1. Fork du projet
2. Créer une branche feature (`git checkout -b feature/AmazingFeature`)
3. Commit des modifications (`git commit -m 'Add AmazingFeature'`)
4. Push vers la branche (`git push origin feature/AmazingFeature`)
5. Ouvrir une Pull Request -->

## 📝 Licence

Distribué sous licence MIT. Voir `LICENSE` pour plus d'informations.

## 👥 Développeurs

### Serveur
- **DNB_Dono530** - Développement backend, architecture serveur, base de données

### Client  
- **Yuriko** - Développement frontend, interface utilisateur, UX/UI

## 📧 Contact

- **Repository** : [https://github.com/Joshua-Hermilly/MateZone](https://github.com/Joshua-Hermilly/MateZone)
- **Issues** : [https://github.com/Joshua-Hermilly/MateZone/issues](https://github.com/Joshua-Hermilly/MateZone/issues)

---

⭐ N'hésitez pas à mettre une étoile si ce projet vous plaît !

