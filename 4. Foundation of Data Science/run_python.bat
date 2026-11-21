@echo off
title Sports Tournament - Data Science Analysis
color 0D
echo ============================================================
echo   FOUNDATION OF DATA SCIENCE - Analysis and Visualization
echo   Velalar College of Engineering and Technology, Erode
echo ============================================================
echo.
echo [1/3] Installing required packages (if needed)...
python -m pip install matplotlib numpy --quiet 2>nul
echo       Packages ready.
echo.
echo [2/3] Running data analysis and generating visualizations...
echo.
cd /d "%~dp0"
python analysis.py
if %ERRORLEVEL% NEQ 0 (
    echo.
    echo ERROR: Python execution failed!
    echo Make sure Python 3, matplotlib, and numpy are installed.
    echo Run: pip install matplotlib numpy
    pause
    exit /b 1
)
echo.
echo [3/3] Saving output to output.txt...
python analysis.py > output.txt 2>&1
echo       Output saved to output.txt
echo.
echo ============================================================
echo   EXECUTION COMPLETE - Press any key to exit
echo ============================================================
pause
