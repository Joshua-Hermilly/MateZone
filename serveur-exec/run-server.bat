@echo off
REM Script d'exécution du serveur MateZone

echo ========================================
echo Lancement du Serveur MateZone
echo ========================================

REM Vérifier si le JAR existe
if not exist "matezone-server.jar" (
    echo Erreur: matezone-server.jar non trouvé!
    echo Veuillez d'abord exécuter build-server.bat
    pause
    exit /b 1
)

REM Vérifier si le fichier de configuration existe
if not exist "config.properties" (
    echo Erreur: config.properties non trouvé!
    echo Veuillez copier config.properties.example vers config.properties
    echo et adapter la configuration selon vos besoins.
    pause
    exit /b 1
)

REM Vérifier si les librairies existent
if not exist "../lib/mysql-connector-j-8.2.0.jar" (
    echo Erreur: Librairies manquantes dans ../lib/
    pause
    exit /b 1
)

echo Démarrage du serveur...
echo Configuration chargée depuis: config.properties
echo.

java -Dconfig.file=config.properties -jar matezone-server.jar

if %ERRORLEVEL% NEQ 0 (
    echo Erreur lors du lancement du serveur!
    pause
    exit /b 1
)

echo.
echo ========================================
echo Serveur terminé
echo ========================================
pause