@echo off
title Team Management - SportsArena
color 0A
cd /d "%~dp0..\3. Java Programming\src"

echo Compiling all Java files...
javac SportsTournament.java TeamManagement.java
if %ERRORLEVEL% NEQ 0 (
    echo.
    echo ERROR: Compilation failed! Ensure JDK is installed and in PATH.
    pause
    exit /b 1
)
echo Compilation successful!
echo.

echo Running TeamManagement...
echo ============================================
java TeamManagement
echo.
echo ============================================
echo Execution complete.
pause
