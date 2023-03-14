package org.stranger.process.spark.execution

import org.apache.spark.sql.{SparkSession, functions}
import org.apache.spark.storage.StorageLevel
import org.slf4j.LoggerFactory
import org.stranger.common.exception.StrangerExceptions.InvalidConfigurationException
import org.stranger.common.model.application.{Application, DataSink, DataSource, Transformation}
import org.stranger.common.util.StrangerConstants
import org.stranger.data.store.model.{BaseTransformation, DataSinkImpl, DataSourceImpl, View}
import org.stranger.process.ExecutionResult
import org.stranger.process.spark.execution.loader.DataLoaderFactory
import org.stranger.process.spark.execution.model.DataBag
import org.stranger.process.spark.execution.sink.DataSinkFactory
import org.stranger.process.spark.execution.tr.TransformationRunnerFactory
import org.stranger.process.spark.execution.util.ProcessUtil

import java.io.{PrintWriter, StringWriter}
import scala.collection.JavaConverters._
import scala.util.{Failure, Success, Try}

class AppProcessor private[execution](rc: RuntimeConfiguration) {

  val logger = LoggerFactory.getLogger(this.getClass)

  def process(application: Application): ExecutionResult = {
    logger.info("Executing : AppProcessor.process()")

    val appConfig = application.getConfiguration
    logger.info("creating spark session ...")
    val result = Try(ProcessUtil.createSparkSession(application.getName, appConfig.getConfiguration(StrangerConstants.SPARK_CONFIG_FIELD))) match {
      case Success(sparkSession) => this.process(application, sparkSession)
      case Failure(exception) => this.toExecutionResult(StrangerConstants.APP_EXE_SESSION_CREATION_FAILED_MESSAGE, exception)
    }
    logger.debug("app execution status - {}", result.getExecutionStatus)
    logger.info("Exiting : AppProcessor.process()")
    result
  }

  private def process(application: Application, sparkSession: SparkSession): ExecutionResult = {
    val executionResult = Try(this.processSources(application.getDataSources.asScala, sparkSession)) match {
      case Success(_) =>
        Try(this.processTransformations(application.getTransformations.asScala, sparkSession)) match {
          case Success(_) =>
            Try(this.processSinks(application.getDataSinks.asScala, sparkSession)) match {
              case Success(_) =>
                new ExecutionResult(ExecutionResult.ExecutionStatus.SUCCESS, StrangerConstants.APP_EXE_SUCCESS_MESSAGE, null)
              case Failure(exception) => this.toExecutionResult(StrangerConstants.APP_EXE_SINK_EXE_FAILED_MESSAGE, exception)
            }
          case Failure(exception) => this.toExecutionResult(StrangerConstants.APP_EXE_TR_EXE_FAILED_MESSAGE, exception)
        }
      case Failure(exception) => this.toExecutionResult(StrangerConstants.APP_EXE_DS_LOAD_FAILED_MESSAGE, exception)
    }
    logger.info("execution result - {}", executionResult.getExecutionStatus)
    logger.info("Exiting : AppProcessor.process()")
    executionResult
  }

  private def processSources(sources: Seq[DataSource], sparkSession: SparkSession): Unit = {
    logger.debug("processing data sources ...")
    sources.filter(_.asInstanceOf[DataSourceImpl].getSource.isActive).foreach(dataSource => {
      val ds = dataSource.asInstanceOf[DataSourceImpl]
      logger.debug("processing source - {}", ds.getSource.getName)
      val dataBag = DataLoaderFactory.getDataLoader(ds.getSource, sparkSession).load(ds.getSource.getSourceDetail)
      this.processDataBag(dataBag, ds.getView)
    })
    logger.debug("data sources processed ...")
  }

  private def processTransformations(transformations: Seq[Transformation], sparkSession: SparkSession): Unit = {
    logger.debug("processing transformations ...")
    transformations.filter(_.asInstanceOf[BaseTransformation].isActive).foreach(transformation => {
      logger.debug("processing transformation - {}", transformation.getClass)
      val dataBag = TransformationRunnerFactory.getTransformationRunner(transformation, sparkSession)
        .runTransformation(transformation)
      this.processDataBag(dataBag, transformation.asInstanceOf[BaseTransformation].getView)
    })
  }

  private def processSinks(sinks: Seq[DataSink], sparkSession: SparkSession): Unit = {
    logger.debug("processing data sinks ...")
    sinks.filter(_.asInstanceOf[DataSinkImpl].getTarget.isActive).foreach(dataSink => {
      val ds = dataSink.asInstanceOf[DataSinkImpl]
      logger.debug("processing sink - {}", ds.getTarget.getName)
      DataSinkFactory.getDataSink(ds.getTarget)
        .sink(ProcessUtil.executeSql(ds.getQueryType, ds.getValue, sparkSession), ds.getTarget.getTargetDetail)
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

  private def processDataBag(dataBag: DataBag, view: View): DataBag = {
    logger.info("processing data bag ...")
    var dataframe = if (view.getDataRestructure.isPresent) {
      val dataRestructure = view.getDataRestructure.get()
      logger.info("data restructure is enabled, mode - {}", dataRestructure.getRestructureOn)
      dataRestructure.getRestructureOn match {
        case StrangerConstants.REPARTITION_ON_COLUMN => dataBag.dataFrame
          .repartition(dataRestructure.getColumns.asScala.map(functions.col): _*)
        case StrangerConstants.REPARTITION_ON_SIZE => dataBag.dataFrame
          .repartition(dataRestructure.getNumPartitions)
        case StrangerConstants.REPARTITION_ON_BOTH => dataBag.dataFrame
          .repartition(dataRestructure.getNumPartitions, dataRestructure.getColumns.asScala.map(functions.col): _*)
        case other => throw new InvalidConfigurationException(s"invalid partition on - $other")
      }
    } else {
      dataBag.dataFrame
    }
    dataframe = if (view.isPersist) {
      logger.info("data persist is enabled, mode - {}", view.getPersistMode)
      view.getPersistMode match {
        case StrangerConstants.PERSIST_MODE_DISK_ONLY => dataframe.persist(StorageLevel.DISK_ONLY)
        case StrangerConstants.PERSIST_MODE_DISK_ONLY_2 => dataframe.persist(StorageLevel.DISK_ONLY_2)
        case StrangerConstants.PERSIST_MODE_MEMORY_ONLY => dataframe.persist(StorageLevel.MEMORY_ONLY)
        case StrangerConstants.PERSIST_MODE_MEMORY_ONLY_2 => dataframe.persist(StorageLevel.MEMORY_ONLY_2)
        case StrangerConstants.PERSIST_MODE_MEMORY_ONLY_SER => dataframe.persist(StorageLevel.MEMORY_ONLY_SER)
        case StrangerConstants.PERSIST_MODE_MEMORY_ONLY_SER_2 => dataframe.persist(StorageLevel.MEMORY_ONLY_SER_2)
        case StrangerConstants.PERSIST_MODE_MEMORY_AND_DISK => dataframe.persist(StorageLevel.MEMORY_AND_DISK)
        case StrangerConstants.PERSIST_MODE_MEMORY_AND_DISK_2 => dataframe.persist(StorageLevel.MEMORY_AND_DISK_2)
        case StrangerConstants.PERSIST_MODE_MEMORY_AND_DISK_SER => dataframe.persist(StorageLevel.MEMORY_AND_DISK_SER)
        case StrangerConstants.PERSIST_MODE_MEMORY_AND_DISK_SER_2 => dataframe.persist(StorageLevel.MEMORY_AND_DISK_SER_2)
        case StrangerConstants.PERSIST_MODE_OFF_HEAP => dataframe.persist(StorageLevel.OFF_HEAP)
        case other => throw new InvalidConfigurationException(s"invalid persist mode - $other")
      }

    } else {
      dataframe
    }
    logger.info("creating view, name - {}", view.getName)
    dataframe.createGlobalTempView(view.getName)
    DataBag(dataframe, dataBag.metadata)
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
