package org.stranger.process.spark.execution.util

import org.apache.spark.sql.SparkSession
import org.stranger.common.model.configuration.Configuration
import org.stranger.data.store.model.View
import org.stranger.process.spark.execution.loader.DataBag

object ProcessUtil {

  def createSparkSession(sparkConfiguration: Configuration): SparkSession = {
    if (sparkConfiguration == null) {
      throw new IllegalArgumentException("spark configuration con not be null")
    }
    null
  }

  def processDataBag(dataBag: DataBag, view: View): Unit = {}

  def executeSql(sqlOrSource:String,sparkSession: SparkSession):DataBag={
    null
  }
}
