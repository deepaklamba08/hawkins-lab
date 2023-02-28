package org.stranger.process.spark.execution

import org.slf4j.LoggerFactory
import org.stranger.common.model.application.Application
import org.stranger.common.util.StrangerConstants
import org.stranger.data.store.model.{DataSinkImpl, DataSourceImpl, SqlTransformation}
import org.stranger.process.ExecutionResult
import org.stranger.process.spark.execution.loader.{DataBag, DataLoaderFactory}
import org.stranger.process.spark.execution.sink.DataSinkFactory
import org.stranger.process.spark.execution.tr.TransformationRunnerFactory
import org.stranger.process.spark.execution.util.ProcessUtil

import collection.JavaConverters._

object AppProcessor {

  val logger = LoggerFactory.getLogger(this.getClass)

  def process(application: Application): ExecutionResult = {
    logger.info("Executing : AppProcessor.process()")

    val appConfig = application.getConfiguration
    val sparkSession = ProcessUtil.createSparkSession(appConfig.getConfiguration(StrangerConstants.SPARK_CONFIG_FIELD))

    logger.debug("processing data sources ...")
    application.getDataSources.asScala.foreach(dataSource => {
      val ds = dataSource.asInstanceOf[DataSourceImpl]
      logger.debug("processing source - {}", ds.getSource.getName)
      val dataBag = DataLoaderFactory.getDataLoader(ds.getSource, sparkSession).load(ds.getSource)
      ProcessUtil.processDataBag(dataBag, ds.getView)
    })
    logger.debug("data sources processed ...")

    logger.debug("processing transformations ...")
    application.getTransformations.asScala.foreach(transformation => {
      logger.debug("processing transformation - {}", transformation.getClass)
      val dataBag = TransformationRunnerFactory.getTransformationRunner(transformation, sparkSession).run(transformation)
      transformation match {
        case sq: SqlTransformation => ProcessUtil.processDataBag(dataBag, sq.getView)
        case other => throw new IllegalStateException(s"transformation is unknown - ${other.getClass}")
      }
    })

    logger.debug("processing data sinks ...")
    application.getDataSinks.asScala.foreach(dataSink => {
      val ds = dataSink.asInstanceOf[DataSinkImpl]
      logger.debug("processing sink - {}", ds.getTarget.getName)
      DataSinkFactory.getDataSink(ds.getTarget).sink(ProcessUtil.executeSql(ds.getSql, sparkSession), ds.getTarget)
    })

    new ExecutionResult(ExecutionResult.ExecutionStatus.SUCCESS, StrangerConstants.APP_EXE_SUCCESS_MESSAGE, null)
  }
}
