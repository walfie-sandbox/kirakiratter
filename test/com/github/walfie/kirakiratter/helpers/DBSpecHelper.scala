package com.github.walfie.kirakiratter.helpers

import play.modules.reactivemongo.ReactiveMongoPlugin
import reactivemongo.api.{MongoDriver, DB}
import scala.concurrent.ExecutionContext.Implicits.global

import com.github.walfie.kirakiratter.dal.UserDao

trait DBSpecHelper {
  val driver = new MongoDriver
  val connection = driver.connection(List("localhost"))
  def db: DB = connection("kirakiratter_test")

  val userDao: UserDao = new UserDao(db)
}

