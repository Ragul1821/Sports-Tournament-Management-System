@echo off
title Player Management - SportsArena
color 0B
cd /d "%~dp0..\3. Java Programming\src"

echo Compiling all Java files...
javac SportsTournament.java PlayerManagement.java
if %ERRORLEVEL% NEQ 0 (
    echo.
    echo ERROR: Compilation failed! Ensure JDK is installed and in PATH.
    pause
    exit /b 1
)
echo Compilation successful!
echo.

echo Running PlayerManagement...
echo ============================================
java PlayerManagement
echo.
echo ============================================
echo Execution complete.
pause
