
echo off

set ID=default
set SFTPD_HOME=${dist.home.dir}
set SFTPD_MEM_MB=64
set SFTPD_OPTS=-verbose:gc -XX:+PrintGCDetails -XX:+PrintGCDateStamps -XX:+PrintGCTimeStamps -showversion -XX:+PrintCommandLineFlags -XX:-PrintFlagsFinal
set SFTPD_POLICY=%SFTPD_HOME%/conf/sftpd.policy
set MAIN_CLASS=org.javastack.sftpserver.Server
set JAVA_CMD=java -Dprogram.name=sftpd %SFTPD_OPTS% -Xmx%SFTPD_MEM_MB%m -Dsftp.id=%ID% -Dsftp.home=%SFTPD_HOME% -cp %SFTPD_HOME%/conf/%ID%/;%SFTPD_HOME%/conf/;%SFTPD_HOME%/lib;%SFTPD_HOME%/lib/* -Djava.security.manager -Djava.security.policy=file:%SFTPD_POLICY% %MAIN_CLASS%

echo %JAVA_CMD%
%JAVA_CMD%