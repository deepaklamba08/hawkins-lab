@echo off
set STRANGER_APP_HOME_DIR=%cd%\..\

set STRANGER_APP_CLASS=org.stranger.app.StrangerApp

set CLASSPATH=%CLASSPATH%;%STRANGER_APP_HOME_DIR%lib\*

set APP_ARGS=jobName %1 %REPOSITORY_ARGS%
set JVM_ARGS=-DSTRANGER_APP_HOME_DIR=%STRANGER_APP_HOME_DIR% -Dlog4j.configuration=file:%STRANGER_APP_HOME_DIR%conf\log4j.xml
echo starting stranger app ...

java -classpath %CLASSPATH% %JVM_ARGS% %STRANGER_APP_CLASS% %APP_ARGS%