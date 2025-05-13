@echo off

echo Creating backup of original src directory...
xcopy /E /I /Y "C:\Users\darii\Desktop\FP24-25\Proyecto\Spring_BackEnd\Spring_BackEnd\src" "C:\Users\darii\Desktop\FP24-25\Proyecto\Spring_BackEnd\Spring_BackEnd\src_backup"

echo Removing demo package...
rmdir /S /Q "C:\Users\darii\Desktop\FP24-25\Proyecto\Spring_BackEnd\Spring_BackEnd\src\main\java\com\example\demo"

echo Replacing pom.xml...
copy /Y "C:\Users\darii\Desktop\FP24-25\Proyecto\Spring_BackEnd\Spring_BackEnd\new-pom.xml" "C:\Users\darii\Desktop\FP24-25\Proyecto\Spring_BackEnd\Spring_BackEnd\pom.xml"

echo Build and package the application...
cd "C:\Users\darii\Desktop\FP24-25\Proyecto\Spring_BackEnd\Spring_BackEnd"
call ./mvnw clean package -DskipTests

echo Done! The application has been updated and built.
echo If build was successful, you can now deploy to Render.com.
