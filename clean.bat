@ echo off
set rootDir=%cd%
cd %rootDir%
call mvn clean -Dmaven.test.skip=true
pause
::exit