# MateZone 🚀

Application de chat temps réel développée en Java utilisant les WebSockets pour la communication client-serveur.
<br>Prochainement CSharp.

## 📋 Description

MateZone est une application de messagerie instantanée permettant aux utilisateurs de :
- Se connecter avec un pseudo et mot de passe
- S'inscrire pour créer un nouveau compte
- Échanger des messages texte et images en temps réel
- Créer et gérer des groupes de discussion
- Rejoindre différents canaux de discussion (groupes publics et conversations privées)
- Visualiser l'historique complet des messages
- Personnaliser leur profil avec une image

<div align="center">

[![Site Web](https://img.shields.io/badge/🌐_Site_Web-www.m8yuriko.fr-blue?style=for-the-badge)](http://www.m8yuriko.fr)
[![Version Beta](https://img.shields.io/badge/🚀_Version_Beta-pre--releases--beta-orange?style=for-the-badge)](https://github.com/Joshua-Hermilly/MateZone/tree/pre-relseases-beta)

</div>

## 📸 Aperçu

| **Connexion** | Logo |
|:---:|:---:|
| <img width="400" alt="Connexion" src="https://github.com/user-attachments/assets/07de1d91-0a52-41c0-845f-1e297726ea05" /> | <img width="400" alt="MateZone" src="https://github.com/user-attachments/assets/7421b893-8067-43e5-a758-623f9d10521e" /> |
| **Salon Public** | **Conversation Privée** |
| <img width="400" alt="Salon Public" src="https://github.com/user-attachments/assets/bbc5faf0-9fb4-4ae4-a89a-f17c03e84109" /> | <img width="400" alt="Privé" src="https://github.com/user-attachments/assets/e32a4532-261a-4a2f-9e5e-a9b90a5e6a15" /> |

## 🏗️ Architecture

L'application suit une architecture hexagonale (ports/adapters) avec séparation claire entre :

### Client (Architecture MVC)
- **Contrôleur** : Gestion de la logique de présentation
- **IHM** : Interface utilisateur Swing/AWT
- **Métier** : Logique business côté client
- **Infrastructure** : Adaptateurs WebSocket

### Serveur (Architecture en couches)
- **Protocole** : Serveur WebSocket pour communication temps réel
- **Service** : Logique métier et orchestration (gestion des clients, messages, groupes)
- **Repository** : Accès aux données (pattern Repository pour utilisateurs et messages)
- **Base de données** : Persistance MySQL avec support des images (MEDIUMBLOB)
- **Modèles** : Entités métier (Client, Message, Groupe, Membre)

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
    │   ├───img
    │   │       (ressources images de l'interface)
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
        │   │
        │   └───service
        │           ClientService.java
        │
        └───Protocol
            └───webSocket
                    WebSocketMateZone.java
```

## 🔧 Prérequis

- **Java** : JDK 11 ou supérieur (recommandé : JDK 25)
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

⚠️ **Important** : Le fichier `config.properties` contient des informations sensibles et ne doit **JAMAIS** être commité sur GitHub.

```bash
# Copier le fichier d'exemple dans le répertoire serveur-exec
cd serveur-exec
cp config.properties.example config.properties
```

Éditer le fichier `serveur-exec/config.properties` avec vos propres valeurs :
```properties
db.url=jdbc:mysql://localhost:3306/votre_nom_bd
db.username=votre_username
db.password=votre_password
```

**Note de sécurité** : Le fichier `.gitattributes` est configuré pour empêcher l'affichage des modifications de `config.properties.example` dans l'historique git, évitant ainsi l'exposition accidentelle de mots de passe.

### 4. Compilation
```powershell
# Compiler le projet (Windows)
.\run.bat
```

## 🚀 Utilisation

### Démarrer le serveur
```powershell
java -cp "bin;lib/*" server.MainServer
```

### Démarrer le client
```powershell
java -cp "bin;lib/*" client.MainClient
```

## 🔌 Protocole de communication

L'application utilise des WebSockets avec des messages JSON structurés :

### Types d'événements
- `LOGIN` : Authentification utilisateur
- `SIGNUP` : Inscription nouvel utilisateur
- `SUCCESS_LOGIN` : Confirmation de connexion réussie
- `SUCCESS_SIGNUP` : Confirmation d'inscription réussie
- `NEW_MESSAGE` : Envoi de message texte
- `NEW_MESSAGE_IMG` : Envoi de message avec image
- `MESSAGE` : Réception d'un message complet
- `MESSAGE_LIST` : Récupération de l'historique des messages
- `NEW_CHANNEL` : Connexion à un canal/groupe
- `SUCCESS` : Opération réussie
- `ERROR` : Erreur avec message explicatif

### Format des messages

**Exemple de message texte :**
```json
{
  "type": "NEW_MESSAGE",
  "data": {
    "idClient": 1,
    "idChannel": 5,
    "contenu": "Bonjour à tous !"
  }
}
```

**Exemple de réception de message :**
```json
{
  "type": "MESSAGE",
  "data": {
    "idClient": 1,
    "pseudo": "Yuriko",
    "contenu": "Bonjour à tous !",
    "date": "2025-11-21T10:30:00"
  }
}
```

**Exemple de liste de messages (historique) :**
```json
{
  "type": "MESSAGE_LIST",
  "lstEventDTO": [
    {
      "type": "MESSAGE",
      "data": {
        "idClient": 1,
        "pseudo": "Yuriko",
        "contenu": "Premier message",
        "date": "2025-11-21T10:00:00"
      }
    },
    {
      "type": "MESSAGE",
      "data": {
        "idClient": 2,
        "pseudo": "Admin",
        "contenu": "Deuxième message",
        "date": "2025-11-21T10:05:00"
      }
    }
  ]
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

- **Java Swing/AWT** : Interface utilisateur graphique
- **WebSockets (javax.websocket)** : Communication bidirectionnelle temps réel
- **MySQL 8.0+** : Base de données relationnelle avec support BLOB
- **Gson** : Sérialisation/désérialisation JSON
- **Architecture hexagonale** : Découplage des couches (Ports & Adapters)
- **Pattern Repository** : Abstraction de l'accès aux données
- **Pattern MVC** : Organisation côté client (Modèle-Vue-Contrôleur)
- **Pattern DTO** : Transfert de données structuré entre couches
- **JDBC** : Connectivité et requêtes SQL

## 🗄️ Base de données

### Structure
Le projet utilise MySQL avec les tables suivantes :

- **clients** : Stockage des utilisateurs avec support d'image de profil (MEDIUMBLOB)
  - `id` (INT, AUTO_INCREMENT, PRIMARY KEY)
  - `pseudo` (VARCHAR(255), UNIQUE)
  - `mdp` (VARCHAR(255))
  - `created_at` (TIMESTAMP)
  - `img_data` (MEDIUMBLOB)

- **groupes** : Gestion des canaux de discussion (publics et privés)
  - `id` (INT, AUTO_INCREMENT, PRIMARY KEY)
  - `nom` (VARCHAR(150), UNIQUE)
  - `type` (VARCHAR(20), 'groupe' ou 'prive')
  - `cree_par` (INT, FOREIGN KEY vers clients)
  - `cree_le` (TIMESTAMP)

- **messages** : Historique complet des messages
  - `id` (INT, AUTO_INCREMENT, PRIMARY KEY)
  - `groupe_id` (INT, FOREIGN KEY vers groupes)
  - `expediteur_id` (INT, FOREIGN KEY vers clients)
  - `contenu` (TEXT)
  - `envoye_le` (TIMESTAMP)

- **membres_groupes** : Table d'association clients ↔ groupes
  - `id` (INT, AUTO_INCREMENT, PRIMARY KEY)
  - `groupe_id` (INT, FOREIGN KEY vers groupes)
  - `client_id` (INT, FOREIGN KEY vers clients)
  - `role` (VARCHAR(50), ex: 'proprietaire', 'admin', 'membre')
  - `date_adhesion` (TIMESTAMP)

### Particularités
- Support des images via MEDIUMBLOB (jusqu'à 16 Mo par image)
- Indexation des messages par groupe et expéditeur pour performances optimales
- Cascade de suppression pour maintenir l'intégrité référentielle
- Stockage UTF-8 pour support multilingue

## 🔒 Sécurité

- Authentification par pseudo/mot de passe
- Validation des entrées utilisateur
- Gestion des erreurs de connexion
- Isolation des canaux de discussion par groupes
- Gestion des rôles et permissions dans les groupes
- **Protection des fichiers de configuration** :
  - Le fichier `config.properties` est dans `.gitignore` pour éviter tout commit accidentel
  - Le fichier `.gitattributes` masque les diffs de `config.properties.example` pour protéger contre l'exposition de mots de passe dans l'historique git
  - Utilisez toujours `config.properties.example` comme modèle et créez votre propre `config.properties` localement

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

## 👥 Développeurs

### Serveur
- **[@Dono0530](https://github.com/Dono0530)** - Développement backend, architecture serveur, base de données

### Client  
- **[@Joshua-Hermilly](https://github.com/Joshua-Hermilly)** - Développement frontend, interface utilisateur, UX/UI
## 📧 Contact

- **Repository** : [https://github.com/Joshua-Hermilly/MateZone](https://github.com/Joshua-Hermilly/MateZone)
- **Issues** : [https://github.com/Joshua-Hermilly/MateZone/issues](https://github.com/Joshua-Hermilly/MateZone/issues)

---

⭐ N'hésitez pas à mettre une étoile si ce projet vous plaît !

## 📝 Licence

MIT License

**Copyright © 2026 MateZone**

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.

