#!/bin/bash

source ./set_env.sh

export STRANGER_APP_HOME_DIR="$(dirname "$PWD")"



APP_JAR="${STRANGER_APP_HOME_DIR}/lib/stranger-app-1.0-SNAPSHOT.jar"
APP_ARGS="jobName $1 ${REPOSITORY_ARGS}"

echo "starting etl app for configuration `$1`..."

nohup spark-submit --master $SPARK_MASTER  \
--deploy-mode client \
--supervise --executor-memory $EXECUTOR_MEMORY \
--driver-memory $DRIVER_MEMORY \
--num-executors $NUM_EXECUTORS \
--executor-cores $EXE_CORES \
--conf spark.cores.max=$EXE_CORES \
$APP_JAR $APP_ARGS > $STRANGER_APP_HOME_DIR/tmp/nohup.out &