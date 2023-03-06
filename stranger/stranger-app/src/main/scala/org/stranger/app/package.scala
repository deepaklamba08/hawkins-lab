package org.stranger

import scala.language.dynamics
import org.stranger.process.Orchestrator
import org.stranger.common.model.id.Id

case class StrangerAppArguments(arguments: Map[String, String]) extends Dynamic {
  require(arguments != null && !arguments.isEmpty,
    s"arguments can not be null or empty")

  def selectDynamic(name: String): String = arguments(name)

  def appId: Id = null

  def submitter: String = null


}

object StrangerAppArguments {
  def apply(arguments: Array[String]): StrangerAppArguments = {
    if (arguments.length % 2 != 0) {
      throw new IllegalArgumentException(s"arguments should be in key value pairs, no. of arguments must be even")
    }
    val argumentsMap = arguments.sliding(2, 2).foldLeft(Map.empty[String, String])((builder, pair) => {
      pair.toList match {
        case key :: value :: Nil => builder ++ Map(key -> value)
        case other :: Nil => throw new IllegalArgumentException(s"can not use argument without name or value - $other")
      }
    })
    new StrangerAppArguments(argumentsMap)
  }
}

object DiHelper {
  def createOrchestrator: Orchestrator = {
    null
  }
}