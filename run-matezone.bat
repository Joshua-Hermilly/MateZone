@echo off
REM Script principal pour lancer MateZone (serveur + client)

echo ========================================
echo Lancement complet de MateZone
echo ========================================

echo.
echo [1/4] Vérification des fichiers JAR...

REM Vérifier si les JARs existent, sinon les construire
if not exist "serveur-exec\matezone-server.jar" (
    echo Construction du serveur JAR...
    cd serveur-exec
    call build-server.bat
    cd ..
)

if not exist "client-exec\matezone-client.jar" (
    echo Construction du client JAR...
    cd client-exec
    call build-client.bat
    cd ..
)

echo.
echo [2/4] Démarrage du serveur...
cd serveur-exec
start "MateZone Server" cmd /k "run-server.bat"
cd ..

echo [3/4] Attente du démarrage du serveur...
timeout /t 5 /nobreak >nul

echo.
echo [4/4] Démarrage du client...
cd client-exec
call run-client.bat
cd ..

echo.
echo ========================================
echo Application terminée
echo ========================================
pause