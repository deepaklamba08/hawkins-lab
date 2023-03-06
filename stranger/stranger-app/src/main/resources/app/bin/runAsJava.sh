#!/bin/bash
ETL_APP_AME=$1


STRANGER_APP_CLASS=org.stranger.app.StrangerApp

STRANGER_APP_HOME_DIR=$PWD/..
echo "etl app home directory is set to - $STRANGER_APP_HOME_DIR"

CLASSPATH=$CLASSPATH$:$STRANGER_APP_HOME_DIR/lib/*

echo "repository arguments set to - $REPOSITORY_ARGS"

APP_ARGS="jobName $ETL_APP_AME $REPOSITORY_ARGS"
echo "etl app arguments set to - $APP_ARGS"

JVM_ARGS="-DSTRANGER_APP_HOME_DIR=$STRANGER_APP_HOME_DIR -Dlog4j.configuration=file:$STRANGER_APP_HOME_DIR/conf/log4j.xml"
echo "starting etl app for configuration - `$1`"

java -classpath $CLASSPATH $JVM_ARGS $STRANGER_APP_CLASS $APP_ARGS
echo "exiting shell"
