#!/bin/bash

# Use this script on the Render.com build environment

echo "Removing demo package..."
rm -rf src/main/java/com/example/demo

echo "Building the application..."
./mvnw clean package -DskipTests
