rem rewrite
rem -- Create a Java ARchive.
cd \spartique\wickedpuzzle\source
rem -- move wicked.jar wicked.old
%JAVA118BINDIR%\jar cvf wickedpuzzle.jar ..\config ..\images\*.gif ..\sound *.class  .\videogame\*.class .\videogame\sprite\*.class .\videogame\util\*.class
