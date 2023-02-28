package org.stranger.process.spark.execution.loader

import org.apache.spark.sql.{DataFrame, SparkSession}
import org.stranger.common.model.source.Source

trait DataLoader {

  def load(source: Source): DataBag
}

case class DataBag(dataFrame: DataFrame)

object DataLoaderFactory {

  def getDataLoader(source: Source,sparkSession: SparkSession): DataLoader = {
    null
  }
}