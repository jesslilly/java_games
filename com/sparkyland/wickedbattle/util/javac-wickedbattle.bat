@echo off
rem ------------------------------------------------------------------------------------------
rem javac11wickedpuzzle.bat
rem
rem Compile WickedPuzzle using javac 1.1.8
rem
rem 
rem @author Jess M. Lilly 
rem -----------------------------------------------------------------------------------------

rem Evironment variables set in autoexec.bat.

echo on

rem cd C:\%NAMESPACE%\
%JAVA118BINDIR%\javac com\sparkyland\wickedbattle\*.java



rem Java Archives are good for compression and for way fewer FTP requests.
rem Java Archives are really bad for MS VM and other older VM's that do not support jar files.
rem The applet wont run, so always publish the jar as well as the class, image, and data files.
rem -- Create a Java ARchive.
rem cd C:\spartique\
move wickedbattle.jar wickedbattle-old.jar

rem -- Only Jar up classes, images, and sound.  java is saved to the server by hand, and csv files cannot be accessed with BufferedReader from a jar.
%JAVA118BINDIR%\jar cvf wickedbattle.jar com\sparkyland\wickedbattle\*.class com\sparkyland\wickedbattle\images\*.gif com\sparkyland\wickedbattle\images\*.jpg com\sparkyland\wickedbattle\sound\*.mid com\sparkyland\wickedbattle\sound\*.au





exit