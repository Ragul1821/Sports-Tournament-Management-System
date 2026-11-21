@echo off
title Subject 3: Java Programming - Sports Tournament System
color 0A
cd /d "%~dp0src"

:compile
echo ======================================================================
echo   SUBJECT 3: JAVA PROGRAMMING
echo   Sports Tournament Management and Performance Analysis System
echo   Velalar College of Engineering and Technology, Erode-12
echo   Department of Information Technology
echo ======================================================================
echo.
echo Compiling Java source files...
javac SportsTournament.java TeamManagement.java PlayerManagement.java TournamentManagement.java
if %ERRORLEVEL% NEQ 0 (
    echo.
    echo ERROR: Compilation failed! Ensure JDK is installed and in PATH.
    pause
    exit /b 1
)
echo Compilation successful!
echo.

if /i "%~1"=="full" goto run_full_simulation

:menu
echo ======================================================================
echo   JAVA PROGRAMMING - SELECT EXECUTION MODULE
echo ======================================================================
echo   1. Team Management       (Interactive CMD Menu: Add/View/Search/Update/Delete)
echo   2. Player Management     (Interactive CMD Menu: Register/View/Search/Participation)
echo   3. Tournament Management (Interactive CMD Menu: Schedule/Standings/Reports)
echo   4. Full Tournament System Simulation (All 11 Phases + Streams + Exceptions)
echo   5. Exit
echo ======================================================================
set /p java_choice="Enter your choice (1-5) (e.g. 1): "

if "%java_choice%"=="1" (
    echo.
    java TeamManagement
    echo.
    goto menu
)
if "%java_choice%"=="2" (
    echo.
    java PlayerManagement
    echo.
    goto menu
)
if "%java_choice%"=="3" (
    echo.
    java TournamentManagement
    echo.
    goto menu
)
if "%java_choice%"=="4" (
    goto run_full_simulation
)
if "%java_choice%"=="5" (
    echo Exiting Java Programming module.
    goto end
)

echo Invalid choice. Please enter 1-5.
goto menu

:run_full_simulation
echo.
echo Running Full Tournament Simulation...
echo ----------------------------------------------------------------------
java SportsTournament > "%~dp0output.txt" 2>&1
type "%~dp0output.txt"
echo.
echo [INFO] Full execution log saved to: 3. Java Programming\output.txt
echo.
if /i "%~1"=="full" exit /b 0
pause
goto menu

:end
pause
