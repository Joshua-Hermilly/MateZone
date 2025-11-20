# Client MateZone

Ce dossier contient les fichiers exécutables pour le client MateZone.

## Fichiers

- `build-client.bat` : Script pour compiler le code source et créer le fichier JAR du client
- `run-client.bat` : Script pour lancer le client MateZone
- `matezone-client.jar` : Fichier JAR exécutable du client (généré après build)

## Utilisation

1. **Première utilisation** : Exécutez `build-client.bat` pour compiler et créer le JAR
2. **Lancement** : Exécutez `run-client.bat` pour démarrer le client

## Prérequis

- Java 8 ou supérieur installé
- Les librairies dans le dossier `../lib/` doivent être présentes :
  - gson-2.13.2.jar
  - Java-WebSocket-1.6.0.jar  
  - slf4j-api-2.0.7.jar

## Notes

- Le client se connectera automatiquement au serveur MateZone
- Assurez-vous que le serveur est démarré avant de lancer le client