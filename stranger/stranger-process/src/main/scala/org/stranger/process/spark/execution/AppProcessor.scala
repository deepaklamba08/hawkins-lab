package org.stranger.process.spark.execution

import org.apache.spark.sql.SparkSession
import org.slf4j.LoggerFactory
import org.stranger.common.model.application.{Application, DataSink, DataSource, Transformation}
import org.stranger.common.util.StrangerConstants
import org.stranger.data.store.model.{DataSinkImpl, DataSourceImpl, SqlTransformation}
import org.stranger.process.ExecutionResult
import org.stranger.process.spark.execution.loader.{DataBag, DataLoaderFactory}
import org.stranger.process.spark.execution.sink.DataSinkFactory
import org.stranger.process.spark.execution.tr.TransformationRunnerFactory
import org.stranger.process.spark.execution.util.ProcessUtil

import java.io.{PrintWriter, StringWriter}
import collection.JavaConverters._
import scala.util.{Failure, Success, Try}

class AppProcessor private[execution](rc: RuntimeConfiguration) {

  val logger = LoggerFactory.getLogger(this.getClass)

  def process(application: Application): ExecutionResult = {
    logger.info("Executing : AppProcessor.process()")

    val appConfig = application.getConfiguration
    logger.info("creating spark session ...")
    val sparkSession = ProcessUtil.createSparkSession(appConfig.getConfiguration(StrangerConstants.SPARK_CONFIG_FIELD))

    Try(this.processSources(application.getDataSources.asScala, sparkSession)) match {
      case Success(_) =>
        Try(this.processTransformations(application.getTransformations.asScala, sparkSession)) match {
          case Success(_) =>
            Try(this.processSinks(application.getDataSinks.asScala, sparkSession)) match {
              case Success(_) =>
                logger.info("Exiting : AppProcessor.process()")
                new ExecutionResult(ExecutionResult.ExecutionStatus.SUCCESS, StrangerConstants.APP_EXE_SUCCESS_MESSAGE, null)
              case Failure(exception) => this.toExecutionResult(StrangerConstants.APP_EXE_SINK_EXE_FAILED_MESSAGE, exception)
            }
          case Failure(exception) => this.toExecutionResult(StrangerConstants.APP_EXE_TR_EXE_FAILED_MESSAGE, exception)
        }
      case Failure(exception) => this.toExecutionResult(StrangerConstants.APP_EXE_DS_LOAD_FAILED_MESSAGE, exception)

    }

  }

  private def processSources(sources: Seq[DataSource], sparkSession: SparkSession): Unit = {
    logger.debug("processing data sources ...")
    sources.foreach(dataSource => {
      val ds = dataSource.asInstanceOf[DataSourceImpl]
      logger.debug("processing source - {}", ds.getSource.getName)
      val dataBag = DataLoaderFactory.getDataLoader(ds.getSource, sparkSession).load(ds.getSource)
      ProcessUtil.processDataBag(dataBag, ds.getView)
    })
    logger.debug("data sources processed ...")
  }

  private def processTransformations(transformations: Seq[Transformation], sparkSession: SparkSession): Unit = {
    logger.debug("processing transformations ...")
    transformations.foreach(transformation => {
      logger.debug("processing transformation - {}", transformation.getClass)
      val dataBag = TransformationRunnerFactory.getTransformationRunner(transformation, sparkSession).run(transformation)
      transformation match {
        case sq: SqlTransformation => ProcessUtil.processDataBag(dataBag, sq.getView)
        case other => throw new IllegalStateException(s"transformation is unknown - ${other.getClass}")
      }
    })
  }

  private def processSinks(sinks: Seq[DataSink], sparkSession: SparkSession): Unit = {
    logger.debug("processing data sinks ...")
    sinks.foreach(dataSink => {
      val ds = dataSink.asInstanceOf[DataSinkImpl]
      logger.debug("processing sink - {}", ds.getTarget.getName)
      DataSinkFactory.getDataSink(ds.getTarget).sink(ProcessUtil.executeSql(ds.getSql, sparkSession), ds.getTarget)
    })
  }

  private def toExecutionResult(messagePrefix: String, exception: Throwable): ExecutionResult = {
    val writer = new StringWriter()
    val printWriter = new PrintWriter(writer)
    exception.printStackTrace(printWriter)
    printWriter.flush()

    var errorMessage = s"$messagePrefix, cause - ${writer.toString()}"
    errorMessage = if (errorMessage.length > this.rc.getErrorMessageLength) {
      s"${errorMessage.substring(0, this.rc.getErrorMessageLength - 3)}..]"
    } else {
      errorMessage
    }

    new ExecutionResult(ExecutionResult.ExecutionStatus.FAILED, errorMessage, null)
  }
}

object AppProcessor {
  def apply(executionProperties: Map[String, String]): AppProcessor = {
    val rc: RuntimeConfiguration = RuntimeConfiguration(executionProperties)
    new AppProcessor(rc)
  }

  def apply(executionProperties: java.util.Map[String, String]): AppProcessor = {
    val rc: RuntimeConfiguration = RuntimeConfiguration(executionProperties.asScala.toMap)
    new AppProcessor(rc)
  }
}
