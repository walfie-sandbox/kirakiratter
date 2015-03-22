package com.github.walfie.kirakiratter.helpers

import com.typesafe.config.{Config, ConfigFactory}
import reactivemongo.api.collections.default.BSONCollection
import reactivemongo.api.{MongoDriver, DB}
import scala.concurrent.ExecutionContext.Implicits.global

import com.github.walfie.kirakiratter.services.{UsersService, MongoUsersService}
import com.github.walfie.kirakiratter.util.MongoHelpers

trait DBSpecHelper {
  val config: Config = ConfigFactory.load

  val uri: String = config.getString("mongodb.uri")
  val db: DB = MongoHelpers.dbFromURI(uri)
  val usersCollection: BSONCollection = db("users")

  val usersService: UsersService = new MongoUsersService(usersCollection)
}

