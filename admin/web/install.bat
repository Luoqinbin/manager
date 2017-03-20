@ echo off
set rootDir=%cd%
cd %rootDir%
set/p version=请输入打包版本并按回车（如dev）：
call mvn clean compile install -P %version% -Dmaven.test.skip=true
pause
::exit