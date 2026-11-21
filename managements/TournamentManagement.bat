@echo off
title Tournament Management - SportsArena
color 0C
cd /d "%~dp0..\3. Java Programming\src"

echo Compiling all Java files...
javac SportsTournament.java TournamentManagement.java
if %ERRORLEVEL% NEQ 0 (
    echo.
    echo ERROR: Compilation failed! Ensure JDK is installed and in PATH.
    pause
    exit /b 1
)
echo Compilation successful!
echo.

echo Running TournamentManagement...
echo ============================================
java TournamentManagement
echo.
echo ============================================
echo Execution complete.
pause
