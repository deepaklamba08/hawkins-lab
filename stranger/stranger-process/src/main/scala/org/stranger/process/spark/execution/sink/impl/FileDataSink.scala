package org.stranger.process.spark.execution.sink.impl

import org.slf4j.LoggerFactory
import org.stranger.common.exception.StrangerExceptions.InvalidConfigurationException
import org.stranger.common.model.configuration.Configuration
import org.stranger.common.model.source.`type`.FileFormat
import org.stranger.common.model.trgt.TargetDetail
import org.stranger.common.model.trgt.`type`.FileTargetDetail
import org.stranger.process.spark.execution.model.DataBag
import org.stranger.process.spark.execution.sink.DataSink

class FileDataSink extends DataSink {
  private val logger = LoggerFactory.getLogger(this.getClass)

  override def sink(dataBag: DataBag, targetDetail: TargetDetail): Unit = {
    logger.info("Executing : FileDataSink.sink()")
    val fileTarget = targetDetail match {
      case ft: FileTargetDetail => ft
      case other => throw new InvalidConfigurationException(s"invalid target type - $other")
    }

    val writer = fileTarget.getFileFormat match {
      case FileFormat.CSV => csvWriter(fileTarget.getMode, dataBag, fileTarget.getConfiguration) _
      case FileFormat.PARQUET => parquetWriter(fileTarget.getMode, dataBag, fileTarget.getConfiguration) _
      case FileFormat.ORC => orcWriter(fileTarget.getMode, dataBag, fileTarget.getConfiguration) _
      case FileFormat.AVRO => avroWriter(fileTarget.getMode, dataBag, fileTarget.getConfiguration) _
      case FileFormat.JSON => jsonWriter(fileTarget.getMode, dataBag, fileTarget.getConfiguration) _
      case other => throw new InvalidConfigurationException(s"invalid file format - $other")
    }
    writer(fileTarget.getLocation)

  }

  private def csvWriter(saveMode: String, dataBag: DataBag, config: Configuration)(location: String): Unit = {
    logger.info("writing csv target ...")
    dataBag.dataFrame.write.format("csv").mode(saveMode)
      .option("header", config.getBoolean("header", false))
      .save(location)

  }

  private def parquetWriter(saveMode: String, dataBag: DataBag, config: Configuration)(location: String): Unit = {
    logger.info("writing parquet target ...")
    dataBag.dataFrame.write.format("parquet").mode(saveMode).save(location)
  }

  private def orcWriter(saveMode: String, dataBag: DataBag, config: Configuration)(location: String): Unit = {
    logger.info("writing orc target ...")
    dataBag.dataFrame.write.format("orc").mode(saveMode).save(location)
  }

  private def avroWriter(saveMode: String, dataBag: DataBag, config: Configuration)(location: String): Unit = {
    logger.info("writing avro target ...")
    dataBag.dataFrame.write.format("avro").mode(saveMode).save(location)
  }

  private def jsonWriter(saveMode: String, dataBag: DataBag, config: Configuration)(location: String): Unit = {
    logger.info("writing json target ...")
    dataBag.dataFrame.write.format("json").mode(saveMode).save(location)
  }
}
