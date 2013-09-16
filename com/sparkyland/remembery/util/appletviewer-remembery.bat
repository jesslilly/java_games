@echo off
rem ------------------------------------------------------------------------------------------
rem appletviewer-remembery.bat
rem
rem Test Remembery using appletviewer 1.1.8
rem
rem @author Jess M. Lilly 
rem -----------------------------------------------------------------------------------------

rem Evironment variables set in autoexec.bat.

echo on

rem cd C:\%NAMESPACE%\com\sparkyland\remembery
rem set

cd C:\%NAMESPACE%\
%JAVA118BINDIR%\appletviewer rememberyd.html

exit
