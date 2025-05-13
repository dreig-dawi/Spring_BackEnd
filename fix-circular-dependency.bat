@echo off

echo Fixing circular dependency and building the application...

echo Removing demo package if exists...
if exist "src\main\java\com\example\demo" (
    rmdir /S /Q "src\main\java\com\example\demo"
)

echo Building application...
call mvnw clean package -DskipTests

echo Done!
echo If the build was successful, you can now deploy to Render.com
