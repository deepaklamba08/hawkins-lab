package org.stranger.process.spark.execution.tr.impl

import org.apache.spark.sql.SparkSession
import org.stranger.common.model.configuration.Configuration
import org.stranger.process.spark.execution.model.DataBag

trait TransformationFunction {

  def init(configuration: Configuration): Unit

  def execute(sparkSession: SparkSession): DataBag

  def close(): Unit
}
