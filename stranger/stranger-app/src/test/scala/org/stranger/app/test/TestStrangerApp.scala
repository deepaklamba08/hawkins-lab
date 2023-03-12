package org.stranger.app.test
import org.stranger.app.StrangerApp

object TestStrangerApp extends App {

  val arguments = Array("appId", "1", "submitter", "xyz", "appConfig",
    """G:\dev\hawkins_lab\stranger\stranger-store\src\main\resources\repository\json\app_config.json""")
  StrangerApp.main(arguments)
}
