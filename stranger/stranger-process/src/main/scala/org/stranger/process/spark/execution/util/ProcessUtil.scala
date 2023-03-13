package org.stranger.process.spark.execution.util

import org.apache.spark.sql.SparkSession
import org.stranger.common.exception.StrangerExceptions.InvalidConfigurationException
import org.stranger.common.model.configuration.Configuration
import org.stranger.data.store.model.View
import org.stranger.process.spark.execution.model.DataBag

import java.io.File
import scala.io.Source

object ProcessUtil {

  def createSparkSession(sparkConfiguration: Configuration): SparkSession = {
    if (sparkConfiguration == null) {
      throw new IllegalArgumentException("spark configuration con not be null")
    }
    null
  }

  def processDataBag(dataBag: DataBag, view: View): Unit = {}

  def executeSql(queryType: String, value: String, sparkSession: SparkSession): DataBag = {
    val sqlQuery = queryType match {
      case "sql" => value
      case "file" => loadFromFile(value)
      case other => throw new InvalidConfigurationException(s"invalid query type - $other")
    }
    DataBag(sparkSession.sql(sqlQuery))
  }

  private def loadFromFile(filePath: String): String = {
    Source.fromFile(new File(filePath)).getLines().mkString("\n")
  }
}
