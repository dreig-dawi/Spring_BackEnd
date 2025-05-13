@echo off

echo Setting up JAVA_HOME environment variable...
set JAVA_HOME=C:\Program Files\Java\jdk-21
echo JAVA_HOME is now set to %JAVA_HOME%

echo Adding Java bin directory to PATH...
set PATH=%PATH%;%JAVA_HOME%\bin
echo Java bin directory added to PATH

echo Building Spring Boot application...
cd /d "%~dp0"
call .\mvnw clean package -DskipTests

echo Done!
echo If the build was successful, you can deploy to Render.com
echo To run the application locally, use: java -jar target\cheffin-0.0.1-SNAPSHOT.jar

pause
