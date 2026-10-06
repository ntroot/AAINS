@echo off
title AAINS - GitHub Push
cd /d "%~dp0"
set PATH=C:\Temp\git\cmd;C:\Temp\git\mingw64\bin;%PATH%

echo ========================================================
echo    AAINS - Android Auto Apps Installer GitHub Yukleme
echo    Hedef: https://github.com/ntroot/AAINS.git
echo ========================================================
echo.
echo [1/2] Baglanti ve yetkilendirme baslatiliyor...
echo Tarayici penceresi veya giris ekrani acilirsa GitHub ile giris yapin.
echo.

git push -u origin main

if %ERRORLEVEL% EQU 0 (
    echo.
    echo ========================================================
    echo  [BASARILI] Proje basariyla GitHub'a yuklendi!
    echo  Repo adresi: https://github.com/ntroot/AAINS
    echo ========================================================
) else (
    echo.
    echo ========================================================
    echo  [DIKKAT] Push islemi tamamlanamadi.
    echo  1. https://github.com/new adresinden 'AAINS' adinda bir repo actiginizdan emin olun.
    echo  2. Giris yetkinizin dogrulandigindan emin olun.
    echo ========================================================
)

echo.
pause
