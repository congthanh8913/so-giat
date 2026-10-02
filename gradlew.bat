@echo off
setlocal EnableExtensions EnableDelayedExpansion
set "GRADLE_VERSION=8.10"
set "GRADLE_HOME=%USERPROFILE%\.gradle\wrapper\bootstrap\gradle-%GRADLE_VERSION%"
set "GRADLE_EXE=%GRADLE_HOME%\bin\gradle.bat"
set "ZIP=%TEMP%\gradle-%GRADLE_VERSION%-bin.zip"

if not exist "%GRADLE_EXE%" (
  echo Gradle %GRADLE_VERSION% not found. Downloading official Gradle distribution...
  powershell -NoProfile -ExecutionPolicy Bypass -Command "& { $ErrorActionPreference='Stop'; $zip = [Environment]::ExpandEnvironmentVariables($env:GRADLE_ZIP); Invoke-WebRequest -UseBasicParsing -Uri ('https://services.gradle.org/distributions/gradle-' + $env:GRADLE_VERSION + '-bin.zip') -OutFile $zip; New-Item -ItemType Directory -Force -Path $env:GRADLE_BOOTSTRAP | Out-Null; Expand-Archive -Force -Path $zip -DestinationPath $env:GRADLE_BOOTSTRAP; Remove-Item -Force $zip }"
  if errorlevel 1 exit /b 1
)

call "%GRADLE_EXE%" %*
set EXIT_CODE=%ERRORLEVEL%
endlocal & exit /b %EXIT_CODE%
