package org.stranger.process.spark.execution.sink

import org.stranger.common.model.trgt.Target
import org.stranger.process.spark.execution.loader.DataBag

trait DataSink {

  def sink(dataBag: DataBag, target: Target): Unit
}

object DataSinkFactory {

  def getDataSink(target: Target): DataSink = {
    null
  }
}