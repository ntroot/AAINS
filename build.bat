@echo off
set "JAVA_HOME=C:\Program Files\Eclipse Adoptium\jdk-17.0.20.101-hotspot"
set "ANDROID_HOME=C:\Android\sdk"
set "PATH=%JAVA_HOME%\bin;%ANDROID_HOME%\platform-tools;%PATH%"

echo JAVA_HOME is %JAVA_HOME%
echo ANDROID_HOME is %ANDROID_HOME%

call gradlew.bat assembleDebug
if %ERRORLEVEL% equ 0 (
    echo BUILD SUCCESSFUL!
    copy /Y app\build\outputs\apk\debug\app-debug.apk AAINS-v2.2-unlimited.apk
    echo APK copied to AAINS-v2.2-unlimited.apk
) else (
    echo BUILD FAILED!
)
