package org.stranger.process.spark.execution.loader

import org.apache.spark.sql.SparkSession
import org.stranger.common.exception.StrangerExceptions.InvalidConfigurationException
import org.stranger.common.model.source.{Source, SourceDetail}
import org.stranger.common.model.source.`type`.FileSource
import org.stranger.process.spark.execution.loader.impl.FileSourceDataLoader
import org.stranger.process.spark.execution.model.DataBag

trait DataLoader {
  def load(source: SourceDetail): DataBag
}


object DataLoaderFactory {

  def getDataLoader(source: Source, sparkSession: SparkSession): DataLoader = {
    source.getSourceDetail match {
      case _: FileSource => new FileSourceDataLoader(sparkSession)
      case other => throw new InvalidConfigurationException(s"invalid source type - $other")
    }
  }
}