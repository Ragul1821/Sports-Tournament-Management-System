@echo off
title Sports Tournament Management System - Master Launcher
color 0F

:menu
cls
cd /d "%~dp0"
echo ======================================================================
echo     SPORTS TOURNAMENT MANAGEMENT AND PERFORMANCE ANALYSIS SYSTEM
echo     Velalar College of Engineering and Technology, Erode-12
echo     Department of Information Technology - 25IT3A and 25IT3B
echo ======================================================================
echo.
echo   EXECUTE SUBJECT FOLDERS ONE BY ONE TO VIEW OUTPUT:
echo.
echo   [1] Subject 1: 1. Software Engineering
echo       - SRS Documentation, Architecture, UML Diagrams, Test Plan
echo.
echo   [2] Subject 2: 2. Database Management System (DBMS)
echo       - SQLite Schema (DDL), Sample Data (DML), 10 SQL Queries, CSV Export
echo.
echo   [3] Subject 3: 3. Java Programming
echo       - Interactive CMD Menus (Team, Player, Tournament) and Full Simulation
echo.
echo   [4] Subject 4: 4. Foundation of Data Science
echo       - Statistical Data Analysis and 3 Matplotlib Visualizations
echo.
echo   [5] Execute ALL Subjects One-by-One in Sequence (1 to 4)
echo.
echo   ------------------------------------------------------------------
echo   EXTRA PRESENTATION AND EVALUATION DELIVERABLES:
echo   [6] Open Visual Interactive HTML Dashboard in Web Browser
echo   [7] View Comprehensive Viva Preparation and Presentation Guide
echo   [8] Exit
echo ======================================================================
set /p choice="Enter your choice (1-8) (e.g. 1): "

if "%choice%"=="1" goto run_se
if "%choice%"=="2" goto run_dbms
if "%choice%"=="3" goto run_java
if "%choice%"=="4" goto run_ds
if "%choice%"=="5" goto run_all
if "%choice%"=="6" goto open_dashboard
if "%choice%"=="7" goto view_viva
if "%choice%"=="8" goto exit_app

echo.
echo Invalid choice. Please enter a number from 1 to 8.
ping 127.0.0.1 -n 2 >nul
goto menu

:run_se
cls
cd /d "%~dp0"
call "%~dp01. Software Engineering\run_software_engineering.bat"
cd /d "%~dp0"
goto menu

:run_dbms
cls
cd /d "%~dp0"
call "%~dp02. Database Management System\run_dbms.bat"
cd /d "%~dp0"
goto menu

:run_java
cls
cd /d "%~dp0"
call "%~dp03. Java Programming\run_java.bat"
cd /d "%~dp0"
goto menu

:run_ds
cls
cd /d "%~dp0"
call "%~dp04. Foundation of Data Science\run_analysis.bat"
cd /d "%~dp0"
goto menu

:open_dashboard
cls
echo ======================================================================
echo   OPENING INTERACTIVE TOURNAMENT DASHBOARD IN DEFAULT BROWSER...
echo ======================================================================
start "" "%~dp0Tournament_Dashboard.html"
echo Dashboard launched in browser!
echo.
pause
goto menu

:view_viva
cls
echo ======================================================================
echo   COMPREHENSIVE VIVA PREPARATION GUIDE (VIVA_PREPARATION.md)
echo ======================================================================
echo Opening VIVA_PREPARATION.md in Notepad for quick review...
start notepad.exe "%~dp0VIVA_PREPARATION.md"
echo.
pause
goto menu

:run_all
cls
echo ======================================================================
echo   STARTING COMPLETE STEP-BY-STEP WALKTHROUGH OF ALL 4 SUBJECTS
echo ======================================================================
echo.

echo ======================================================================
echo   STEP 1 OF 4: Executing Subject 1 (Software Engineering)
echo ======================================================================
cd /d "%~dp0"
call "%~dp01. Software Engineering\run_software_engineering.bat"
cd /d "%~dp0"

echo.
echo ----------------------------------------------------------------------
echo   Subject 1 Completed!
echo   Press any key to proceed to STEP 2 (Database Management System)...
echo ----------------------------------------------------------------------
pause >nul

cls
echo ======================================================================
echo   STEP 2 OF 4: Executing Subject 2 (Database Management System)
echo ======================================================================
cd /d "%~dp0"
call "%~dp02. Database Management System\run_dbms.bat"
cd /d "%~dp0"

echo.
echo ----------------------------------------------------------------------
echo   Subject 2 Completed!
echo   Press any key to proceed to STEP 3 (Java Programming)...
echo ----------------------------------------------------------------------
pause >nul

cls
echo ======================================================================
echo   STEP 3 OF 4: Executing Subject 3 (Java Programming)
echo ======================================================================
cd /d "%~dp0"
call "%~dp03. Java Programming\run_java.bat" full
cd /d "%~dp0"

echo.
echo ----------------------------------------------------------------------
echo   Subject 3 Completed!
echo   Press any key to proceed to STEP 4 (Foundation of Data Science)...
echo ----------------------------------------------------------------------
pause >nul

cls
echo ======================================================================
echo   STEP 4 OF 4: Executing Subject 4 (Foundation of Data Science)
echo ======================================================================
cd /d "%~dp0"
call "%~dp04. Foundation of Data Science\run_analysis.bat"
cd /d "%~dp0"

cls
echo ======================================================================
echo   ALL 4 SUBJECTS EXECUTED SUCCESSFULLY!
echo ======================================================================
echo.
echo   Summary of Generated Deliverables:
echo   [1] 1. Software Engineering       : software_engineering_output.txt
echo   [2] 2. Database Management System : dbms_output.txt, sports_tournament.db
echo   [3] 3. Java Programming           : output.txt, Team/Player/Tournament outputs
echo   [4] 4. Foundation of Data Science : analysis_output.txt, 3 PNG visualizations
echo   [5] Interactive Web Dashboard     : Tournament_Dashboard.html
echo   [6] Viva Preparation Guide        : VIVA_PREPARATION.md
echo.
echo ======================================================================
set /p open_dash_final="Open Interactive HTML Dashboard in browser now? (Y/N) (e.g. Y): "
if /i "%open_dash_final%"=="Y" (
    start "" "%~dp0Tournament_Dashboard.html"
)
goto menu

:exit_app
echo.
echo Thank you for using Sports Tournament Management System!
ping 127.0.0.1 -n 2 >nul
exit /b 0
