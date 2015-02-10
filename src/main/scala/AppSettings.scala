
import com.typesafe.config.Config
import com.typesafe.config.{Config, ConfigFactory}

import scala.util.Try

class AppSettings(conf: Option[Config] = None){

  val rootConfig = conf match {
    case Some(c) => c.withFallback(ConfigFactory.load)
    case _ => ConfigFactory.load
  }

  val AppName=Try(rootConfig.getString("app-name"),"demo-app")

  val minDelay = Try(rootConfig.getInt("mindelay")) getOrElse 0

}
