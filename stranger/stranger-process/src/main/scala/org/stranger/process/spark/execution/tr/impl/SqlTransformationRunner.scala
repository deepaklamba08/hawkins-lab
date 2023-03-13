package org.stranger.process.spark.execution.tr.impl

import org.apache.spark.sql.SparkSession
import org.slf4j.LoggerFactory
import org.stranger.common.exception.StrangerExceptions.InvalidConfigurationException
import org.stranger.common.model.application.Transformation
import org.stranger.process.spark.execution.model.DataBag
import org.stranger.process.spark.execution.tr.TransformationRunner
import org.stranger.data.store.model.SqlTransformation
import org.stranger.process.spark.execution.util.ProcessUtil

class SqlTransformationRunner(sparkSession: SparkSession) extends TransformationRunner {
  private val logger = LoggerFactory.getLogger(this.getClass)

  override protected def run(transformation: Transformation): DataBag = {
    logger.info("Executing : SqlTransformationRunner.run()")
    val sqlTransformation = transformation match {
      case sql: SqlTransformation => sql
      case other => throw new InvalidConfigurationException(s"invalid transformation type - $other")
    }
    ProcessUtil.executeSql(sqlTransformation.getQueryType, sqlTransformation.getValue, sparkSession)
  }
}
