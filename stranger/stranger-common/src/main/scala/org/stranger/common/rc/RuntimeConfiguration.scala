package org.stranger.common.rc

class RuntimeConfiguration private[rc](cli: Map[String, String], configMap: Map[String, String]) {

  def getStringConfig(key: String): String = this.configMap.getOrElse(key,
    throw new IllegalStateException(s"config not present for key - $key"))

  def getIntConfig(key: String): Int = this.getStringConfig(key).toInt

  def getErrorMessageLength = this.getIntConfig(ConfigConstants.ERROR_MSG_LEN)
}

private object ConfigConstants {
  final val ERROR_MSG_LEN = "exception.message.length"
}

object RuntimeConfiguration {
  def apply(cli: Map[String, String], configMap: Map[String, String], loadDefault: Boolean = true): RuntimeConfiguration = {
    val allConfigMap = configMap ++ this.getDefaultConfiguration()
    new RuntimeConfiguration(cli, allConfigMap)
  }

  def apply(cli: Map[String, String]): RuntimeConfiguration = {
    new RuntimeConfiguration(cli, this.getDefaultConfiguration())
  }

  private def getDefaultConfiguration(): Map[String, String] = {
    Map(ConfigConstants.ERROR_MSG_LEN -> "1023")
    Map.empty[String, String]
  }
}
