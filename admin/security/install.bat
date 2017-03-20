@ echo off
set rootDir=%cd%
cd %rootDir%
call mvn clean compile install -Dmaven.test.skip=true
pause
::exit