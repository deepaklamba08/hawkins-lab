package org.stranger.process.spark.execution.loader.impl

import org.apache.spark.sql.{DataFrame, SparkSession}
import org.slf4j.LoggerFactory
import org.stranger.common.exception.StrangerExceptions.InvalidConfigurationException
import org.stranger.common.model.configuration.Configuration
import org.stranger.common.model.source.SourceDetail
import org.stranger.common.model.source.`type`.{FileFormat, FileSource}
import org.stranger.process.spark.execution.loader.DataLoader
import org.stranger.process.spark.execution.model.DataBag

class FileSourceDataLoader(sparkSession: SparkSession) extends DataLoader {

  private val logger = LoggerFactory.getLogger(this.getClass)

  override def load(sourceDetail: SourceDetail): DataBag = {
    logger.info("Executing : FileSourceDataLoader.load()")
    val fileSource = sourceDetail match {
      case fs: FileSource => fs
      case other => throw new InvalidConfigurationException(s"invalid source type - $other")
    }

    val reader = fileSource.getFileFormat match {
      case FileFormat.CSV => csvReader(fileSource.getConfiguration) _
      case FileFormat.PARQUET => parquetReader(fileSource.getConfiguration) _
      case FileFormat.ORC => orcReader(fileSource.getConfiguration) _
      case FileFormat.AVRO => avroReader(fileSource.getConfiguration) _
      case FileFormat.JSON => jsonReader(fileSource.getConfiguration) _
      case other => throw new InvalidConfigurationException(s"invalid file format - $other")
    }
    val dataFrame = reader(Seq(fileSource.getLocation))
    logger.info("Exiting : FileSourceDataLoader.load()")
    DataBag(dataFrame)
  }

  private def csvReader(config: Configuration)(location: Seq[String]): DataFrame = {
    logger.info("reading csv source ...")
    sparkSession.read.format("csv")
      .option("header", config.getBoolean("header", false))
      .load(location: _*)
  }

  private def parquetReader(config: Configuration)(location: Seq[String]): DataFrame = {
    logger.info("reading parquet source ...")
    sparkSession.read.format("parquet").load(location: _*)
  }

  private def orcReader(config: Configuration)(location: Seq[String]): DataFrame = {
    logger.info("reading orc source ...")
    sparkSession.read.format("orc").load(location: _*)
  }

  private def avroReader(config: Configuration)(location: Seq[String]): DataFrame = {
    logger.info("reading avro source ...")
    sparkSession.read.format("avro").load(location: _*)
  }

  private def jsonReader(config: Configuration)(location: Seq[String]): DataFrame = {
    logger.info("reading json source ...")
    sparkSession.read.format("json").load(location: _*)
  }
}
