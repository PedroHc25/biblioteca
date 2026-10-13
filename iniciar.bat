@echo off
REM ============================================================
REM  BIBLIOTECA - Iniciar la app (Spring Boot + MySQL)
REM  Doble clic sobre este archivo y listo.
REM  URL: http://localhost:8081  |  Para detener: Ctrl+C
REM ============================================================

REM ---- Variables de entorno (las pone este archivo solo) ----
REM Contraseña del usuario root de MySQL
set DB_PASSWORD=1234
REM Secreto JWT para firmar los tokens de login
set JWT_SECRET=vLNSyHIUGAJlb6TIcvv+cJ0WW0RiJLhoSJlhX1ITMW7sQ0WOrkRTQHC8OqzDK1wU

cd /d "%~dp0"

echo ============================================
echo  Levantando BIblioteca en: http://localhost:8081
echo  Para detener: Ctrl+C
echo ============================================

call gradlew.bat bootRun

pause