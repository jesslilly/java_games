@echo off
rem ------------------------------------------------------------------------------------------
rem javac-remembery.bat
rem
rem Compile Remembery using javac 1.1.8
rem
rem 
rem @author Jess M. Lilly 
rem Jess, I switched back to compiling and testing in java 1.1.8.  It seems to work WAY better.
rem for one thing it is much faster.  Mostly I hope it will be compatable with MS VM.
rem It is not jar files that is causing the game not to work in MS VM.  Carin is being
rem very nice and testing at school for me.
rem -----------------------------------------------------------------------------------------

rem Evironment variables set in autoexec.bat.

echo on

rem %JAVA118BINDIR%\javac com\sparkyland\spartique\gui\*.java
rem %JAVA118BINDIR%\javac com\sparkyland\spartique\videogame\*.java
rem %JAVA118BINDIR%\javac com\sparkyland\spartique\common\*.java

rem ------------  Set intial directory in Editplus so you can click on the error and go to the syntx error.
rem ------------  cd C:\%NAMESPACE%\
%JAVA118BINDIR%\javac com\sparkyland\remembery\*.java

rem Java Archives are good for compression and for way fewer FTP requests.
rem Java Archives are really bad for MS VM and other older VM's that do not support jar files.
rem The applet wont run, so always publish the jar as well as the class, image, and data files.
rem -- Create a Java ARchive.
rem cd C:\spartique\
move remembery.jar rememery-old.jar

rem -- Only Jar up classes, images, and sound.  java is saved to the server by hand, and csv files cannot be accessed with BufferedReader from a jar.
%JAVA118BINDIR%\jar cvf remembery.jar com\sparkyland\remembery\*.class com\sparkyland\remembery\images\*.gif com\sparkyland\remembery\images\*.jpg com\sparkyland\remembery\sound\*.mid com\sparkyland\remembery\sound\*.au


rem this would be a cooler way, but I can't get it to work.
rem -- Create a Java ARchive.
rem cd C:\spartique\com\sparkyland\remembery\
rem move remembery.jar rememery-old.jar

rem -- Only Jar up classes, images, and sound.  java is saved to the server by hand, and csv files cannot be accessed with BufferedReader from a jar.
rem %JAVA118BINDIR%\jar cvf remembery.jar *.class images\*.gif images\*.jpg sound\*.mid sound\*.au


exit
