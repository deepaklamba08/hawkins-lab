package org.stranger

import scala.language.dynamics
import org.stranger.process.{IApplicationRunner, Orchestrator}
import org.stranger.common.model.id.{Id, StringId}
import org.stranger.common.rc.RuntimeConfiguration
import org.stranger.common.util.StrangerConstants
import org.stranger.data.store.repo.impl.{JsonApplicationRepository, JsonExecutionResultRepository}
import org.stranger.data.store.repo.{ApplicationRepository, ExecutionResultRepository}
import org.stranger.process.spark.engine.SparkApplicationRunner

import java.io.File
import java.util.Properties
import scala.io.Source
import scala.util.{Failure, Success, Try}
import scala.collection.JavaConverters._

case class StrangerAppArguments(arguments: Map[String, String]) extends Dynamic {
  require(arguments != null && !arguments.isEmpty,
    s"arguments can not be null or empty")

  def selectDynamic(name: String): String = arguments(name)

  def appId: Id = {
    val id = this.arguments(StrangerConstants.APP_PARAM_ID)
    new StringId(id)
  }

  def submitter: String = this.arguments(StrangerConstants.APP_PARAM_SUBMITTER)

  def getValue(key: String): String = this.arguments.get(key).fold(throw new IllegalStateException(s"key not found - $key"))(v => v)


}

object StrangerAppArguments {
  private final val MANDATORY_PARAMS = Seq(StrangerConstants.APP_PARAM_ID, StrangerConstants.APP_PARAM_SUBMITTER)

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
    new StrangerAppArguments(this.validate(argumentsMap))
  }

  private def validate(arguments: Map[String, String]): Map[String, String] = {
    val missing = MANDATORY_PARAMS.filter(param => !arguments.contains(param))
    if (!missing.isEmpty) {
      throw new IllegalArgumentException(s"mandatory parameters not provided, missing - ${missing.mkString(",")}")
    }
    arguments
  }
}

object DiHelper {
  def createOrchestrator(arguments: StrangerAppArguments): Orchestrator = {
    val runtimeConfiguration = this.createRuntimeConfiguration(arguments)
    val applicationRepository: ApplicationRepository = this.createApplicationRepository(runtimeConfiguration)
    val executionResultRepository: ExecutionResultRepository = this.createExecutionResultRepository(runtimeConfiguration)
    val applicationRunner: IApplicationRunner = this.createApplicationRunner(runtimeConfiguration)

    new Orchestrator(applicationRepository, executionResultRepository, applicationRunner)
  }

  private def createApplicationRepository(runtimeConfiguration: RuntimeConfiguration): ApplicationRepository = {
    new JsonApplicationRepository(new File(runtimeConfiguration.getStringConfig("appConfig")),
      java.util.Optional.of(runtimeConfiguration.getAll()))
  }

  private def createExecutionResultRepository(runtimeConfiguration: RuntimeConfiguration): ExecutionResultRepository = {
    new JsonExecutionResultRepository(new File(runtimeConfiguration.getStringConfig("executionResultDirectory")))
  }

  private def createRuntimeConfiguration(arguments: StrangerAppArguments): RuntimeConfiguration = {
    val runnerProperties = new File(arguments.getValue("runnerProperties"))
    val executionProperties = Try(Source.fromFile(runnerProperties)) match {
      case Success(source) =>
        Try(source.bufferedReader()) match {
          case Success(reader) => val properties: Properties = new Properties()
            properties.load(reader)
            source.close()
            reader.close()
            properties
          case Failure(exception) =>
            throw new IllegalStateException(s"error occurred curred while reading file - $runnerProperties", exception)
        }

      case Failure(exception) =>
        throw new IllegalStateException(s"error occurred while reading file - $runnerProperties", exception)
    }

    val configMap = executionProperties.stringPropertyNames().asScala.map(key => (key, executionProperties.getProperty(key))).toMap
    RuntimeConfiguration(arguments.arguments, configMap)
  }

  private def createApplicationRunner(runtimeConfiguration: RuntimeConfiguration): IApplicationRunner = {
    new SparkApplicationRunner(runtimeConfiguration)
  }
}