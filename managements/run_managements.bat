@echo off
title Sports Tournament Managements - Menu
color 0F

:menu
cls
cd /d "%~dp0"
echo ======================================================================
echo          SPORTS TOURNAMENT MANAGEMENT MODULES
echo          Velalar College of Engineering and Technology
echo ======================================================================
echo.
echo   [1] Team Management       (Add/View/Search/Update/Delete Teams)
echo   [2] Player Management     (Register/View/Search/Participation)
echo   [3] Tournament Management (Schedule/Points Table/Record Results)
echo   [4] Exit
echo ======================================================================
set /p choice="Enter your choice (1-4) (e.g. 1): "

if "%choice%"=="1" (
    call "%~dp0TeamManagement.bat"
    goto menu
)
if "%choice%"=="2" (
    call "%~dp0PlayerManagement.bat"
    goto menu
)
if "%choice%"=="3" (
    call "%~dp0TournamentManagement.bat"
    goto menu
)
if "%choice%"=="4" (
    exit /b 0
)

echo.
echo Invalid choice. Please enter 1-4.
ping 127.0.0.1 -n 2 >nul
goto menu
