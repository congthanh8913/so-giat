@echo off
setlocal
set DIRNAME=%~dp0
set APP_HOME=%DIRNAME%
set WRAPPER_JAR=%APP_HOME%gradle\wrapper\gradle-wrapper.jar
if not exist "%WRAPPER_JAR%" (
  echo Gradle Wrapper JAR not found. Downloading official Gradle Wrapper...
  powershell -NoProfile -ExecutionPolicy Bypass -Command "$ErrorActionPreference='Stop'; New-Item -ItemType Directory -Force -Path '%APP_HOME%gradle\wrapper' | Out-Null; Invoke-WebRequest -UseBasicParsing -Uri 'https://raw.githubusercontent.com/gradle/gradle/v8.10.0/gradle/wrapper/gradle-wrapper.jar' -OutFile '%WRAPPER_JAR%'"
  if errorlevel 1 exit /b 1
)
if not defined JAVA_HOME (
  echo JAVA_HOME is not configured. Open this project in Android Studio and use its embedded JDK, or set JAVA_HOME to your JDK.
  exit /b 1
)
"%JAVA_HOME%\bin\java.exe" -jar "%WRAPPER_JAR%" %*
endlocal
