
rem cd \j\wicked
cd C:\j\wicked\utilities\batch\

rem C:\jdk1.1.8\bin\javakey -c SystemProperties.class true
rem C:\jdk1.1.8\bin\javakey -cs spartique true
rem C:\jdk1.1.8\bin\javakey -gk spartique "DSA" 512 publicKey privateKey
rem C:\jdk1.1.8\bin\javakey -ikp spartique publicKey privateKey
rem C:\jdk1.1.8\bin\javakey -ik SystemProperties.class publicKey
rem C:\jdk1.1.8\bin\javakey -gc spartiqueDirective
rem C:\jdk1.1.8\bin\javakey -ii SystemProperties.class 
rem C:\jdk1.1.8\bin\javakey -ii spartique 
rem C:\jdk1.1.8\bin\javakey -ld
rem C:\jdk1.1.8\bin\jar cvf SystemProperties.jar C:\j\wicked\SystemProperties.class C:\j\wicked\DieFrame.class
C:\jdk1.1.8\bin\javakey -gs spartiqueSignature SystemProperties.jar


