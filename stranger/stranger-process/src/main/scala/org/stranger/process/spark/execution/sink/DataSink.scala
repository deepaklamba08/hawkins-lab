package org.stranger.process.spark.execution.sink

import org.stranger.common.exception.StrangerExceptions.InvalidConfigurationException
import org.stranger.common.model.trgt.{Target, TargetDetail}
import org.stranger.common.model.trgt.`type`.FileTargetDetail
import org.stranger.process.spark.execution.model.DataBag
import org.stranger.process.spark.execution.sink.impl.FileDataSink

trait DataSink {

  def sink(dataBag: DataBag, target: TargetDetail): Unit
}

object DataSinkFactory {

  def getDataSink(target: Target): DataSink = {
    target.getTargetDetail match {
      case _: FileTargetDetail => new FileDataSink()
      case other => throw new InvalidConfigurationException(s"invalid source type - $other")
    }
  }
}