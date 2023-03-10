package org.stranger.app

import org.slf4j.LoggerFactory
import org.stranger.{DiHelper, StrangerAppArguments}

import scala.util.{Failure, Success, Try}

object StrangerApp {

  private val logger = LoggerFactory.getLogger(this.getClass)

  def main(args: Array[String]): Unit = {
    logger.info("starting StrangerApp ...")

    Try({
      val arguments = StrangerAppArguments(args)
      val orchestrator = DiHelper.createOrchestrator(arguments)
      orchestrator.executeApplication(arguments.appId, arguments.submitter)
    }) match {
      case Success(_) => logger.info("exiting StrangerApp ...")
      case Failure(exception) => logger.error("app execution failed", exception)
    }


  }


}
