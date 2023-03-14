package org.stranger.process.spark.execution.util

import org.apache.spark.SparkConf
import org.apache.spark.sql.SparkSession
import org.stranger.common.exception.StrangerExceptions.InvalidConfigurationException
import org.stranger.common.model.configuration.Configuration
import org.stranger.common.util.StrangerConstants
import org.stranger.process.spark.execution.model.DataBag

import java.io.File
import scala.collection.JavaConverters._
import scala.io.Source

object ProcessUtil {

  def createSparkSession(appName: String, sparkConfiguration: Configuration): SparkSession = {
    if (sparkConfiguration == null) {
      throw new IllegalArgumentException("spark configuration can not be null")
    }
    SparkSession.builder().appName(appName)
      .master(sparkConfiguration.getString("master"))
      .config(this.createSparkConf(sparkConfiguration.getConfiguration("other"))).getOrCreate()
  }

  def executeSql(queryType: String, value: String, sparkSession: SparkSession): DataBag = {
    val sqlQuery = queryType match {
      case StrangerConstants.QUERY_SOURCE_SQL => value
      case StrangerConstants.QUERY_SOURCE_FILE => this.loadFromFile(value)
      case other => throw new InvalidConfigurationException(s"invalid query type - $other")
    }
    DataBag(sparkSession.sql(sqlQuery))
  }

  private def loadFromFile(filePath: String): String = {
    Source.fromFile(new File(filePath)).getLines().mkString("\n")
  }

  private def createSparkConf(sparkConfiguration: Configuration): SparkConf = {
    if (!sparkConfiguration.isObject) {
      throw new InvalidConfigurationException("spark configuration is invalid, sparkConfiguration - " + sparkConfiguration)
    }
    val sparkConf = new SparkConf()

    sparkConfiguration.getFieldNames.asScala.foreach(key => {
      if (sparkConfiguration.isString(key)) {
        sparkConf.set(key, sparkConfiguration.getString(key))
      }
    })
    sparkConf
  }
}
