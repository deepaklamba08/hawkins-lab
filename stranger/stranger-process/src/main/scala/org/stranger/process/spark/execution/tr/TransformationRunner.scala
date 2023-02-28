package org.stranger.process.spark.execution.tr

import org.apache.spark.sql.SparkSession
import org.stranger.common.model.application.Transformation
import org.stranger.process.spark.execution.loader.DataBag

trait TransformationRunner {

  def run(transformation: Transformation): DataBag = {
    null
  }
}

object TransformationRunnerFactory {

  def getTransformationRunner(transformation: Transformation, sparkSession: SparkSession): TransformationRunner = {
    null
  }
}
