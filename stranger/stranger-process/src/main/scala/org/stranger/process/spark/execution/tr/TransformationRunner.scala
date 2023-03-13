package org.stranger.process.spark.execution.tr

import org.apache.spark.sql.SparkSession
import org.stranger.common.exception.StrangerExceptions.InvalidConfigurationException
import org.stranger.common.model.application.Transformation
import org.stranger.data.store.model.{BaseTransformation}
import org.stranger.process.spark.execution.model.DataBag
import org.stranger.process.spark.execution.tr.impl.{CustomTransformationRunner, SqlTransformationRunner}
import org.stranger.process.spark.execution.util.ProcessUtil
import org.stranger.data.store.model.{CustomTransformation, SqlTransformation}

trait TransformationRunner {

  def runTransformation(transformation: Transformation): DataBag = {
    val baseTransformation = transformation match {
      case baseTr: BaseTransformation => baseTr
      case other => throw new InvalidConfigurationException(s"invalid transformation type - $other")
    }
    val dataBag = this.run(transformation)
    ProcessUtil.processDataBag(dataBag, baseTransformation.getView)
    dataBag
  }

  protected def run(transformation: Transformation): DataBag
}

object TransformationRunnerFactory {

  def getTransformationRunner(transformation: Transformation, sparkSession: SparkSession): TransformationRunner = {
    transformation match {
      case _: SqlTransformation => new SqlTransformationRunner(sparkSession)
      case _: CustomTransformation => new CustomTransformationRunner(sparkSession)
      case other => throw new InvalidConfigurationException(s"invalid transformation type - $other")
    }
  }
}
