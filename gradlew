#!/bin/sh
APP_HOME=$(CDPATH= cd -- "$(dirname -- "$0")" && pwd)
WRAPPER_JAR="$APP_HOME/gradle/wrapper/gradle-wrapper.jar"
if [ ! -f "$WRAPPER_JAR" ]; then
  echo "Gradle Wrapper JAR not found. Downloading official Gradle Wrapper..."
  mkdir -p "$APP_HOME/gradle/wrapper"
  curl -fsSL -o "$WRAPPER_JAR" "https://raw.githubusercontent.com/gradle/gradle/v8.10.0/gradle/wrapper/gradle-wrapper.jar" || exit 1
fi
exec "${JAVA_HOME}/bin/java" -jar "$WRAPPER_JAR" "$@"
