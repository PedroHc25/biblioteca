@echo off
setlocal
title BIBLIOTECA - Backend Spring Boot

cd /d "%~dp0"

if not exist ".env" (
    echo ERROR: No existe el archivo .env
    pause
    exit /b 1
)

for /f "usebackq tokens=1,* delims==" %%A in (".env") do (
    if not "%%A"=="" set "%%A=%%B"
)

if not defined DB_PASSWORD (
    echo ERROR: DB_PASSWORD no configurada
    pause
    exit /b 1
)

if not defined JWT_SECRET (
    echo ERROR: JWT_SECRET no configurada
    pause
    exit /b 1
)

echo ============================================
echo       BIBLIOTECA - SPRING BOOT
echo       http://localhost:8081
echo ============================================

cd /d "%~dp0backend"

call gradlew.bat bootRun

pause
