package com.github.walfie.kirakiratter.helpers

import com.typesafe.config.{Config, ConfigFactory}
import reactivemongo.api.DB
import scala.concurrent.ExecutionContext.Implicits.global

import com.github.walfie.kirakiratter.util.MongoHelpers

trait DBSpecHelper {
  private val config: Config = ConfigFactory.load

  val uri: String = config.getString("mongodb.uri")
  val db: DB = MongoHelpers.dbFromURI(uri)
}

