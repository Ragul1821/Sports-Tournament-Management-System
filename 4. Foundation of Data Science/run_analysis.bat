@echo off
title Subject 4: Foundation of Data Science - Sports Tournament System
color 0C
cd /d "%~dp0"

echo ======================================================================
echo   SUBJECT 4: FOUNDATION OF DATA SCIENCE
echo   Sports Tournament Management System - Analysis and Visualizations
echo   Velalar College of Engineering and Technology, Erode-12
echo   Department of Information Technology
echo ======================================================================
echo.

echo Running Python Data Science Analysis...
echo ----------------------------------------------------------------------

python "%~dp0analysis.py" > "%~dp0analysis_output.txt" 2>&1
if %ERRORLEVEL% NEQ 0 (
    echo.
    echo ERROR: Python analysis failed!
    type "%~dp0analysis_output.txt"
    pause
    exit /b 1
)

type "%~dp0analysis_output.txt"
echo.
echo ======================================================================
echo   [SUCCESS] DATA SCIENCE ANALYSIS COMPLETED
echo   - Execution Log:    analysis_output.txt
echo   - Visualizations in: visualizations\
echo     1) goals_per_match.png
echo     2) points_distribution.png
echo     3) top_scorers.png
echo   - Interactive Dashboard: Tournament_Dashboard.html
echo ======================================================================
echo.

set /p open_dash="Open Visual Interactive HTML Dashboard in browser now? (Y/N) (e.g. Y): "
if /i "%open_dash%"=="Y" (
    echo Opening Tournament_Dashboard.html...
    start "" "%~dp0..\Tournament_Dashboard.html"
)
echo.
pause
