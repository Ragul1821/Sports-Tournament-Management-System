@echo off
title Subject 2: Database Management System - Sports Tournament System
color 0B
cd /d "%~dp0"

echo ======================================================================
echo   SUBJECT 2: DATABASE MANAGEMENT SYSTEM (DBMS)
echo   Sports Tournament Management System - SQLite SQL Execution
echo   Velalar College of Engineering and Technology, Erode-12
echo   Department of Information Technology
echo ======================================================================
echo.

echo Running Python SQLite Database Script...
echo ----------------------------------------------------------------------

python "%~dp0run_dbms.py" > "%~dp0dbms_output.txt" 2>&1
if %ERRORLEVEL% NEQ 0 (
    echo.
    echo ERROR: DBMS execution failed!
    type "%~dp0dbms_output.txt"
    pause
    exit /b 1
)

type "%~dp0dbms_output.txt"
echo.
echo ======================================================================
echo   [SUCCESS] DBMS EXECUTION COMPLETED
echo   - SQLite Database: sports_tournament.db
echo   - Execution Log:   dbms_output.txt
echo   - CSV Exports in:  ..\4. Foundation of Data Science\data\
echo ======================================================================
echo.
pause
