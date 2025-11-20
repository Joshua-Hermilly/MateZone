@echo off
REM Script d'exécution du client MateZone

echo ========================================
echo Lancement du Client MateZone
echo ========================================

REM Vérifier si le JAR existe
if not exist "matezone-client.jar" (
    echo Erreur: matezone-client.jar non trouvé!
    echo Veuillez d'abord exécuter build-client.bat
    pause
    exit /b 1
)

REM Vérifier si les librairies existent
if not exist "../lib/gson-2.13.2.jar" (
    echo Erreur: Librairies manquantes dans ../lib/
    pause
    exit /b 1
)

echo Démarrage du client...
java -jar matezone-client.jar

if %ERRORLEVEL% NEQ 0 (
    echo Erreur lors du lancement du client!
    pause
    exit /b 1
)

echo.
echo ========================================
echo Client terminé
echo ========================================
pause