@echo off
chcp 65001 >nul
echo ═══════════════════════════════════════════════
echo    🎓 SYSTÈME DE GESTION D'ÉCOLE
echo ═══════════════════════════════════════════════
echo.
echo Compilation et lancement de l'application...
echo.

REM Vérifier si Maven est installé
where mvn >nul 2>nul
if %errorlevel% neq 0 (
    echo ❌ ERREUR: Maven n'est pas installé ou pas dans le PATH
    echo.
    echo Veuillez installer Maven: https://maven.apache.org/download.cgi
    echo.
    pause
    exit /b 1
)

REM Compiler le projet
echo [1/2] Compilation du projet...
call mvn clean install
if %errorlevel% neq 0 (
    echo.
    echo ❌ Erreur lors de la compilation
    pause
    exit /b 1
)

echo.
echo ✅ Compilation réussie!
echo.
echo [2/2] Lancement de l'application...
echo.
echo ═══════════════════════════════════════════════
echo   Connexion par défaut:
echo   👤 Utilisateur: admin
echo   🔑 Mot de passe: admin
echo   🎭 Rôle: ADMIN
echo ═══════════════════════════════════════════════
echo.

REM Lancer l'application
call mvn javafx:run

pause
