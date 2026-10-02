@echo off
setlocal
set "GRADLE_VERSION=8.10"
set "GRADLE_HOME=%USERPROFILE%\.gradle\wrapper\bootstrap\gradle-%GRADLE_VERSION%"
set "GRADLE_EXE=%GRADLE_HOME%\bin\gradle.bat"

if not exist "%GRADLE_EXE%" (
  echo Gradle %GRADLE_VERSION% not found. Downloading official Gradle distribution...
  set "ZIP=%TEMP%\gradle-%GRADLE_VERSION%-bin.zip"
  powershell -NoProfile -ExecutionPolicy Bypass -Command "$ErrorActionPreference='Stop'; Invoke-WebRequest -UseBasicParsing -Uri 'https://services.gradle.org/distributions/gradle-%GRADLE_VERSION%-bin.zip' -OutFile '%ZIP%'; New-Item -ItemType Directory -Force -Path '%USERPROFILE%\.gradle\wrapper\bootstrap' | Out-Null; Expand-Archive -Force -Path '%ZIP%' -DestinationPath '%USERPROFILE%\.gradle\wrapper\bootstrap'; Remove-Item -Force '%ZIP%'"
  if errorlevel 1 exit /b 1
)
call "%GRADLE_EXE%" %*
set EXIT_CODE=%ERRORLEVEL%
endlocal & exit /b %EXIT_CODE%
