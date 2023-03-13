package org.stranger.process.spark.execution.model

import org.apache.spark.sql.DataFrame

case class DataBag(dataFrame: DataFrame, metadata: Option[DataBagMetadata] = None)

class DataBagMetadata {

}
