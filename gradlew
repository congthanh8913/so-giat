#!/bin/sh
set -e
GRADLE_VERSION="8.10.2"
GRADLE_HOME="${GRADLE_USER_HOME:-$HOME/.gradle}/wrapper/bootstrap/gradle-$GRADLE_VERSION"
GRADLE_EXE="$GRADLE_HOME/bin/gradle"
if [ ! -x "$GRADLE_EXE" ]; then
  echo "Gradle $GRADLE_VERSION not found. Downloading official Gradle distribution..."
  ZIP="${TMPDIR:-/tmp}/gradle-$GRADLE_VERSION-bin.zip"
  mkdir -p "$(dirname "$GRADLE_HOME")"
  curl -fL -o "$ZIP" "https://services.gradle.org/distributions/gradle-$GRADLE_VERSION-bin.zip"
  unzip -q -o "$ZIP" -d "$(dirname "$GRADLE_HOME")"
  rm -f "$ZIP"
fi
exec "$GRADLE_EXE" "$@"
