@echo off
rem ------------------------------------------------------------------------------------------
rem applet11wickedpuzzle.bat
rem
rem Test WickedPuzzle using appletviewer 1.1.8
rem
rem @author Jess M. Lilly 
rem -----------------------------------------------------------------------------------------

rem Evironment variables set in autoexec.bat.

echo on

rem set

cd C:\%NAMESPACE%
%JAVA118BINDIR%\appletviewer wickedbattled.html

exit
