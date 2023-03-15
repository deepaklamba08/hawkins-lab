package org.stranger.process.spark.execution.tr.impl.custom

import org.apache.spark.sql.SparkSession
import org.slf4j.LoggerFactory
import org.stranger.common.model.configuration.Configuration
import org.stranger.process.spark.execution.model.DataBag
import org.stranger.process.spark.execution.tr.impl.TransformationFunction

class DataProfileTransformationFunction extends TransformationFunction {

  private val logger = LoggerFactory.getLogger(this.getClass)

  private var configuration: Configuration = _

  override def init(configuration: Configuration): Unit = {
    logger.info("Executing : DataProfileTransformationFunction.init()")
    this.configuration = configuration
  }

  override def execute(sparkSession: SparkSession): DataBag = {
    logger.info("Executing : DataProfileTransformationFunction.execute()")
    val sourceView = this.configuration.getString("sourceView")
    val result = sparkSession.sql(s"select *,'data_profile' as field1 from $sourceView")
    DataBag(result)
  }

  override def close(): Unit = {
    logger.info("Executing : DataProfileTransformationFunction.close()")
  }
}
