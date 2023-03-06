package org.stranger.process.spark.execution

class RuntimeConfiguration private[execution](configMap: Map[String, String]) {


  def getStringConfig(key: String): String = this.configMap.getOrElse(key, throw new IllegalStateException(s"config not present for key - $key"))

  def getIntConfig(key: String): Int = this.getStringConfig(key).toInt

  def getErrorMessageLength = this.getIntConfig(ConfigConstants.ERROR_MSG_LEN)
}

private object ConfigConstants {
  final val ERROR_MSG_LEN = "exception.message.length"
}

object RuntimeConfiguration {
  def apply(configMap: Map[String, String], loadDefault: Boolean = true): RuntimeConfiguration = {
    val allConfigMap = configMap ++ this.getDefaultConfiguration()
    new RuntimeConfiguration(allConfigMap)
  }

  def apply(): RuntimeConfiguration = {
    new RuntimeConfiguration(this.getDefaultConfiguration())
  }

  private def getDefaultConfiguration(): Map[String, String] = {
    Map(ConfigConstants.ERROR_MSG_LEN -> "1023")
    Map.empty[String, String]
  }
}

