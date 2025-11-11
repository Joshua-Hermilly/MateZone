# Serveur MateZone

Ce dossier contient les fichiers exécutables pour le serveur MateZone.

## Fichiers

- `build-server.bat` : Script pour compiler le code source et créer le fichier JAR du serveur
- `run-server.bat` : Script pour lancer le serveur MateZone
- `stop-server.bat` : Script pour arrêter le serveur MateZone
- `config.properties` : Fichier de configuration du serveur
- `config.properties.example` : Exemple de fichier de configuration
- `matezone-server.jar` : Fichier JAR exécutable du serveur (généré après build)

## Configuration

### Fichier config.properties

Le serveur utilise un fichier `config.properties` pour sa configuration. Les paramètres principaux sont :

**Base de données :**
- `db.url` : URL de connexion à la base de données MySQL
- `db.username` : Nom d'utilisateur pour la base de données
- `db.password` : Mot de passe pour la base de données

**Serveur WebSocket :**
- `server.host` : Adresse d'écoute du serveur (par défaut: localhost)
- `server.port` : Port d'écoute du serveur (par défaut: 8080)

**Application :**
- `app.name` : Nom de l'application
- `app.version` : Version de l'application
- `app.debug` : Mode debug (true/false)

**Logs :**
- `log.level` : Niveau de log (DEBUG, INFO, WARN, ERROR)
- `log.file` : Fichier de log

**Sécurité :**
- `security.token.expiration` : Durée d'expiration des tokens (en secondes)
- `security.max.connections` : Nombre maximum de connexions simultanées

## Utilisation

1. **Configuration** : 
   - Copiez `config.properties.example` vers `config.properties`
   - Modifiez les paramètres selon vos besoins

2. **Première utilisation** : Exécutez `build-server.bat` pour compiler et créer le JAR

3. **Lancement** : Exécutez `run-server.bat` pour démarrer le serveur

4. **Arrêt** : Exécutez `stop-server.bat` pour arrêter le serveur

## Prérequis

- Java 8 ou supérieur installé
- MySQL Server accessible avec les paramètres de connexion configurés
- Les librairies dans le dossier `../lib/` doivent être présentes :
  - gson-2.13.2.jar
  - Java-WebSocket-1.6.0.jar
  - mysql-connector-j-8.2.0.jar
  - slf4j-api-2.0.7.jar

## Ordre de démarrage

1. Démarrez d'abord le serveur avec `run-server.bat`
2. Attendez que le serveur soit prêt (message dans la console)
3. Lancez ensuite le client depuis le dossier `../client-exec/`

## Logs

Les logs du serveur sont enregistrés dans le fichier spécifié par `log.file` dans la configuration (par défaut: `matezone-server.log`).

## Sécurité

⚠️ **ATTENTION** : Le fichier `config.properties` contient des informations sensibles (mots de passe de base de données). Ne jamais commiter ce fichier dans un dépôt Git public !