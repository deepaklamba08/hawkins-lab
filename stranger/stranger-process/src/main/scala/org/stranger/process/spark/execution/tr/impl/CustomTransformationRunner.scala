package org.stranger.process.spark.execution.tr.impl

import org.apache.spark.sql.SparkSession
import org.slf4j.LoggerFactory
import org.stranger.common.exception.StrangerExceptions.InvalidConfigurationException
import org.stranger.common.model.application.Transformation
import org.stranger.process.spark.execution.model.DataBag
import org.stranger.process.spark.execution.tr.TransformationRunner
import org.stranger.data.store.model.CustomTransformation

import scala.util.{Failure, Success, Try}

class CustomTransformationRunner(sparkSession: SparkSession) extends TransformationRunner {
  private val logger = LoggerFactory.getLogger(this.getClass)

  override protected def run(transformation: Transformation): DataBag = {
    logger.info("Executing : CustomTransformationRunner.run()")
    val customTransformation = transformation match {
      case customTr: CustomTransformation => customTr
      case other => throw new InvalidConfigurationException(s"invalid transformation type - $other")
    }
    val transformationFunction = Try(Class.forName(customTransformation.getImplementation).newInstance()) match {
      case Success(value) => value match {
        case trFx: TransformationFunction => trFx
        case other =>
          throw new InvalidConfigurationException(s"implementation is not of type -${classOf[TransformationFunction]}, found - $other")
      }
      case Failure(exception) => throw new InvalidConfigurationException(s"failed to load custom transformation - ${customTransformation.getImplementation}", exception)
    }
    transformationFunction.execute(customTransformation.getConfiguration, this.sparkSession)
  }
}
