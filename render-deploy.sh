#!/bin/bash

# This script is meant to be used on Render.com for deployment

echo "Setting up JAVA_HOME..."
export JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64
echo "JAVA_HOME is set to $JAVA_HOME"

# Remove the demo package if it exists
if [ -d "src/main/java/com/example/demo" ]; then
  echo "Removing demo package..."
  rm -rf src/main/java/com/example/demo
fi

echo "Building the application..."
./mvnw clean package -DskipTests

echo "Build complete! JAR file should be in the target folder."
