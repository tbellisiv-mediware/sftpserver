
echo off

set ID=default
set SFTPD_HOME=C:/dev/sftp/sftpserver/home
set SFTPD_MEM_MB=64
set SFTPD_OPTS_DEF=-verbose:gc -XX:+PrintGCDetails -XX:+PrintGCDateStamps -XX:+PrintGCTimeStamps -showversion -XX:+PrintCommandLineFlags -XX:-PrintFlagsFinal
set SFTPD_OPTS=%SFTPD_OPTS_DEF%
rem SFTPD_POLICY="%SFTPD_HOME%/conf/%ID%/sftpd.policy"
set SFTPD_POLICY=%SFTPD_HOME%/conf/sftpd.policy
set PWD_CLASS=org.javastack.sftpserver.PasswordEncrypt
set JAVA_CMD=java -Dprogram.name=sftpd %SFTPD_OPTS% -Xmx%SFTPD_MEM_MB%m -Dsftp.id=%ID% -Dsftp.home=%SFTPD_HOME% -Dsftp.log=CONSOLE -cp %SFTPD_HOME%/conf/%ID%/;%SFTPD_HOME%/conf/;%SFTPD_HOME%/lib;%SFTPD_HOME%/lib/* -Djava.security.manager -Djava.security.policy=file:%SFTPD_POLICY% %PWD_CLASS%

echo %JAVA_CMD%
%JAVA_CMD%