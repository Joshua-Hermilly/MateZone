@echo off
REM Script pour lancer MateZone Server puis Client

echo ========================================
echo Lancement de MateZone
echo ========================================

REM Compilation des sources
echo.
echo [1/4] Compilation des sources...
javac -encoding UTF-8 -d bin -cp "lib/*" src/common/dto/*.java src/common/protocol/*.java src/server/bd/*.java src/server/bd/repository/*.java src/server/metier/interfaces/*.java src/server/metier/model/*.java src/server/metier/service/*.java src/server/Protocol/webSocket/*.java src/server/MainServer.java src/client/metier/interfaces/*.java src/client/metier/*.java src/client/infrastructure/websocket/*.java src/client/controleur/*.java src/client/ihm/frame/connexion/*.java src/client/ihm/frame/affichage/*.java src/client/ihm/panel/connexion/*.java src/client/ihm/panel/affichage/*.java src/client/ihm/*.java src/client/MainClient.java

if %ERRORLEVEL% NEQ 0 (
    echo Erreur lors de la compilation!
    pause
    exit /b 1
)

echo Compilation reussie!

REM Lancement du serveur en arrière-plan
echo.
echo [2/4] Demarrage du serveur...
start "MateZone Server" cmd /k "java -cp bin;lib/* server.MainServer"

REM Attendre que le serveur soit prêt
echo [3/4] Attente du demarrage du serveur...
timeout /t 3 /nobreak >nul

REM Lancement du client
echo.
echo [4/4] Demarrage du client...
java -cp bin;lib/* client.MainClient

echo.
echo ========================================
echo Application terminee
echo ========================================
pause
