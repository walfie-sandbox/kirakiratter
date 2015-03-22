package com.github.walfie.kirakiratter

import play.api.GlobalSettings
import com.typesafe.config.{Config, ConfigFactory}

object Kirakiratter extends GlobalSettings {
  lazy val config: Config = ConfigFactory.load
}

